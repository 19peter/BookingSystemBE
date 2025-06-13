package com.pd.BookingSystem.features.jobRequests.entity;

import com.pd.BookingSystem.exceptions.CustomExceptions.BadRequestException;
import com.pd.BookingSystem.features.jobRequests.dto.InPersonRequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@DiscriminatorValue("IN_PERSON")
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class InPersonRequest extends  JobRequest {
    @Nonnull
    String address;

    @Override
    public JobRequest processRequest(JobRequest jobRequest, RequestDto requestDto) {
        try {
            InPersonRequest inPersonRequest = (InPersonRequest) jobRequest;
            InPersonRequestDto inPersonRequestDto = (InPersonRequestDto) requestDto;

            inPersonRequest.setAddress(inPersonRequestDto.getAddress());
            inPersonRequest.setInfo(inPersonRequestDto.getInfo());
            inPersonRequest.setServiceDate(inPersonRequestDto.getServiceDateTime().toLocalDate());
            inPersonRequest.setServiceTime(inPersonRequestDto.getServiceDateTime().toLocalTime());
            inPersonRequest.setPlannedDurationInMinutes(inPersonRequestDto.getPlannedDurationInMinutes());
            inPersonRequest.setStatus(RequestStatusEnum.PENDING);
            inPersonRequest.setIsDeleted(false);
            return inPersonRequest;

        } catch (Exception e) {
            throw new BadRequestException("Error processing request");
        }

    }
}