package com.example.fraudtector.SpringLogic.Endpoint;

import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findByUrlContainsOrderByEndpointIdAsc(String name);
    List<Endpoint> findAllByConfigOrderByUrlAsc(Long configId);
    
    List<Endpoint> findBySpec_SpecId(Long specId);
    List<Endpoint> findByConfig_ConfigId(Long configId);
    Endpoint findByUrl(String url);

    Optional<Endpoint> findByEndpointId(Long endpointId);
    Optional<Endpoint> findByUrlAndConfig(String url, NetworkCfg config);

}
