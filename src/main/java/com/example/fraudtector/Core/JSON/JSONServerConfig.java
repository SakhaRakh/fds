package com.example.fraudtector.Core.JSON;

import com.example.fraudtector.Constant.StateType;
import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
import com.example.fraudtector.SpringLogic.Endpoint.EndpointService;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttribute;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttributeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.*;

@Slf4j
@Service
public class JSONServerConfig {

    @Autowired
    private EndpointService endpointService;

    @Autowired
    private TransDataAttributeService transDataAttributeService;

    public Mono<Void> handle(HttpServerRequest request, HttpServerResponse response){
        return request
                .receive()
                .aggregate()
                .asString()
                .flatMap(body -> this.handle(request, response, body));
    }

    public Mono<Void> handle(HttpServerRequest request, HttpServerResponse response, String body) {
        log.info(body);
        try {
            List<TransDataAttribute> dataAttr = transDataAttributeService.findAll();
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(body);
            Map<String, Object> storeAttr = new HashMap<>();
            StringBuilder errorMessage = new StringBuilder();

            this.processJsonNode(jn, dataAttr, request.uri(), "", storeAttr, errorMessage);

            log.info(storeAttr.toString());

            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            httpHeaders.add(HttpHeaderNames.CONTENT_TYPE, "application/json");

            if (errorMessage.length() > 0) {
                return response.headers(httpHeaders).sendString(Mono.just("{\"error\": \"" + errorMessage.toString() + "\"}")).then();
            }

            ObjectNode responseMessage = om.createObjectNode();

            for (TransDataAttribute attr : dataAttr) {
                if (attr.getStateType() == StateType.RESPONSE) {
                    if (attr.getParentId() == null) {
                        JsonNode childNode = this.hasChildAttributes(attr.getAttrId(), dataAttr)
                                ? this.createChildObjectNode(attr, dataAttr)
                                : this.createValueNode(attr);
                        responseMessage.set(attr.getFieldTag(), childNode);
                    }
                }
            }

            return response.headers(httpHeaders).sendString(Mono.just(responseMessage.toString())).then();
        } catch (Exception e) {
            e.printStackTrace();
            return response.status(500).sendString(Mono.just("{ error : Internal Server Error}")).then();
        }
    }

    private JsonNode createChildObjectNode(TransDataAttribute parentAttr, List<TransDataAttribute> dataAttr){
        ObjectMapper om = new ObjectMapper();
        JsonNode parentNode;

        if (parentAttr.getDataType().equals("ARRAY")) {
            parentNode = om.createArrayNode();
            ((ArrayNode) parentNode).add(this.createArrayChildObjectNode(parentAttr, dataAttr));
        } else if (parentAttr.getDataType().equals("OBJECT")) {
            parentNode = om.createObjectNode();
            for (TransDataAttribute attr : dataAttr) {
                if(parentAttr.getAttrId().equals(attr.getParentId()) && attr.getStateType() == StateType.RESPONSE) {
                    JsonNode childNode = this.hasChildAttributes(attr.getAttrId(), dataAttr)
                            ? this.createChildObjectNode(attr, dataAttr)
                            : this.createValueNode(attr);
                    ((ObjectNode) parentNode).set(attr.getFieldTag(), childNode);
                }
            }
        } else {
            parentNode = this.createValueNode(parentAttr);
        }
        return parentNode;
    }

    private JsonNode createArrayChildObjectNode(TransDataAttribute parentAttr, List<TransDataAttribute> dataAttr){
        ObjectMapper om = new ObjectMapper();
        ObjectNode childNode = om.createObjectNode();

        for (TransDataAttribute attr : dataAttr) {
            if (parentAttr.getAttrId().equals(attr.getParentId()) && attr.getStateType() == StateType.RESPONSE) {
                JsonNode nestedChildNode;
                if (this.hasChildAttributes(attr.getAttrId(), dataAttr)) {
                    nestedChildNode = this.createChildObjectNode(attr, dataAttr);
                } else {
                    nestedChildNode = this.createValueNode(attr);
                }
                childNode.set(attr.getFieldTag(), nestedChildNode);
            }
        }
        return childNode;
    }

