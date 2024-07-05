package com.example.fraudtector.SpringLogic.NetworkCfg;
import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
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
