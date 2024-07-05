package com.example.fraudtector.SpringLogic.TransSpec;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransSpecRepository extends JpaRepository<TransSpec, Long> {

        List<TransSpec> findByNameContainsOrderBySpecIdAsc(String name);
    
}
