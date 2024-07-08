package com.example.fraudtector.ISO8583.Core.ServerHandler;

import com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration.FieldConfiguration;
import com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration.FieldConfigurationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.jpos.iso.ISOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServerHandler {

    private final FieldConfigurationService fieldConfigurationService;

    @Autowired
    public ServerHandler(FieldConfigurationService fieldConfigurationService) {
        this.fieldConfigurationService = fieldConfigurationService;
    }

    public void handle(Connection connection, List<FieldConfiguration> fieldConfigurations) throws IOException, ISOException {
        try {
            connection.inbound()
                    .receive()
                    .asByteArray()
                    .subscribe(data -> {
                        try {
                            handleClient(new ByteArrayInputStream(data), connection, fieldConfigurations);
                        } catch (IOException | ISOException e) {
                            System.out.println("Error" + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }



    public void handleClient(InputStream inputStream, Connection connection, List<FieldConfiguration> fieldConfigurations) throws IOException, ISOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        try {
            while (true) {
                String isoMessage = readISOMessage(dataInputStream);

                if (isoMessage == null) {
                    break;
                }

                byte[] rawByte = isoMessage.getBytes(StandardCharsets.UTF_8);

                int lastDigit = 0;
                Map<String, String> parsedField = new LinkedHashMap<>();

                int mtiLength = 4;
                byte[] mtiByte = new byte[mtiLength];

                System.arraycopy(rawByte, 0, mtiByte, 0, mtiLength);
                lastDigit += mtiLength;

                parsedField.put("0", new String(mtiByte, StandardCharsets.ISO_8859_1));

                int bitmapLength = 16;
                BitSet bmap = hex2BitSet(rawByte, lastDigit, bitmapLength << 3);
                lastDigit += Math.min(bitmapLength << 1, (bmap.get(1) ? 128 : 64) >> 2);

                parsedField.put("1", bmap.toString());

                for (int i = bmap.nextSetBit(2); i != -1; i = bmap.nextSetBit(i + 1)) {
                    int bitActive = i;

                    List<FieldConfiguration> temp = fieldConfigurations.stream().filter(v1 -> v1.getFieldId() == bitActive).collect(Collectors.toList());

                    if (!temp.isEmpty()) {
                        FieldConfiguration config = temp.get(0);

                        int embeddedLength = 0;
                        int nDigit = 0;
                        byte[] dataByte;

                        switch (config.getDataType()) {
                            case "LLCHAR":
                            case "LLNUM":
                                nDigit = 2;
                                embeddedLength = decodeLLLength(rawByte, lastDigit);
                                break;
                            case "LLLCHAR":
                            case "LLLNUM":
                                nDigit = 3;
                                embeddedLength = decodeLLLLength(rawByte, lastDigit);
                                break;
                        }

                        if (nDigit != 0) {
                            embeddedLength = Math.min(embeddedLength, config.getLength());
                            dataByte = new byte[embeddedLength];
                            System.arraycopy(rawByte, lastDigit + nDigit, dataByte, 0, embeddedLength);
                            lastDigit += nDigit + embeddedLength;
                            parsedField.put(String.valueOf(config.getFieldId()), new String(dataByte, StandardCharsets.ISO_8859_1));
                        } else {
                            if (config.getDataType().equals("BINARY")) {
                                dataByte = hex2byte(rawByte, lastDigit, config.getLength());
                                parsedField.put(String.valueOf(config.getFieldId()), hexString(dataByte));
                                lastDigit += config.getLength() * 2;
                                continue;
                            } else {
                                dataByte = new byte[config.getLength()];
                                System.arraycopy(rawByte, lastDigit, dataByte, 0, config.getLength());
                            }

                            lastDigit += config.getLength();
                        }

                        parsedField.put(String.valueOf(config.getFieldId()), new String(dataByte, StandardCharsets.ISO_8859_1));
                        String fieldValue = new String(dataByte, StandardCharsets.ISO_8859_1);
                        if (!config.getPad()) {
                            fieldValue = fieldValue.trim();
                        }
                        parsedField.put(String.valueOf(config.getFieldId()), fieldValue);
                    }
                }
                System.out.println("ISO Message: " + isoMessage);
                System.out.println(parsedField);

                String responseMessage = createISOResponse(parsedField);
                sendISOMessage(connection, responseMessage);
                System.out.println("Sent ISO Message: " + responseMessage);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createISOResponse(Map<String, String> parsedField) {
        StringBuilder response = new StringBuilder();

        Map<Integer, Object> container = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : parsedField.entrySet()) {
            int fieldId = Integer.parseInt(entry.getKey());
            String fieldValue = entry.getValue();
            container.put(fieldId, fieldValue);
        }

        container.put(0, "0210");
        container.put(7, generateDate(Map.of("format", "MMddhhmmss", "timeZone", "GMT+7", "additionalTime", "0")));
        container.put(11, generateRandomNumber(6));
        container.put(12, generateDate(Map.of("format", "hhmmss", "timeZone", "GMT+7", "additionalTime", "0")));
        container.put(13, generateDate(Map.of("format", "MMdd", "timeZone", "GMT+7", "additionalTime", "0")));
        container.put(15, generateDate(Map.of("format", "MMdd", "timeZone", "GMT+7", "additionalTime", "1")));
        container.put(37, generateRandomNumber(12));
        container.put(39, "00");

        int mf = Math.min(128, 192);
        BitSet bmap = new BitSet(mf + 62 >> 6 << 6);
        bmap.set(1);

        container.forEach((key, value) -> {
            if (key != 0) bmap.set(key);
        });

        int len = container.size() >= 8 ? bmap.length() + 62 >> 6 << 3 : container.size();

        container.put(1, hexString(bitSet2byte(bmap, len)));

        Map<Integer, Object> containerSort = getIntegerObjectMap(container, response);

        return response.toString();
    }

    private void sendISOMessage(Connection connection, String responseMessage) {
        byte[] responseBytes = responseMessage.getBytes(StandardCharsets.UTF_8);
        connection.outbound().sendByteArray(Mono.just(responseBytes)).then().subscribe();
    }

    private Map<Integer, Object> getIntegerObjectMap(Map<Integer, Object> container, StringBuilder stringBuilder) {
        List<FieldConfiguration> fieldConfigurations = fieldConfigurationService.getAllFieldConfiguration();

        Map<Integer, Object> objectTreeMap = new TreeMap<>(container);

        objectTreeMap.forEach((key, value) -> {
            FieldConfiguration spec = getSpecContainerByFieldId(fieldConfigurations, key);
            if (spec != null) {
                String formattedValue = formatFieldValue(value.toString(), spec);
                stringBuilder.append(formattedValue);
            } else {
                stringBuilder.append(value);
            }
        });
        return objectTreeMap;
    }

    private static FieldConfiguration getSpecContainerByFieldId(List<FieldConfiguration> fieldConfigurations, int fieldId) {
        return fieldConfigurations.stream()
                .filter(spec -> spec.fieldId == fieldId)
                .findFirst()
                .orElse(null);
    }

    private static String formatFieldValue(String value, FieldConfiguration spec) {
        String formattedValue = value;
        switch (spec.dataType) {
            case "LLCHAR":
            case "LLNUM":
                formattedValue = String.format("%02d%s", value.length(), value);
                break;
            case "LLLCHAR":
            case "LLLNUM":
                formattedValue = String.format("%03d%s", value.length(), value);
                break;
        }
        return formattedValue;
    }

    public static String generateDate(Map<String, String> args) {
        Calendar calendar = Calendar.getInstance();

        String format = args.get("format");
        String timeZone = args.get("timeZone");
        String additionalTime = args.get("additionalTime");

        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(additionalTime));

        Date date = calendar.getTime();

        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        outputFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

        return outputFormat.format(date);
    }

    public static String generateRandomNumber(int size) {
        return RandomStringUtils.random(size, false, true);
    }

    public static byte[] bitSet2byte(BitSet b, int bytes) {
        int len = bytes * 8;
        byte[] d = new byte[bytes];

        for (int i = 0; i < len; ++i) {
            if (b.get(i + 1)) {
                d[i >> 3] = (byte) (d[i >> 3] | 128 >> i % 8);
            }
        }

        if (len > 64) {
            d[0] = (byte) (d[0] | 128);
        }

        if (len > 128) {
            d[8] = (byte) (d[8] | 128);
        }

        return d;
    }

    private static void sendISOMessage(DataOutputStream outputStream, String isoMessage) throws IOException {
        byte[] messageBytes = isoMessage.getBytes(StandardCharsets.UTF_8);
        outputStream.write(messageBytes);
        outputStream.flush();
    }

    public static BitSet hex2BitSet(byte[] b, int offset, int maxBits) {
        int len = maxBits > 64 ? ((Character.digit((char) b[offset], 16) & 8) == 8 ? 128 : 64) : maxBits;
        if (len > 64 && maxBits > 128 && b.length > offset + 16 && (Character.digit((char) b[offset + 16], 16) & 8) == 8) {
            len = 192;
        }

        BitSet bmap = new BitSet(len);

        for (int i = 0; i < len; ++i) {
            int digit = Character.digit((char) b[offset + (i >> 2)], 16);
            if ((digit & 8 >> i % 4) > 0) {
                bmap.set(i + 1);
                if (i == 65 && maxBits > 128) {
                    len = 192;
                }
            }
        }
        return bmap;
    }

    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];

        for (int i = 0; i < len * 2; ++i) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] = (byte) (d[i >> 1] | Character.digit((char) b[offset + i], 16) << shift);
        }

        return d;
    }

    public static final String[] hexStrings;

    static {
        hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            StringBuilder d = new StringBuilder(2);
            char ch = Character.forDigit((byte) i >> 4 & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            ch = Character.forDigit((byte) i & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            hexStrings[i] = d.toString();
        }
    }

    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        byte[] var2 = b;
        int var3 = b.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte aB = var2[var4];
            d.append(hexStrings[aB & 255]);
        }
        return d.toString();
    }


    public static int decodeLLLength(byte[] b, int lastDigitIn) {
        int len = 0;
        int nDigit = 2;

        for (int i = 0; i < nDigit; i++) {
            byte d = b[lastDigitIn + i];
            if (d < '0' || d > '9') {
                System.out.println("Expected Digit not Char");
            }
            len = len * 10 + d - (byte) '0';
        }
        return len;
    }

    public static int decodeLLLLength(byte[] b, int lastDigitIn) {
        int len = 0;
        int nDigit = 3;

        for (int i = 0; i < nDigit; i++) {
            byte d = b[lastDigitIn + i];
            if (d < '0' || d > '9') {
                System.out.println("Expected Digit not Char");
            }
            len = len * 10 + d - (byte) '0';
        }
        return len;
    }

    private static String readISOMessage(DataInputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead = inputStream.read(buffer);
        if (bytesRead == -1) {
            return null;
        }
        return new String(buffer, 0, bytesRead);
    }
}
