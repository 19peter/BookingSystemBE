package com.pd.BookingSystem.features.jobRequests.entity;

import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.TranslationRequestDto;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@DiscriminatorValue("TRANSLATION")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranslationRequest extends  JobRequest{
    String Documents;
    String Notes;

    @Override
    public JobRequest processRequest(JobRequest jobRequest, RequestDto requestDto) {
        TranslationRequest translationRequest = (TranslationRequest) jobRequest;
        TranslationRequestDto translationRequestDto = (TranslationRequestDto) requestDto;

        translationRequest.setDocuments(translationRequestDto.getDocuments());
        translationRequest.setNotes(translationRequestDto.getNotes());
        translationRequest.setInfo(requestDto.getInfo());
        translationRequest.setServiceDate(requestDto.getServiceDateTime().toLocalDate());
        translationRequest.setServiceTime(requestDto.getServiceDateTime().toLocalTime());
        translationRequest.setPlannedDurationInMinutes(requestDto.getPlannedDurationInMinutes());
        translationRequest.setStatus(RequestStatusEnum.PENDING);
        translationRequest.setIsDeleted(false);

        return translationRequest;
    }
}
