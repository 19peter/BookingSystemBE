package com.pd.BookingSystem.features.employee.enums;

public enum RoleEnum {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE");
    private final String role;
    RoleEnum(String role) {
        this.role = role;
    }
}
