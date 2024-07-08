package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FieldConfigurationService {

    private final FieldConfigurationRepository fieldConfigurationRepository;

    @Autowired
    public FieldConfigurationService(FieldConfigurationRepository fieldConfigurationRepository) {
        this.fieldConfigurationRepository = fieldConfigurationRepository;
    }

    public List<FieldConfiguration> getAllFieldConfiguration() {
        return fieldConfigurationRepository.findAllByOrderByFieldIdAsc();
    }

    public FieldConfiguration addFieldConfiguration(FieldConfiguration fieldConfiguration) {
        return fieldConfigurationRepository.save(fieldConfiguration);
    }

    public FieldConfiguration updateFieldConfiguration(FieldConfiguration fieldConfiguration) {
        return fieldConfigurationRepository.save(fieldConfiguration);
    }
    public FieldConfiguration removeFieldConfiguration(Long id) {
        fieldConfigurationRepository.deleteById(id);
        return null;
    }
    public FieldConfiguration getFieldConfigurationById(Long id) {
        return fieldConfigurationRepository.findById(id).orElse(null);
    }

    public List<FieldConfiguration> getFieldConfigurationsBySchemeConfiguration(Long schemeConfiguration) {
        return fieldConfigurationRepository.findAllBySchemeConfigurationId(schemeConfiguration);
    }

}
