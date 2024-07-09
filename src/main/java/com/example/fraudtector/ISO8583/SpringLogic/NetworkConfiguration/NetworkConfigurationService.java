package com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetworkConfigurationService {

    private final NetworkConfigurationRepository configRepository;

    @Autowired
    public NetworkConfigurationService(NetworkConfigurationRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<NetworkConfiguration> getAllPort() {
        return configRepository.findAll();
    }

    public List<NetworkConfiguration> getNetworkConfigurationBySchemeConfiguration(Long schemeConfiguration) {
        return configRepository.findNetworkConfigurationBySchemeConfigurationId(schemeConfiguration);
    }

    public NetworkConfiguration addNetworkConfiguration(NetworkConfiguration networkConfiguration) {
        return configRepository.save(networkConfiguration);
    }

    public NetworkConfiguration updateNetworkConfiguration(NetworkConfiguration networkConfiguration) {
        return configRepository.save(networkConfiguration);
    }

    public NetworkConfiguration removeNetworkConfiguration(Long id) {
        configRepository.deleteById(id);
        return null;
    }

    public NetworkConfiguration getNetworkConfigurationById(Long id) {
        return configRepository.findById(id).orElse(null);
    }
}
