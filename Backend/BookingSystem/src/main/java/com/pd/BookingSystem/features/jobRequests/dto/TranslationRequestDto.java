package com.pd.BookingSystem.features.jobRequests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequestDto extends RequestDto {
    private String documents;
    private String notes;


}
