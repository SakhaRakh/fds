package com.example.fraudtector.JSON.SpringLogic.FieldConfiguration;

import com.example.fraudtector.JSON.Constant.StateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldConfigurationRepository extends JpaRepository<FieldConfiguration, Long> {

    List<FieldConfiguration> findByStateType(StateType stateType);
    Optional<FieldConfiguration> findByAttribute(String attribute);
    Optional<FieldConfiguration> findByAttributeAndEndpoint_EndpointIdAndParentId(String attribute, Long endpointId, Long parentId);
    List<FieldConfiguration> findByEndpoint_EndpointId(Long endpointId);

}