package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnalyticsFilterDto {
    private Integer year;
    private Integer month;
    private Long employeeId;
    private Long clientId;
    private LocalDate startDate;
    private LocalDate endDate;

    public AnalyticsFilterDto(Integer year, Integer month) {
        this.year = year;
        this.month = month;
    }
}
