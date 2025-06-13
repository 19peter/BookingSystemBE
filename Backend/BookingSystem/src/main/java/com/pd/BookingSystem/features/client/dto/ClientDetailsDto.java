package com.pd.BookingSystem.features.client.dto;

import com.pd.BookingSystem.features.client.enums.TypeEnum;
import lombok.Data;

@Data
public class ClientDetailsDto {
    private String name;
    private String email;
    private String phone;
    private String address;
    private TypeEnum type;
}
