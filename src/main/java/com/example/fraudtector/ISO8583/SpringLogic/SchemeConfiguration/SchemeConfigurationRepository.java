package com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeConfigurationRepository extends JpaRepository<SchemeConfiguration, Long> {
}
