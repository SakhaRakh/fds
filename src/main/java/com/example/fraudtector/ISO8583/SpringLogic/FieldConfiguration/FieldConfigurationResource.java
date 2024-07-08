package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fieldConfiguration")
public class FieldConfigurationResource {

    private final FieldConfigurationService fieldConfigurationService;

    @Autowired
    public FieldConfigurationResource(FieldConfigurationService fieldConfigurationService) {
        this.fieldConfigurationService = fieldConfigurationService;
    }

    @GetMapping("/list")
    public List<FieldConfiguration> getSpecContainer() {
        return fieldConfigurationService.getAllFieldConfiguration();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldConfiguration> getSpecContainerById(@PathVariable Long id) {
        FieldConfiguration fieldConfiguration = fieldConfigurationService.getFieldConfigurationById(id);
        if (fieldConfiguration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fieldConfiguration);
    }

    @PostMapping("/add")
    public FieldConfiguration addSpecContainer(@RequestBody FieldConfiguration fieldConfiguration) {
        return fieldConfigurationService.addFieldConfiguration(fieldConfiguration);
    }

    @PostMapping("/update")
    public FieldConfiguration updateFieldConfiguration(@RequestBody FieldConfiguration fieldConfiguration) {
        return fieldConfigurationService.updateFieldConfiguration(fieldConfiguration);
    }

    @DeleteMapping("/delete/{id}")
    public FieldConfiguration deleteFieldConfiguration(@PathVariable Long id) {
        return fieldConfigurationService.removeFieldConfiguration(id);
    }

}
