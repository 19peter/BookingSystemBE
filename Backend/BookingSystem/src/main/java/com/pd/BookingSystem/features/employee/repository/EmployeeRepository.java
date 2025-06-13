package com.pd.BookingSystem.features.employee.repository;

import com.pd.BookingSystem.features.employee.entity.Employee;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@EnableJpaRepositories

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Nonnull
    Optional<Employee> findById(@Nonnull Long id);

    Optional<Employee> findByEmail(String email);

    @Query("""
            SELECT e.id FROM Employee e
            JOIN e.languages l
            WHERE l.id IN :languageIds
            GROUP BY e.id
            HAVING COUNT(DISTINCT l.id) = :totalCount
            """)
    List<Long> findEmployeesWithAllLanguages(@Param("languageIds") Set<Long> languageIds, @Param("totalCount") Long totalCount);


    @Query(value = """
                WITH qualified_employees AS (
                    SELECT employee_id
                    FROM employee_languages
                    WHERE language_id IN :languageIds
                    GROUP BY employee_id
                    HAVING COUNT(DISTINCT language_id) = :totalCount
                ),
                busy_employees AS (
                    SELECT DISTINCT employee_id
                    FROM job_requests jr
                     WHERE (jr.service_date + jr.service_time) < (CAST(:startTime AS timestamp) + CAST(:duration || ' minutes' AS interval))
                     AND
                     ((jr.service_date + jr.service_time) + CAST(jr.planned_duration_in_minutes || ' minutes' AS interval)) > CAST(:startTime AS timestamp)
                     )
                SELECT e.id
                FROM employees e
                JOIN qualified_employees q ON e.id = q.employee_id
                LEFT JOIN busy_employees b ON e.id = b.employee_id
                WHERE b.employee_id IS NULL
            """, nativeQuery = true)

    List<Long> findAvailableEmployees(
            @Param("languageIds") Set<Long> languageIds,
            @Param("totalCount") int totalCount,
            @Param("startTime") LocalDateTime startTime,
            @Param("duration") String duration);


}