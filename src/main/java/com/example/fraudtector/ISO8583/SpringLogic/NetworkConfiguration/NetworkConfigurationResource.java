package com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/networkConfiguration")
public class NetworkConfigurationResource {
    private final NetworkConfigurationService networkConfigurationService;

    @Autowired
    public NetworkConfigurationResource(NetworkConfigurationService networkConfigurationService) {
        this.networkConfigurationService = networkConfigurationService;
    }

    @GetMapping("/list")
    public List<NetworkConfiguration> getNetworkConfiguration() {
        return networkConfigurationService.getAllPort();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkConfiguration> getNetworkConfigurationById(@PathVariable Long id) {
        NetworkConfiguration fieldConfiguration = networkConfigurationService.getNetworkConfigurationById(id);
        if (fieldConfiguration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fieldConfiguration);
    }

    @PostMapping("/add")
    public NetworkConfiguration addNetworkConfiguration(@RequestBody NetworkConfiguration fieldConfiguration) {
        return networkConfigurationService.addNetworkConfiguration(fieldConfiguration);
    }

    @PostMapping("/update")
    public NetworkConfiguration updateNetworkConfiguration(@RequestBody NetworkConfiguration fieldConfiguration) {
        return networkConfigurationService.updateNetworkConfiguration(fieldConfiguration);
    }

    @DeleteMapping("/delete/{id}")
    public NetworkConfiguration deleteNetworkConfiguration(@PathVariable Long id) {
        return networkConfigurationService.removeNetworkConfiguration(id);
    }
}
