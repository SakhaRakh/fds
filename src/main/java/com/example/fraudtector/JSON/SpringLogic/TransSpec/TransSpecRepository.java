package com.example.fraudtector.JSON.SpringLogic.TransSpec;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransSpecRepository extends JpaRepository<TransSpec, Long> {

    List<TransSpec> findByNameContainsOrderBySpecIdAsc(String name);

}
