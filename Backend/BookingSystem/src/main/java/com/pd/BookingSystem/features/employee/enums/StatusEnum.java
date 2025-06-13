package com.pd.BookingSystem.features.employee.enums;

public enum StatusEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BUSY("BUSY"),
    DELETED("DELETED"),
    PENDING("PENDING");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
