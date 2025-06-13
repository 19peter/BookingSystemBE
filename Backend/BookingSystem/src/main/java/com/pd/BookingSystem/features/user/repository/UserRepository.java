package com.pd.BookingSystem.features.user.repository;

import com.pd.BookingSystem.features.user.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    @Nonnull
    Optional<User> findById(@Nonnull Long id);
    Optional<User> findByEmail(String email);

}
