package com.pd.BookingSystem.features.language.repository;

import com.pd.BookingSystem.features.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@EnableJpaRepositories
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("SELECT l FROM Employee e JOIN e.languages l WHERE e.id = :employeeId")
    Set<Language> findLanguagesByEmployeeId(@Param("employeeId") Long employeeId);

}
