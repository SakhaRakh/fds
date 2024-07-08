package com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schemeConfiguration")
public class SchemeConfigurationResource {

    private final SchemeConfigurationService service;

    @Autowired
    public SchemeConfigurationResource(SchemeConfigurationService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<SchemeConfiguration> getSpecContainer() {
        return service.getAllSchemeConfiguration();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchemeConfiguration> getSpecContainerById(@PathVariable Long id) {
        SchemeConfiguration fieldConfiguration = service.getSchemeConfigurationById(id);
        if (fieldConfiguration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fieldConfiguration);
    }

    @PostMapping("/add")
    public SchemeConfiguration addSpecContainer(@RequestBody SchemeConfiguration fieldConfiguration) {
        return service.addSchemeConfiguration(fieldConfiguration);
    }

    @PostMapping("/update")
    public SchemeConfiguration updateSchemeConfiguration(@RequestBody SchemeConfiguration schemeConfiguration){
         return service.updateSchemeConfiguration(schemeConfiguration);
    }

    @DeleteMapping("/delete/{id}")
    public SchemeConfiguration deleteSchemeConfiguration(@PathVariable Long id){
        return service.removeSchemeConfiguration(id);
    }
}
