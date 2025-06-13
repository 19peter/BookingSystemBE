package com.pd.BookingSystem.features.employee.dto;

import com.pd.BookingSystem.features.employee.enums.RoleEnum;
import com.pd.BookingSystem.features.employee.enums.StatusEnum;
import com.pd.BookingSystem.features.language.entity.Language;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeeDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double rate;
    private RoleEnum role;
    private StatusEnum status;
    private Set<Language> languages;


}
