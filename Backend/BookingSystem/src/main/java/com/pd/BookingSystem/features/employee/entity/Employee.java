package com.pd.BookingSystem.features.employee.entity;

import com.pd.BookingSystem.features.employee.enums.RoleEnum;
import com.pd.BookingSystem.features.employee.enums.StatusEnum;
import com.pd.BookingSystem.features.language.entity.Language;
import com.pd.BookingSystem.features.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@DiscriminatorValue("EMPLOYEE")
@Entity
@Table(name="employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Employee extends User {
    @Column(name="first_name")
    String firstName;

    @Column(name="last_name")
    String lastName;

    @Column(name="rate")
    Double rate;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    RoleEnum role;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    StatusEnum status;

    @ManyToMany
    @JoinTable(
            name = "employee_languages",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"),
            indexes = {
                    @Index(name = "idx_employee_id_language_id", columnList = "employee_id, language_id")
            }
    )
    Set<Language> languages = new HashSet<>();
}
