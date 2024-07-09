package com.example.fraudtector.JSON.SpringLogic.TransDataAttribute;

import com.example.fraudtector.JSON.Constant.StateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransDataAttributeRepository extends JpaRepository<TransDataAttribute, Long> {

    List<TransDataAttribute> findByStateType(StateType stateType);

    Optional<TransDataAttribute> findByAttribute(String attribute);

    Optional<TransDataAttribute> findByAttributeAndEndpoint_EndpointIdAndParentId(String attribute, Long endpointId, Long parentId);

    List<TransDataAttribute> findByEndpoint_EndpointId(Long endpointId);

}