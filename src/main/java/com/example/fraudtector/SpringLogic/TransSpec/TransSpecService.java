package com.example.fraudtector.SpringLogic.TransSpec;

import java.util.List;
import java.util.Optional;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransSpecService {
     protected final TransSpecRepository transSpecRepository;

    @Autowired
    public TransSpecService(TransSpecRepository transSpecRepository) {
        this.transSpecRepository = transSpecRepository;
    }
    public Iterable<TransSpec> findAll(){
        return transSpecRepository.findAll();
    }

    public TransSpec save(TransSpec transSpec){
        return transSpecRepository.save(transSpec);
    }

    public TransSpec update(TransSpec reqbody){
        this.validateDataId(reqbody.getSpecId());
        return this.transSpecRepository.save(reqbody);
    }

    protected void validateDataId(Long id){
        this.transSpecRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Data not found"));
    }

    public Optional<TransSpec> findById(Long specId) {
        return transSpecRepository.findById(specId);
    }

    public List<TransSpec> findByName(String name){
        return transSpecRepository.findByNameContainsOrderBySpecIdAsc(name);
    }
    
    public void removeOne(Long specId){
        transSpecRepository.deleteById(specId);
    }

}
