package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseDto {

    private Long id;
    private String info;
    private Long clientId;
    private LocalDateTime serviceDateTime;
    private Long sourceLanguageId;
    private String sourceLanguageName;
    private Long targetLanguageId;
    private String targetLanguageName;
    private Integer plannedDurationInMinutes;

}
