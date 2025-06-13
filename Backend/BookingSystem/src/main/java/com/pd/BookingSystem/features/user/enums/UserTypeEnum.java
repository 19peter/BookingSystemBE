package com.pd.BookingSystem.features.user.enums;

public enum UserTypeEnum {
    EMPLOYEE("EMPLOYEE"),
    CLIENT("CLIENT");
    private final String value;
    UserTypeEnum(String value) {
        this.value = value;
    }
}
