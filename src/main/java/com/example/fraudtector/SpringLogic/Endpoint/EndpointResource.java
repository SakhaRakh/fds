package com.example.fraudtector.SpringLogic.Endpoint;

import java.util.*;

import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfg;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fraudtector.Domain.ResponseResourceEntity;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;

import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(path = "/endpoint")
public class EndpointResource extends ResponseResourceEntity<Object> {

    protected final EndpointService endpointService;

    @Autowired
    public EndpointResource(EndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @Autowired
    private ModelMapper modelMapper;

    //error
    @GetMapping("list")
    public List<Endpoint> fetchAll() {
            return endpointService.findAll();
    }


    @GetMapping("find/{endpointId}")
    public Endpoint findByEndpointId(@PathVariable("endpointId") long endpointId) {
        return endpointService.findOne(endpointId);
        // HttpStatus httpStatus;
        // String httpMessage;
        // try {
        // Endpoint fetchedData = endpointService.findOne(endpointId);
        // if(fetchedData != null){
        // httpStatus = OK;
        // httpMessage = "Channel Endpoint Fetched Successfully";
        // return responseWithData(httpStatus, httpMessage, fetchedData);
        // } else {
        // httpStatus = HttpStatus.NOT_FOUND;
        // httpMessage = "Data not found";
        // return response(httpStatus, httpMessage);

        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // httpStatus = INTERNAL_SERVER_ERROR;
        // httpMessage = e.getMessage();
        // return responseWithData(httpStatus, httpMessage, null);
        // }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody EndpointDto reqBody) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            Endpoint endpoint = new Endpoint();
            endpoint.setUrl(reqBody.getUrl());
            endpoint.setIsAuth(reqBody.getIsAuth());

            TransSpec transSpec = new TransSpec();
            transSpec.setSpecId(reqBody.getSpecId());
            endpoint.setSpec(transSpec);

            NetworkCfg networkCfg = new NetworkCfg();
            networkCfg.setConfigId(reqBody.getConfigId());
            endpoint.setConfig(networkCfg);

            Endpoint fetchedData = endpointService.save(endpoint);
            httpStatus = OK;
            httpMessage = "Channel Endpoint Fetched Successfully";
            return responseWithListObjectData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return new ResponseEntity<>(httpMessage, httpStatus);
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@RequestBody EndpointDto reqBody) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            Endpoint endpoint = modelMapper.map(reqBody, Endpoint.class);
            endpointService.update(reqBody);
            httpStatus = OK;
            httpMessage = "Channel Endpoint updated Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            endpointService.removeOne(id);
            httpStatus = OK;
            httpMessage = "Channel Endpoint deleted Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("/find/spec/{specId}")
    public List<Endpoint> getEndpointsBySpecId(@PathVariable Long specId) {
        try {
            return endpointService.getEndpointsBySpecId(specId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/find/config/{configId}")
    public List<Endpoint> getEndpointsByConfigId(@PathVariable Long configId) {
        try { 
            return endpointService.getEndpointsByConfigId(configId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
