package com.pd.BookingSystem.features.client.dto;

import com.pd.BookingSystem.features.client.enums.TypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterClientDto {
    String email;
    String password;
    String name;
    String phone;
    String address;
    TypeEnum type;
}
