package com.pd.BookingSystem.features.employee.dto;

import com.pd.BookingSystem.features.employee.enums.RoleEnum;
import com.pd.BookingSystem.features.employee.enums.StatusEnum;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeeRegistrationDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String rate;
    private RoleEnum role;
    private StatusEnum status;
    private Set<String> languages;
}
