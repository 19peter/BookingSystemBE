package com.pd.BookingSystem.features.configurations.enums;

import lombok.Getter;

@Getter
public enum ConfigTypeEnum {
    STRING("String"),
    INTEGER("Integer"),
    BOOLEAN("Boolean"),
    DOUBLE("Double");

    private final String type;

    ConfigTypeEnum(String type) {
        this.type = type;
    }

}
