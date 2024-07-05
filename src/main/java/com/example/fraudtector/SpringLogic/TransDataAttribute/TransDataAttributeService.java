package com.example.fraudtector.SpringLogic.TransDataAttribute;

import com.example.fraudtector.SpringLogic.Endpoint.EndpointService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
import com.example.fraudtector.SpringLogic.Endpoint.EndpointRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class TransDataAttributeService {
    protected final TransDataAttributeRepository transDataAttributeRepository;
    protected final EndpointRepository endpointRepository;
    protected final EndpointService endpointService;


    @Autowired
    public TransDataAttributeService(TransDataAttributeRepository transDataAttributeRepository, EndpointRepository endpointRepository, EndpointService endpointService) {
        this.transDataAttributeRepository = transDataAttributeRepository;
        this.endpointRepository = endpointRepository;
        this.endpointService = endpointService;
    }
    @Autowired
    private ModelMapper modelMapper;

    public List<TransDataAttribute> findAll() {
        return transDataAttributeRepository.findAll();
    }

    @Transactional
    public TransDataAttribute createDataAttribute(TransDataAttributeDto dataAttrDto) {
        Endpoint endpoint = endpointService.findOne(dataAttrDto.getEndpointId());

        Optional<TransDataAttribute> existingAttribute = transDataAttributeRepository.findByAttributeAndEndpoint_EndpointIdAndParentId(
                dataAttrDto.getAttribute(), dataAttrDto.getEndpointId(), dataAttrDto.getParentId());
        if (existingAttribute.isPresent()) {
            throw new IllegalArgumentException("Data attribute already exists for the given endpoint and parent id");
        }

        TransDataAttribute dataAttribute = new TransDataAttribute();
        dataAttribute.setAttribute(dataAttrDto.getAttribute());
        dataAttribute.setFieldTag(dataAttrDto.getFieldTag());
        dataAttribute.setDescription(dataAttrDto.getDescription());
        dataAttribute.setStateType(dataAttrDto.getStateType());
        dataAttribute.setParentId(dataAttrDto.getParentId());
        dataAttribute.setDataType(dataAttrDto.getDataType());
        dataAttribute.setEndpoint(endpoint);

        return transDataAttributeRepository.save(dataAttribute);
    }

    public TransDataAttribute update(TransDataAttribute reqbody){
        this.validateDataId(reqbody.getAttrId());
        return this.transDataAttributeRepository.save(reqbody);
    }

    protected void validateDataId(long id){
        this.transDataAttributeRepository
            .findById(id)
            .orElseThrow(()-> new RuntimeException("Data not found"));
    }

    public TransDataAttribute findOne(Long attrId){
        return transDataAttributeRepository.findById(attrId)
        .orElseThrow(()-> new EntityNotFoundException("data not foundd"));
    }

    public List<TransDataAttribute> getDataAttributesByEndpointId(Long endpointId) {
        return transDataAttributeRepository.findByEndpoint_EndpointId(endpointId);
    }

//    public List<TransDataAttribute> findByStateType(String stateType) {
//        return transDataAttributeRepository.findByStateType(stateType);
//    }

    public void removeOne(Long attrId){
        transDataAttributeRepository.deleteById(attrId);
    }

}