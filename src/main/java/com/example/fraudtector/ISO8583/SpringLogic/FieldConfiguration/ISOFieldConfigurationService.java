package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ISOFieldConfigurationService {

    private final ISOFieldConfigurationRepository ISOFieldConfigurationRepository;

    @Autowired
    public ISOFieldConfigurationService(ISOFieldConfigurationRepository ISOFieldConfigurationRepository) {
        this.ISOFieldConfigurationRepository = ISOFieldConfigurationRepository;
    }

    public List<ISOFieldConfiguration> getAllFieldConfiguration() {
        return ISOFieldConfigurationRepository.findAllByOrderByFieldIdAsc();
    }

    public ISOFieldConfiguration addFieldConfiguration(ISOFieldConfiguration ISOFieldConfiguration) {
        return ISOFieldConfigurationRepository.save(ISOFieldConfiguration);
    }

    public ISOFieldConfiguration updateFieldConfiguration(ISOFieldConfiguration ISOFieldConfiguration) {
        return ISOFieldConfigurationRepository.save(ISOFieldConfiguration);
    }
    public ISOFieldConfiguration removeFieldConfiguration(Long id) {
        ISOFieldConfigurationRepository.deleteById(id);
        return null;
    }
    public ISOFieldConfiguration getFieldConfigurationById(Long id) {
        return ISOFieldConfigurationRepository.findById(id).orElse(null);
    }

    public List<ISOFieldConfiguration> getFieldConfigurationsBySchemeConfiguration(Long schemeConfiguration) {
        return ISOFieldConfigurationRepository.findAllBySchemeConfigurationId(schemeConfiguration);
    }

}
