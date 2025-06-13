package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDataDto {
    private Long id;
    private String serviceType;
    private String clientName;
    private String employeeName;
    private String sourceLanguage;
    private String targetLanguage;
    private String serviceTime;
    private Integer plannedDurationInMinutes;
}
