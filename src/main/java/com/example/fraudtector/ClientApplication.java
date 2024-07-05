package com.example.fraudtector;

import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

public class ClientApplication {
    public static void main(String[] args) {
        HttpClient client = HttpClient.create();

        client.post()
                .uri("https://example.com/")
                .send(ByteBufFlux.fromString(Mono.just("hello")))
                .response()
                .block();
    }
}

