package com.example.fraudtector.SpringLogic.Endpoint;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointDto {
    private long endpointId;
    private String url;
    private Boolean isAuth;
    private long configId;
    private long specId;
}

