package com.example.fraudtector.SpringLogic.TransDataAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.fraudtector.Domain.HttpResponse;
import com.example.fraudtector.Domain.ResponseResourceEntity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/dataAttribute")

public class TransDataAttributeResource extends ResponseResourceEntity<TransDataAttribute> {
    protected final TransDataAttributeService transDataAttributeService;

    @Autowired
    public TransDataAttributeResource(TransDataAttributeService transDataAttributeService) {
        this.transDataAttributeService = transDataAttributeService;
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<TransDataAttribute>>> fetchAllData() {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<TransDataAttribute> fetchedData = transDataAttributeService.findAll();
            httpStatus = OK;
            httpMessage = "Data Attribute Fetched Successfully";
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return responseWithListData(httpStatus, httpMessage, new LinkedList<>());
        }
    }

    @GetMapping("find/{attrId}")
    public ResponseEntity<?> findByAttrId(@PathVariable("attrId") long attrId) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            TransDataAttribute fetchedData = transDataAttributeService.findOne(attrId);
            httpStatus = HttpStatus.OK;
            httpMessage = "Data Attribute Fetched Successfully";
            return responseWithData(httpStatus, httpMessage, fetchedData);
        } catch (EntityNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
            httpMessage = e.getMessage();
            return responseWithData(httpStatus, httpMessage, null);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return responseWithData(httpStatus, httpMessage, null);
        }
    }

    @GetMapping("/listByEndpoint/{endpointId}")
    public List<TransDataAttribute> getDataAttributesByEndpointId(@PathVariable Long endpointId) {
        return transDataAttributeService.getDataAttributesByEndpointId(endpointId);
    }


    @PostMapping
    public ResponseEntity<?> createDataAttribute(@RequestBody TransDataAttributeDto dataAttrDto) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            TransDataAttribute createdData = transDataAttributeService.createDataAttribute(dataAttrDto);
            httpStatus = HttpStatus.CREATED;
            httpMessage = "Data Attribute Created Successfully";
            return responseWithData(httpStatus, httpMessage, createdData);
        } catch (IllegalArgumentException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            httpMessage = e.getMessage();
            return responseWithData(httpStatus, httpMessage, null);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return responseWithData(httpStatus, httpMessage, null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<HttpResponse<TransDataAttribute>> update(@RequestBody TransDataAttribute requestData) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            transDataAttributeService.update(requestData);
            httpStatus = OK;
            httpMessage = "Data Attribute Updated Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            transDataAttributeService.removeOne(id);
            httpStatus = OK;
            httpMessage = "Data Attribute deleted Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
