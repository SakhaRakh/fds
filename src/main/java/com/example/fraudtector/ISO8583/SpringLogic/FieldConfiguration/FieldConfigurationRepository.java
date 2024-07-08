package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldConfigurationRepository extends JpaRepository<FieldConfiguration, Long>{
    List<FieldConfiguration> findAllByOrderByFieldIdAsc();

    FieldConfiguration getFieldConfigurationByFieldId(int fieldId);
    List<FieldConfiguration> findAllBySchemeConfigurationId(Long schemeConfiguration);
}
