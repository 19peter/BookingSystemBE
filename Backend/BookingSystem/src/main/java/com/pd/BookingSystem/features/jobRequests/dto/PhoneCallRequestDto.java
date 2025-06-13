package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneCallRequestDto extends RequestDto {
    private String phone_number;
}
