package com.pd.BookingSystem.features.jobRequests.dto;

import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private String info;
    private Long clientId;
    private LocalDateTime serviceDateTime;
    private Long sourceLanguageId;
    private Long targetLanguageId;
    private Integer plannedDurationInMinutes;
}
