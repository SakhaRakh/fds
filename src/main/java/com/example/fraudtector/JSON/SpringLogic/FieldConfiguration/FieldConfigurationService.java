package com.example.fraudtector.JSON.SpringLogic.FieldConfiguration;

import com.example.fraudtector.JSON.SpringLogic.Endpoint.EndpointService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fraudtector.JSON.SpringLogic.Endpoint.Endpoint;
import com.example.fraudtector.JSON.SpringLogic.Endpoint.EndpointRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class FieldConfigurationService {
    protected final FieldConfigurationRepository fieldConfigurationRepository;
    protected final EndpointRepository endpointRepository;
    protected final EndpointService endpointService;


    @Autowired
    public FieldConfigurationService(FieldConfigurationRepository fieldConfigurationRepository, EndpointRepository endpointRepository, EndpointService endpointService) {
        this.fieldConfigurationRepository = fieldConfigurationRepository;
        this.endpointRepository = endpointRepository;
        this.endpointService = endpointService;
    }
    @Autowired
    private ModelMapper modelMapper;

    public List<FieldConfiguration> findAll() {
        return fieldConfigurationRepository.findAll();
    }

    @Transactional
    public FieldConfiguration createDataAttribute(FieldConfigurationDto dataAttrDto) {
        Endpoint endpoint = endpointService.findOne(dataAttrDto.getEndpointId());

        Optional<FieldConfiguration> existingAttribute = fieldConfigurationRepository.findByAttributeAndEndpoint_EndpointIdAndParentId(
                dataAttrDto.getAttribute(), dataAttrDto.getEndpointId(), dataAttrDto.getParentId());
        if (existingAttribute.isPresent()) {
            throw new IllegalArgumentException("Data attribute already exists for the given endpoint and parent id");
        }

        FieldConfiguration dataAttribute = new FieldConfiguration();
        dataAttribute.setAttribute(dataAttrDto.getAttribute());
        dataAttribute.setFieldTag(dataAttrDto.getFieldTag());
        dataAttribute.setDescription(dataAttrDto.getDescription());
        dataAttribute.setStateType(dataAttrDto.getStateType());
        dataAttribute.setParentId(dataAttrDto.getParentId());
        dataAttribute.setDataType(dataAttrDto.getDataType());
        dataAttribute.setEndpoint(endpoint);

        return fieldConfigurationRepository.save(dataAttribute);
    }

    public FieldConfiguration update(FieldConfiguration reqbody){
        this.validateDataId(reqbody.getAttrId());
        return this.fieldConfigurationRepository.save(reqbody);
    }

    protected void validateDataId(long id){
        this.fieldConfigurationRepository
            .findById(id)
            .orElseThrow(()-> new RuntimeException("Data not found"));
    }

    public FieldConfiguration findOne(Long attrId){
        return fieldConfigurationRepository.findById(attrId)
        .orElseThrow(()-> new EntityNotFoundException("data not foundd"));
    }

    public List<FieldConfiguration> getDataAttributesByEndpointId(Long endpointId) {
        return fieldConfigurationRepository.findByEndpoint_EndpointId(endpointId);
    }

//    public List<FieldConfiguration> findByStateType(String stateType) {
//        return fieldConfigurationRepository.findByStateType(stateType);
//    }

    public void removeOne(Long attrId){
        fieldConfigurationRepository.deleteById(attrId);
    }

}