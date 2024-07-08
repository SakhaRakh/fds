package com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemeConfigurationService {

    private final SchemeConfigurationRepository repository;

    @Autowired
    public SchemeConfigurationService(SchemeConfigurationRepository repository) {
        this.repository = repository;
    }

    public List<SchemeConfiguration> getAllSchemeConfiguration() {
        return repository.findAll();
    }

    public SchemeConfiguration addSchemeConfiguration(SchemeConfiguration fieldConfiguration) {
        return repository.save(fieldConfiguration);
    }

    public SchemeConfiguration updateSchemeConfiguration(SchemeConfiguration fieldConfiguration) {
        return repository.save(fieldConfiguration);
    }
    public SchemeConfiguration removeSchemeConfiguration(Long id) {
        repository.deleteById(id);
        return null;
    }
    public SchemeConfiguration getSchemeConfigurationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Optional<SchemeConfiguration> getSchemeConfigurationByFieldId(Long id) {
        return repository.findById(id);
    }
}
