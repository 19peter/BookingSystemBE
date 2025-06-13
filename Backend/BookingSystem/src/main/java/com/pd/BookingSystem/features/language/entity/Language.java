package com.pd.BookingSystem.features.language.entity;

import com.pd.BookingSystem.features.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="languages",
        indexes = {
                @Index(name = "idx_language_id_name", columnList = "id, language_name")
        })
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false , name="id")
    Long id;

    String language_name;

    @ManyToMany(mappedBy = "languages")
    Set<Employee> employees = new HashSet<>();
}
