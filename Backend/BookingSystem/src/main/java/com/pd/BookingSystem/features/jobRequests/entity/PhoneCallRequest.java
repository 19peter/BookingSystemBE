package com.pd.BookingSystem.features.jobRequests.entity;

import com.pd.BookingSystem.features.jobRequests.dto.PhoneCallRequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import jakarta.annotation.Nonnull;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.parameters.P;

@Entity
@DiscriminatorValue("PHONE_CALL")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneCallRequest extends  JobRequest {
    @Nonnull
    String phone_number;

    @Override
    public JobRequest processRequest(JobRequest jobRequest, RequestDto requestDto) {
        PhoneCallRequest phoneCallRequest = (PhoneCallRequest) jobRequest;
        PhoneCallRequestDto phoneCallRequestDto = (PhoneCallRequestDto) requestDto;

        phoneCallRequest.setPhone_number(phoneCallRequestDto.getPhone_number());
        phoneCallRequest.setInfo(phoneCallRequestDto.getInfo());
        phoneCallRequest.setServiceDate(phoneCallRequestDto.getServiceDateTime().toLocalDate());
        phoneCallRequest.setServiceTime(requestDto.getServiceDateTime().toLocalTime());
        phoneCallRequest.setPlannedDurationInMinutes(phoneCallRequestDto.getPlannedDurationInMinutes());
        phoneCallRequest.setStatus(RequestStatusEnum.PENDING);
        phoneCallRequest.setIsDeleted(false);

        return phoneCallRequest;
    }
}
