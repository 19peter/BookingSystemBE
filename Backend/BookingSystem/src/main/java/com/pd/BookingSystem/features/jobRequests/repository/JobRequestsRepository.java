package com.pd.BookingSystem.features.jobRequests.repository;

import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import com.pd.BookingSystem.features.jobRequests.projections.AnalyticsBillingData;
import com.pd.BookingSystem.features.jobRequests.projections.AnalyticsLanguageSummary;
import com.pd.BookingSystem.features.jobRequests.projections.RequestIdStatus;
import com.pd.BookingSystem.features.jobRequests.projections.RequestTimeData;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories

public interface JobRequestsRepository extends JpaRepository<JobRequest, Long> {

    @Query(value = "SELECT r.service_date, r.service_time FROM job_requests r WHERE r.client_id = :clientId " +
            "AND NOT (r.service_date + r.service_time >= :endTime OR " +
            "(r.service_date + r.service_time + (r.planned_duration_in_minutes * INTERVAL '1 minute')) <= :startTime)" +
            "AND NOT r.status = 'REJECTED'",
            nativeQuery = true)
    List<Object[]> findOverlappingRequests(
            @Param("clientId") Long clientId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query(value = """
            SELECT jr.service_date, jr.service_time, jr.planned_duration_in_minutes
            FROM job_requests jr
            WHERE jr.status IN ('PENDING', 'IN_PROGRESS')
            AND jr.employee_id = :employeeId
            """, nativeQuery = true)
    List<RequestTimeData> findEmployeeJobsSchedule(
            @Param("employeeId") Long employeeId
    );

    @Query(value = """
            SELECT jr.*
            FROM job_requests jr
            WHERE jr.status = 'PENDING'
            AND jr.source_language_id IN :languageIds
            AND jr.target_language_id IN :languageIds
            """, nativeQuery = true)
    List<JobRequest> findPendingRequestsByLanguage(
            @Param("languageIds") List<Long> languageIds
    );

    @Query(value = """
    SELECT jr.service_date, jr.service_time, jr.planned_duration_in_minutes
    FROM job_requests jr
    WHERE jr.employee_id = :employeeId
    AND jr.service_date BETWEEN :startOfDay AND :endOfDay
    AND NOT jr.status = 'REJECTED'
    """, nativeQuery = true)
    List<RequestTimeData> findByEmployeeScheduleBetween(
            Long employeeId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    @Transactional
    @Query(value = """
    SELECT id
    FROM   job_requests
    WHERE  service_date >= :startDate
      AND  service_date  < :nextMonthDate
""", nativeQuery = true)
    List<Long> findIdsByMonth(@Param("startDate") LocalDate startDate,
                                    @Param("nextMonthDate") LocalDate nextMonthDate);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT jr FROM JobRequest jr WHERE jr.id = :id")
    Optional<JobRequest> findByIdForUpdate(Long id);

    Page<JobRequest> findAllByStatusAndEmployeeIsNull(RequestStatusEnum status, Pageable pageable);

    Page<JobRequest> findAllByStatusAndEmployeeIsNotNull(RequestStatusEnum status, Pageable pageable);


    Optional<RequestIdStatus> findRequestIdAndStatusById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE job_requests SET status = :newStatus WHERE id = :id", nativeQuery = true)
    void updateStatusById(@Param("id") Long id, @Param("newStatus") String newStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE job_requests SET start_time = :startTime, status = :newStatus WHERE id = :id", nativeQuery = true)
    void updateStartTimeAndStatusById(@Param("startTime") LocalTime startTime,
                                      @Param("id") Long id,
                                      @Param("newStatus") String newStatus);




    @Query("SELECT jr FROM JobRequest jr WHERE " +
            "jr.serviceDate >= :monthStart AND jr.serviceDate < :monthEnd " +
            "AND (:employeeId IS NULL OR jr.employee.id = :employeeId) " +
            "AND (:clientId IS NULL OR jr.client.id = :clientId) " +
            "AND (:startDate IS NULL OR jr.serviceDate >= :startDate) " +
            "AND (:endDate IS NULL OR jr.serviceDate <= :endDate) " +
            "AND (jr.isDeleted IS NULL OR jr.isDeleted = false)")
    Page<JobRequest> findJobRequestsByDateRangeWithFilters(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("employeeId") Long employeeId,
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );


    @Query("SELECT COUNT(jr) FROM JobRequest jr WHERE " +
            "jr.serviceDate >= :monthStart AND jr.serviceDate < :monthEnd " +
            "AND (:employeeId IS NULL OR jr.employee.id = :employeeId) " +
            "AND (:clientId IS NULL OR jr.client.id = :clientId) " +
            "AND (:startDate IS NULL OR jr.serviceDate >= :startDate) " +
            "AND (:endDate IS NULL OR jr.serviceDate <= :endDate) " +
            "AND (jr.isDeleted IS NULL OR jr.isDeleted = false)")
    Long countJobRequestsByDateRangeWithFilters(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("employeeId") Long employeeId,
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
            "COUNT(jr) AS count, " +
            "SUM(jr.plannedDurationInMinutes) AS plannedDurationInMinutes, " +
            "SUM(CASE WHEN jr.actualDurationInMinutes IS NOT NULL THEN jr.actualDurationInMinutes ELSE 0 END) AS actualDurationInMinutes, " +
            "SUM(CASE WHEN jr.price IS NOT NULL THEN jr.price ELSE 0 END) AS totalPrice " +
            "FROM JobRequest jr WHERE " +
            "jr.serviceDate >= :monthStart AND jr.serviceDate < :monthEnd " +
            "AND (:employeeId IS NULL OR jr.employee.id = :employeeId) " +
            "AND (:clientId IS NULL OR jr.client.id = :clientId) " +
            "AND (:startDate IS NULL OR jr.serviceDate >= :startDate) " +
            "AND (:endDate IS NULL OR jr.serviceDate <= :endDate) " +
            "AND (jr.isDeleted IS NULL OR jr.isDeleted = false)")
    AnalyticsBillingData getAnalyticsSummary(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("employeeId") Long employeeId,
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT jr.status, COUNT(jr) FROM JobRequest jr WHERE " +
            "jr.serviceDate >= :monthStart AND jr.serviceDate < :monthEnd " +
            "AND (:employeeId IS NULL OR jr.employee.id = :employeeId) " +
            "AND (:clientId IS NULL OR jr.client.id = :clientId) " +
            "AND (:startDate IS NULL OR jr.serviceDate >= :startDate) " +
            "AND (:endDate IS NULL OR jr.serviceDate <= :endDate) " +
            "AND (jr.isDeleted IS NULL OR jr.isDeleted = false) " +
            "GROUP BY jr.status")
    List<Object[]> getStatusCounts(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("employeeId") Long employeeId,
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT 'SOURCE' as type, l.language_name as languageName , COUNT(jr) as count " +
            "FROM JobRequest jr " +
            "JOIN Language l ON jr.sourceLanguage.id = l.id " +
            "WHERE jr.status != 'CANCELLED' " +
            "GROUP BY l.id, l.language_name " +
            "UNION ALL " +
            "SELECT 'TARGET' as type, l.language_name as languageName, COUNT(jr) as count " +
            "FROM JobRequest jr " +
            "JOIN Language l ON jr.targetLanguage.id = l.id " +
            "WHERE jr.status != 'CANCELLED' " +
            "GROUP BY l.id, l.language_name " +
            "ORDER BY type, COUNT(jr) DESC")
    List<AnalyticsLanguageSummary> getLanguageSummary();

    @Query(value = """
    SELECT *
    FROM job_requests
    WHERE service_date = :date
      AND employee_id IS NOT NULL
      AND status = 'CONFIRMED'
      AND is_deleted = false
    ORDER BY service_time DESC
    """, nativeQuery = true)
    Page<JobRequest> findConfirmedJobRequestsByServiceDate(
            @Param("date") LocalDate date,
            Pageable pageable);
}

