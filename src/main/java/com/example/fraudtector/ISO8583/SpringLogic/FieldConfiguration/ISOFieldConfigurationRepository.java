package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISOFieldConfigurationRepository extends JpaRepository<ISOFieldConfiguration, Long>{
    List<ISOFieldConfiguration> findAllByOrderByFieldIdAsc();

    ISOFieldConfiguration getFieldConfigurationByFieldId(int fieldId);
    List<ISOFieldConfiguration> findAllBySchemeConfigurationId(Long schemeConfiguration);
}
