package com.example.fraudtector;

import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfgService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class FraudtectorApplication {
    @Autowired
    private NetworkCfgService networkService;

    public static void main(String[] args) {
        SpringApplication.run(FraudtectorApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
