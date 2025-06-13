package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InPersonRequestDto extends RequestDto {
    private String address;
    // Add any other fields or methods as needed
}
