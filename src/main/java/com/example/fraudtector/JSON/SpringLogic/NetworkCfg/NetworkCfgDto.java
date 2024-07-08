package com.example.fraudtector.JSON.SpringLogic.NetworkCfg;
import com.example.fraudtector.JSON.SpringLogic.Endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NetworkCfgDto {
    private Long configId;
    private String portNumber;
    private Long specId;
    private Set<Endpoint> endpoints;
}
