package com.example.fraudtector.Core.JSON;

import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
import com.example.fraudtector.SpringLogic.Endpoint.EndpointService;
import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfg;
import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;

import java.util.List;

@Slf4j
@Configuration
public class ServerConfiguration {

    private final NetworkCfgService networkService;
    private final EndpointService endpointService;
    private final JSONServerConfig jsonServerConfig;

    @Autowired
    public ServerConfiguration(NetworkCfgService networkService, EndpointService endpointService, JSONServerConfig jsonServerConfig) {
        this.networkService = networkService;
        this.endpointService = endpointService;
        this.jsonServerConfig = jsonServerConfig;
    }

    @Bean
    public void run() {
        List<NetworkCfg> configs = networkService.findAll();
        for (NetworkCfg config : configs) {
            int port = Integer.parseInt(config.getPortNumber());
            List<Endpoint> endpoints = endpointService.getEndpointsByConfigId(config.getConfigId());

            HttpServer.create()
                    .port(port)
                    .route(routes -> {
                        for (Endpoint endpoint : endpoints) {
                            routes.post(endpoint.getUrl(), jsonServerConfig::handle);
                        }
                    })
                    .bindNow();
        }
    }
}
