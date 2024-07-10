package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fieldConfiguration")
public class ISOFieldConfigurationResource {

    private final ISOFieldConfigurationService ISOFieldConfigurationService;

    @Autowired
    public ISOFieldConfigurationResource(ISOFieldConfigurationService ISOFieldConfigurationService) {
        this.ISOFieldConfigurationService = ISOFieldConfigurationService;
    }

    @GetMapping("/list")
    public List<ISOFieldConfiguration> getSpecContainer() {
        return ISOFieldConfigurationService.getAllFieldConfiguration();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ISOFieldConfiguration> getSpecContainerById(@PathVariable Long id) {
        ISOFieldConfiguration ISOFieldConfiguration = ISOFieldConfigurationService.getFieldConfigurationById(id);
        if (ISOFieldConfiguration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ISOFieldConfiguration);
    }

    @PostMapping("/add")
    public ISOFieldConfiguration addSpecContainer(@RequestBody ISOFieldConfiguration ISOFieldConfiguration) {
        return ISOFieldConfigurationService.addFieldConfiguration(ISOFieldConfiguration);
    }

    @PostMapping("/update")
    public ISOFieldConfiguration updateFieldConfiguration(@RequestBody ISOFieldConfiguration ISOFieldConfiguration) {
        return ISOFieldConfigurationService.updateFieldConfiguration(ISOFieldConfiguration);
    }

    @DeleteMapping("/delete/{id}")
    public ISOFieldConfiguration deleteFieldConfiguration(@PathVariable Long id) {
        return ISOFieldConfigurationService.removeFieldConfiguration(id);
    }

}
