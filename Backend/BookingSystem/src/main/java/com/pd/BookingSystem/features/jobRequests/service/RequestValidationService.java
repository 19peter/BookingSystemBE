package com.pd.BookingSystem.features.jobRequests.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.BadRequestException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import com.pd.BookingSystem.features.jobRequests.projections.RequestIdStatus;
import com.pd.BookingSystem.features.jobRequests.projections.RequestTimeData;
import com.pd.BookingSystem.features.jobRequests.repository.JobRequestsRepository;
import com.pd.BookingSystem.features.language.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestValidationService {

    @Autowired
    private JobRequestsRepository jobRequestsRepository;
    void validateRequest(RequestDto requestDto) {
        if (requestDto.getClientId() == null || requestDto.getSourceLanguageId() == null || requestDto.getTargetLanguageId() == null || requestDto.getServiceDateTime() == null)
            throw new BadRequestException("Request parameters cannot be null");

        LocalDateTime endTime = requestDto.getServiceDateTime().plusMinutes(requestDto.getPlannedDurationInMinutes());
        List<Object[]> overlappingRequests = jobRequestsRepository.findOverlappingRequests(requestDto.getClientId(), requestDto.getServiceDateTime(), endTime);

        if (!overlappingRequests.isEmpty())
            throw new ValidationException("Request overlaps with existing requests");
    }
    void validateBeforeAcceptance(JobRequest jobRequest, Employee employee) {
        var languages = employee.getLanguages().stream().map(Language::getId).toList();
        if (!languages.contains(jobRequest.getSourceLanguage().getId()) || !languages.contains(jobRequest.getTargetLanguage().getId()))
            throw new ValidationException("Employee does not speak the required languages");

        List<RequestTimeData> requestsData = jobRequestsRepository.findByEmployeeScheduleBetween(
                employee.getId(),
                jobRequest.getServiceDate().atStartOfDay(),
                jobRequest.getServiceDate().plusDays(1).atStartOfDay()
        );

        if (requestsData.stream().anyMatch(r -> isOverlapping(r, jobRequest)))
            throw new ValidationException("Employee has overlapping requests");

        jobRequest.setStatus(RequestStatusEnum.IN_PROGRESS);
        jobRequest.setEmployee(employee);
    }
    void validateByStatus(Long id, RequestStatusEnum status) {
        RequestIdStatus requestIdStatus = jobRequestsRepository.findRequestIdAndStatusById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));

        if (!requestIdStatus.getStatus().equals(status.toString()))
            throw new ValidationException("Job request is not in IN_PROGRESS status");
    }
    List<JobRequest> filterOverlappingRequests (List<RequestTimeData> jobs, List<JobRequest> requests) {
        return requests.stream()
                .filter(request -> jobs.stream().noneMatch(job -> isOverlapping(job, request)))
                .toList();
    }
    boolean isOverlapping(RequestTimeData job, JobRequest request) {
        if (!job.getServiceDate().equals(request.getServiceDate())) return false;

        LocalDateTime job1Start = LocalDateTime.of(job.getServiceDate(), job.getServiceTime());
        LocalDateTime job1End = job1Start.plusMinutes(job.getPlannedDurationInMinutes());

        LocalDateTime job2Start = LocalDateTime.of(request.getServiceDate(), request.getServiceTime());
        LocalDateTime job2End = job2Start.plusMinutes(request.getPlannedDurationInMinutes());

        // Overlap condition
        return !(job1End.isBefore(job2Start) || job2End.isBefore(job1Start));

    }

}
