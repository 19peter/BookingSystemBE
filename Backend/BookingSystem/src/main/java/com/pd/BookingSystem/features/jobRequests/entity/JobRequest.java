package com.pd.BookingSystem.features.jobRequests.entity;

import com.pd.BookingSystem.features.jobRequests.dto.InPersonRequestDto;
import com.pd.BookingSystem.features.client.entity.Client;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import com.pd.BookingSystem.features.language.entity.Language;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
@Table(name="job_requests",
        indexes = {
                @Index(name = "idx_date", columnList = "service_date, service_time, id"),
                @Index(name = "idx_client_date", columnList = "client_id, service_date, service_time, planned_duration_in_minutes, status"),
                @Index(name = "idx_employee_date", columnList = "employee_id, service_date, service_time, planned_duration_in_minutes, status"),
                @Index(name = "idx_target_source_language_id", columnList = "status, target_language_id, source_language_id, employee_id")
        })
public abstract class JobRequest {
    @Id
    @Column(unique = true, nullable = false , name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_req_seq_gen")
    @SequenceGenerator(
            name = "job_req_seq_gen",
            sequenceName = "job_req_seq",
            initialValue = 1000,
            allocationSize = 1
    )
    Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "source_language_id", referencedColumnName = "id")
    Language sourceLanguage;

    @ManyToOne
    @JoinColumn(name = "target_language_id", referencedColumnName = "id")
    Language targetLanguage;

    String info;

    @Column(name = "service_date", nullable = false)
    LocalDate serviceDate;

    @Column(name = "service_time", nullable = false)
    LocalTime serviceTime;

    @Column(name = "planned_duration_in_minutes", nullable = false)
    Integer plannedDurationInMinutes;

    @Column(name = "actual_duration_in_minutes")
    Integer actualDurationInMinutes;

    @Column(name = "start_time")
    LocalTime startTime;

    @Enumerated(EnumType.STRING)
    RequestStatusEnum status;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "is_deleted")
    Boolean isDeleted;

    Double price;

    String feedback;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Transient
    public LocalTime getEndTime() {
        return serviceTime.plusMinutes(plannedDurationInMinutes);
    }

    @Transient
    public LocalDateTime getServiceDateTime() {
        return LocalDateTime.of(serviceDate, serviceTime);
    }
    public abstract JobRequest processRequest(JobRequest jobRequest, RequestDto requestDto);
}
