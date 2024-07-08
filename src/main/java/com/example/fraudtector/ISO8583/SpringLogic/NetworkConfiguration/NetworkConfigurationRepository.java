package com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkConfigurationRepository extends JpaRepository<NetworkConfiguration, Long> {
    List<NetworkConfiguration> findNetworkConfigurationBySchemeConfigurationId(Long schemeConfiguration);
}
