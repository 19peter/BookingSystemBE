package com.pd.BookingSystem.features.language.repository;

import com.pd.BookingSystem.features.language.entity.InterpreterLanguagePriceLK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface InterpreterLanguagePriceRepository extends JpaRepository<InterpreterLanguagePriceLK, Long> {
     Optional<InterpreterLanguagePriceLK> findByLanguageId(Long languageId);
}
