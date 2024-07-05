package com.example.fraudtector.SpringLogic.NetworkCfg;

import java.util.List;
import javax.validation.Valid;

import com.example.fraudtector.Domain.HttpResponse;
import com.example.fraudtector.Domain.ResponseData;
import com.example.fraudtector.Domain.ResponseResourceEntity;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttribute;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttributeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/networkCfg")
public class NetworkCfgResource extends ResponseResourceEntity<NetworkCfg> {
    protected final NetworkCfgService networkCfgService;

    @Autowired
    public NetworkCfgResource(NetworkCfgService networkCfgService) {
        this.networkCfgService = networkCfgService;
    }
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<NetworkCfg> findAll() {
        return networkCfgService.findAll();
    }

    @GetMapping("/{configId}")
    public NetworkCfg findOne(@PathVariable("configId") Long configId) {
        return networkCfgService.findOne(configId);
    }

    @PostMapping
    public ResponseEntity<ResponseData<NetworkCfg>> create(@Valid @RequestBody NetworkCfgDto networkCfgDto, Errors errors) {
        ResponseData<NetworkCfg> responseData = new ResponseData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        NetworkCfg data = modelMapper.map(networkCfgDto, NetworkCfg.class);

        responseData.setStatus(true);
        responseData.setPayload(networkCfgService.save(data));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpResponse<NetworkCfg>> update(@RequestBody NetworkCfgDto requestData) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            networkCfgService.update(requestData);
            httpStatus = OK;
            httpMessage = "Network Config Updated Successfully";
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{configId}")
    public void removeOne(@PathVariable("configId") Long configId) {
        networkCfgService.removeOne(configId);
    }

    @GetMapping("/find/spec/{specId}")
    public List<NetworkCfg> getConfigBySpec(@PathVariable("specId") Long specId){
        return networkCfgService.findBySpecId(specId);
    }
}
