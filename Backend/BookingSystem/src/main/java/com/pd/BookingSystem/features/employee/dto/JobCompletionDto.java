package com.pd.BookingSystem.features.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class JobCompletionDto {
    Long requestId;
    LocalTime endTime;
}
