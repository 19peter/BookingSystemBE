package com.pd.BookingSystem.features.client.repository;

import com.pd.BookingSystem.features.client.entity.Client;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    @Nonnull
    Optional<Client> findById(@Nonnull Long id);

}
