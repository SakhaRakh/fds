package com.example.fraudtector.SpringLogic.TransSpec;

import javax.validation.Valid;

import com.example.fraudtector.Domain.HttpResponse;
import com.example.fraudtector.Domain.ResponseResourceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fraudtector.Domain.ResponseData;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/transSpec")
public class TransSpecResource extends ResponseResourceEntity<TransSpec> {
    protected final TransSpecService transSpecService;
    protected final ModelMapper modelMapper;

    @Autowired
    public TransSpecResource(TransSpecService transSpecService, ModelMapper modelMapper) {
        this.transSpecService = transSpecService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Iterable<TransSpec> findAll(){
        return transSpecService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<TransSpec> findOne(@PathVariable("id") Long specId){
        
        return transSpecService.findById(specId);
    }

    @PostMapping
    public ResponseEntity<ResponseData<TransSpec>> create(@Valid @RequestBody TransSpecDto transSpecDto, Errors errors){
        ResponseData<TransSpec> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for(ObjectError error : errors.getAllErrors()){
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        TransSpec transSpec = modelMapper.map(transSpecDto, TransSpec.class);

        responseData.setStatus(true);
        responseData.setPayload(transSpecService.save(transSpec));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpResponse<TransSpec>> update(@RequestBody TransSpec requestData) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            transSpecService.update(requestData);
            httpStatus = OK;
            httpMessage = "Data Attribute Updated Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void removeOne(@PathVariable("id") Long specId){
        transSpecService.removeOne(specId);
    }
}
