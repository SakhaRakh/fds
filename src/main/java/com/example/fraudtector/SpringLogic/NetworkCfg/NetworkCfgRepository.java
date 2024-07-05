package com.example.fraudtector.SpringLogic.NetworkCfg;

import java.util.*;

import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkCfgRepository extends JpaRepository<NetworkCfg, Long> {
    List<NetworkCfg> findByPortNumberContainsOrderByConfigIdAsc(String name);
    List<NetworkCfg> findBySpecId(Long specId);

    NetworkCfg findTopByOrderByConfigIdAsc();
    
}
