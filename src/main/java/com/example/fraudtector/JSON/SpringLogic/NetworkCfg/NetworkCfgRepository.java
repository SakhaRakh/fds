package com.example.fraudtector.JSON.SpringLogic.NetworkCfg;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkCfgRepository extends JpaRepository<NetworkCfg, Long> {
    List<NetworkCfg> findByPortNumberContainsOrderByConfigIdAsc(String name);
    List<NetworkCfg> findBySpecId(Long specId);

    NetworkCfg findTopByOrderByConfigIdAsc();
    
}
