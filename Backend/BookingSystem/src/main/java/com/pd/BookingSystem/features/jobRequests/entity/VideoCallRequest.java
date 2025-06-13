package com.pd.BookingSystem.features.jobRequests.entity;

import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.VideoCallRequestDto;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@DiscriminatorValue("VIDEO_CALL")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoCallRequest extends  JobRequest{
    Boolean is_active;

    @Override
    public JobRequest processRequest(JobRequest jobRequest, RequestDto requestDto) {
        VideoCallRequest videoCallRequest = (VideoCallRequest) jobRequest;
        VideoCallRequestDto videoCallRequestDto = (VideoCallRequestDto) requestDto;

        videoCallRequest.setIs_active(videoCallRequestDto.getActive());
        videoCallRequest.setInfo(requestDto.getInfo());
        videoCallRequest.setServiceTime(requestDto.getServiceDateTime().toLocalTime());
        videoCallRequest.setServiceDate(requestDto.getServiceDateTime().toLocalDate());
        videoCallRequest.setPlannedDurationInMinutes(requestDto.getPlannedDurationInMinutes());
        videoCallRequest.setStatus(RequestStatusEnum.PENDING);
        videoCallRequest.setIsDeleted(false);
        return videoCallRequest;
    }
}
