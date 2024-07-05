package com.example.fraudtector.SpringLogic.NetworkCfg;

import java.util.*;

import com.example.fraudtector.SpringLogic.Endpoint.EndpointRepository;
import com.example.fraudtector.SpringLogic.Endpoint.EndpointService;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttribute;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttributeRepository;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class NetworkCfgService {
    protected final NetworkCfgRepository networkCfgRepository;
    protected final TransSpecService transSpecService;

    @Autowired
    public NetworkCfgService(NetworkCfgRepository networkCfgRepository, TransSpecService transSpecService) {
        this.networkCfgRepository = networkCfgRepository;
        this.transSpecService = transSpecService;
    }

    public List<NetworkCfg> findAll() {
        return networkCfgRepository.findAll();
    }

    public NetworkCfg save(NetworkCfg networkCfg) {
        return networkCfgRepository.save(networkCfg);
    }

    public void update(NetworkCfgDto requestDto) {
        // Convert specId to TransSpec object
        TransSpec spec = transSpecService.findById(requestDto.getSpecId())
                .orElseThrow(() -> new RuntimeException("TransSpec not found with id: " + requestDto.getSpecId()));

        // Create NetworkCfg entity
        NetworkCfg networkCfg = new NetworkCfg();
        networkCfg.setConfigId(requestDto.getConfigId());
        networkCfg.setPortNumber(requestDto.getPortNumber());
        networkCfg.setSpecId(spec); // Ensure spec is set correctly

        // Save or update NetworkCfg in repository
        networkCfgRepository.save(networkCfg);
    }

    public NetworkCfg findOne(Long configId) {
        Optional<NetworkCfg> networkCfg = networkCfgRepository.findById(configId);
        return networkCfg.orElse(null);
    }

    public List<NetworkCfg> findByPortNumber(String portNumber) {
        return networkCfgRepository.findByPortNumberContainsOrderByConfigIdAsc(portNumber);
    }

    public void removeOne(Long configId) {
        networkCfgRepository.deleteById(configId);
    }

    public List<NetworkCfg> findBySpecId(Long specId){
        Optional<TransSpec> transSpec = transSpecService.findById(specId);
        if(transSpec == null){
            return new ArrayList<>();
        }
        return networkCfgRepository.findBySpecId(specId);
    }


}
