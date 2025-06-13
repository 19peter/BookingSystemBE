package com.pd.BookingSystem.features.configurations.repository;

import com.pd.BookingSystem.features.configurations.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Configuration findByKey(String key);

    Optional<Configuration> findOptionalByKey(String key);

    boolean existsByKey(String key);
}
