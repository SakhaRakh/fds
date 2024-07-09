package com.example.fraudtector.ISO8583.Core;

import com.example.fraudtector.ISO8583.Core.ServerHandler.ServerHandler;
import com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration.FieldConfiguration;
import com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration.FieldConfigurationService;
import com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration.NetworkConfiguration;
import com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration.NetworkConfigurationService;
import com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration.SchemeConfiguration;
import com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration.SchemeConfigurationService;
import io.netty.handler.logging.LogLevel;
import org.jpos.iso.ISOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.io.IOException;
import java.util.List;

@Service
public class Server {
    private final NetworkConfigurationService networkConfigurationService;
    private final FieldConfigurationService fieldConfigurationService;
    private final ServerHandler serverHandler;
    private final SchemeConfigurationService schemeConfigurationService;

    @Autowired
    public Server(NetworkConfigurationService networkConfigurationService, FieldConfigurationService fieldConfigurationService, ServerHandler serverHandler, SchemeConfigurationService schemeConfigurationService) {
        this.networkConfigurationService = networkConfigurationService;
        this.fieldConfigurationService = fieldConfigurationService;
        this.serverHandler = serverHandler;
        this.schemeConfigurationService = schemeConfigurationService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public void startServers() {
        List<SchemeConfiguration> activeSchemeConfiguration = schemeConfigurationService.getAllSchemeConfiguration();


        for (SchemeConfiguration schemeConfiguration : activeSchemeConfiguration) {
            List<NetworkConfiguration> networkConfigurations = networkConfigurationService.getNetworkConfigurationBySchemeConfiguration(schemeConfiguration.getId());
            List<FieldConfiguration> fieldConfigurations = fieldConfigurationService.getFieldConfigurationsBySchemeConfiguration(schemeConfiguration.getId());
            for (NetworkConfiguration portNumber : networkConfigurations) {
                new Thread(() -> {
                    DisposableServer server = TcpServer.create()
                            .host("localhost")
                            .port(portNumber.getLocalPort())
                            .wiretap(String.format("tcp-server-%d", portNumber.getLocalPort()), LogLevel.INFO)
                            .doOnConnection(connection -> {
                                System.out.println("Client connected: " + connection.address());
                                try {
                                    serverHandler.handle(connection, fieldConfigurations);
                                } catch (IOException | ISOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .bindNow();
                    System.out.println("Server started on port " + portNumber.getLocalPort());
                    server.onDispose().block();
                }).start();
            }
        }
    }

}
