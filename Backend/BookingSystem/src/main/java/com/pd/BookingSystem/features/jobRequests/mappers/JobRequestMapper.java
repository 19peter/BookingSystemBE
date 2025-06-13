package com.pd.BookingSystem.features.jobRequests.mappers;

import com.pd.BookingSystem.features.jobRequests.dto.JobDataDto;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobRequestMapper {


     @Mapping(target = "id", source = "id")
     @Mapping(target = "clientId", source = "client.id")
     @Mapping(target = "sourceLanguageId", source = "sourceLanguage.id")
     @Mapping(target = "targetLanguageId", source = "targetLanguage.id")
     @Mapping(target = "sourceLanguageName", source = "sourceLanguage.language_name")
     @Mapping(target = "targetLanguageName", source = "targetLanguage.language_name")
     ResponseDto toResponseDto(JobRequest jobRequest);

     @Mapping(target = "clientId", source = "client.id")
     @Mapping(target = "sourceLanguageId", source = "sourceLanguage.id")
     @Mapping(target = "targetLanguageId", source = "targetLanguage.id")
     RequestDto toRequestDto(JobRequest jobRequest);

     List<RequestDto> toRequestDtoList(List<JobRequest> jobRequests);
     List<ResponseDto> toResponseDtoList(List<JobRequest> jobRequests);

     @Mapping(target = "employeeName", expression = "java(jobRequest.getEmployee() != null ? jobRequest.getEmployee().getFirstName() + \" \" + jobRequest.getEmployee().getLastName() : null)")
     @Mapping(target = "clientName", source = "client.name")
     @Mapping(target = "sourceLanguage", source = "sourceLanguage.language_name")
     @Mapping(target = "targetLanguage", source = "targetLanguage.language_name")
     JobDataDto toJobDataDto(JobRequest jobRequest);

}