    private JsonNode createValueNode(TransDataAttribute attr) {
        ObjectMapper om = new ObjectMapper();
        switch (attr.getDataType()) {
            case "NUMBER":
                return om.valueToTree(200);
            case "STRING":
                return om.valueToTree("Tes Response string from db " + attr.getFieldTag());
            default:
                return om.valueToTree("Tes response default from db " + attr.getFieldTag());
        }
    }

    private void processJsonNode(JsonNode node, List<TransDataAttribute> dataAttr, String uri, String parentFieldName, Map<String, Object> storeAttr, StringBuilder errorMessage) {
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            String fullFieldName = parentFieldName.isEmpty() ? fieldName : parentFieldName + "." + fieldName;

            boolean matchFound = false;
            for (TransDataAttribute attr : dataAttr) {
                String dbFieldName = this.getFullFieldName(attr, dataAttr);
                if (fullFieldName.equals(dbFieldName) && attr.getStateType() == StateType.REQUEST) {
                    if (this.isEndpointAllowed(uri, attr.getEndpoint().getUrl())) {
                        matchFound = true;
                        if (this.hasChildAttributes(attr.getAttrId(), dataAttr)) {
                            if (fieldValue.isObject()){
                                this.processJsonNode(fieldValue, dataAttr, uri, fullFieldName, storeAttr, errorMessage);
                            } else if (fieldValue.isArray()) {
                                this.processJsonArray(fieldValue, dataAttr, uri, fullFieldName, storeAttr, errorMessage);
                            } else {
                                errorMessage
                                        .append("Field '" + fullFieldName + "' with child attributes must be an object or array.");
                            }
                        } else {
                            storeAttr.put(fullFieldName, fieldValue.asText());
                        }
                        break;
                    }
                }
            }
            if(!matchFound) {
                log.info("No Match found for: " + fullFieldName + ": " + fieldValue.asText());
                errorMessage.append("No match found for: ").append(fullFieldName).append(". ");
            }
        }
    }

    private void processJsonArray(JsonNode node, List<TransDataAttribute> dataAttr, String uri, String parentFieldName, Map<String, Object> storeAttr, StringBuilder errorMessage) {
        for (int i = 0; i < node.size(); i++) {
            JsonNode arrayItem = node.get(i);
            if (arrayItem.isObject()) {
                this.processJsonNode(arrayItem, dataAttr, uri, parentFieldName, storeAttr, errorMessage);
            } else {
                errorMessage.append("Array element '").append(parentFieldName).append("[").append(i).append("]' must be an object.");
                throw new RuntimeException("Array element " + parentFieldName + "[" + i + "] must be an object.");
            }
        }
    }

    private boolean hasChildAttributes(Long attrId, List<TransDataAttribute> dataAttr) {
        for (TransDataAttribute attr : dataAttr) {
            if (attrId.equals(attr.getParentId())) {
                return true;
            }
        }
        return false;
    }

    private String getFullFieldName(TransDataAttribute attr, List<TransDataAttribute> dataAttr) {
        StringBuilder fullFieldName = new StringBuilder(attr.getFieldTag());
        TransDataAttribute parentAttr = this.getParentAttribute(attr, dataAttr);

        while (parentAttr != null) {
            fullFieldName.insert(0, parentAttr.getFieldTag() + ".");
            parentAttr = this.getParentAttribute(parentAttr, dataAttr);
        }

        return fullFieldName.toString();
    }

    private TransDataAttribute getParentAttribute(TransDataAttribute attr, List<TransDataAttribute> dataAttr) {
        if (attr.getParentId() == null) {
            return null;
        }
        for (TransDataAttribute parentAttr : dataAttr) {
            if (attr.getParentId().equals(parentAttr.getAttrId())) {
                return parentAttr;
            }
        }
        return null;
    }

    private boolean isEndpointAllowed(String uri, String endpointUrl){
        return uri.startsWith(endpointUrl);
    }
}
