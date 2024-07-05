package com.example.fraudtector.SpringLogic.Endpoint;

import com.example.fraudtector.GeneralComponent.DataNotFoundWhenUpdate;
import com.example.fraudtector.GeneralComponent.EndpointNotFoundException;
import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfg;
import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfgRepository;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpecRepository;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class EndpointService {
    protected final EndpointRepository endpointRepository;
    protected final NetworkCfgRepository networkCfgRepository;
    protected final TransSpecRepository transSpecRepository;


    @Autowired
    public EndpointService(EndpointRepository endpointRepository, NetworkCfgRepository networkCfgRepository, TransSpecRepository transSpecRepository) {
        this.endpointRepository = endpointRepository;
        this.networkCfgRepository = networkCfgRepository;
        this.transSpecRepository = transSpecRepository;
    }

    public List<Endpoint> findAll(){
        return this.endpointRepository.findAll();
    }

    public Endpoint findOne(Long endpointId){
        Optional<Endpoint> endpoint = endpointRepository.findById(endpointId);
        if(!endpoint.isPresent()){
            return null;
        }
        return endpoint.get();
    }

    public List<Endpoint> getEndpointsBySpecId(Long specId) {
        return endpointRepository.findBySpec_SpecId(specId);
    }

    public List<Endpoint> getEndpointsByConfigId(Long configId) {
        return endpointRepository.findByConfig_ConfigId(configId);
    }

    public Endpoint save(Endpoint reqBody){
        return this.endpointRepository.save(reqBody);
    }

//    public void updateData(Endpoint data) throws DataNotFoundWhenUpdate, EndpointNotFoundException {
//        Optional<Endpoint> fetchedData = endpointRepository.findById(data.getEndpointId());
//        if (isEndpointIdIsPresent(data.getEndpointId())) {
//            if (fetchedData.isPresent()) {
//                endpointRepository.save(data);
//            } else throw new DataNotFoundWhenUpdate(data.getEndpointId());
//        } else throw new EndpointNotFoundException();
//    }

    public void update(EndpointDto reqBody) {
        this.validateDataId(reqBody.getEndpointId());
        this.validateDataAlreadyPresent(reqBody);
        this.dataMapper(reqBody);
    }

    protected void validateDataId(long id) {
        this.endpointRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Existing data not found"));
    }

    protected void validateDataAlreadyPresent(EndpointDto reqBody) {
        NetworkCfg config = networkCfgRepository.findById(reqBody.getConfigId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        this.endpointRepository.findByUrlAndConfig(reqBody.getUrl(), config)
                .ifPresent(v1 -> {
                    if (!Objects.equals(v1.getEndpointId(), reqBody.getEndpointId()))
                        throw new RuntimeException("Data already exist");
                });
    }


    protected void dataMapper(EndpointDto reqBody) {
        NetworkCfg config = networkCfgRepository.findById(reqBody.getConfigId())
                .orElseThrow(() -> new RuntimeException("Config not found"));
        TransSpec spec = transSpecRepository.findById(reqBody.getSpecId())
                .orElseThrow(() -> new RuntimeException("Spec not found"));

        if (!(reqBody.getIsAuth())) {
            this.endpointRepository.save(
                    Endpoint.builder()
                            .endpointId(reqBody.getEndpointId())
                            .url(reqBody.getUrl())
                            .config(config)
                            .spec(spec)
                            .isAuth(false)
                            .build()
            );
        } else if (reqBody.getIsAuth()) {
            this.endpointRepository.save(
                    Endpoint.builder()
                            .endpointId(reqBody.getEndpointId())
                            .url(reqBody.getUrl())
                            .config(config)
                            .spec(spec)
                            .isAuth(true)
                            .build()
            );
        }
    }

    public void removeOne(Long endpointId){
        endpointRepository.deleteById(endpointId);
    }


}

