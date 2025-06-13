package com.pd.BookingSystem.features.jobRequests.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.BadRequestException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.client.service.ClientService;
import com.pd.BookingSystem.features.employee.dto.AcceptRequestDto;
import com.pd.BookingSystem.features.employee.dto.JobCompletionDto;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.employee.service.EmployeeService;
import com.pd.BookingSystem.features.jobRequests.dto.JobDataDto;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import com.pd.BookingSystem.features.jobRequests.mappers.JobRequestMapper;
import com.pd.BookingSystem.features.jobRequests.projections.RequestIdStatus;
import com.pd.BookingSystem.features.jobRequests.projections.RequestTimeData;
import com.pd.BookingSystem.features.jobRequests.repository.JobRequestsRepository;
import com.pd.BookingSystem.features.language.entity.Language;
import com.pd.BookingSystem.features.language.service.LanguageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobRequestService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClientService clientService;
    @Autowired
    LanguageService languageService;
    @Autowired
    RequestValidationService requestValidationService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    BusinessRulesService businessRulesService;
    @Autowired
    JobRequestsRepository jobRequestsRepository;
    @Autowired
    JobRequestMapper jobRequestMapper;

    @CacheEvict(value = "jobRequests", allEntries = true)
    public void initializeRequest(JobRequest jobRequest, RequestDto requestDto) {
        requestValidationService.validateRequest(requestDto);
        JobRequest processedRequest = jobRequest.processRequest(jobRequest, requestDto);
        populateRequest(processedRequest, requestDto);

        jobRequestsRepository.save(processedRequest);
        notificationService.notifyAllEmployees("/employee/job", jobRequestMapper.toResponseDto(processedRequest));
    }

    @Cacheable(value = "jobRequests", key = "#id")
    public List<JobRequest> getRequestsForEmployee(Long id) {
        Set<Language> employeeLanguages = employeeService.findEmployeeLanguages(id);
        List<Long> languageIds = employeeLanguages.stream().map(Language::getId).toList();
        if (languageIds.isEmpty())
            throw new BadRequestException("Employee does not speak any languages");

        //Get Employee Jobs
        List<RequestTimeData> jobs = jobRequestsRepository.findEmployeeJobsSchedule(id);
        //Get requests By the languages
        List<JobRequest> requests = jobRequestsRepository.findPendingRequestsByLanguage(languageIds);
        //Filter the requests
        return requestValidationService.filterOverlappingRequests(jobs, requests);

    }

    public Page<ResponseDto> getAllPendingRequests(Pageable pageable) {
        var requests = jobRequestsRepository.findAllByStatusAndEmployeeIsNull(RequestStatusEnum.PENDING, pageable);
        return requests.map(jobRequestMapper::toResponseDto);
    }
    public Page<ResponseDto> getAllInProgressRequests(Pageable pageable) {
        var requests =  jobRequestsRepository.findAllByStatusAndEmployeeIsNotNull(RequestStatusEnum.IN_PROGRESS, pageable);
        return requests.map(jobRequestMapper::toResponseDto);
    }
    public Page<ResponseDto> getAllApprovedRequests(Pageable pageable) {
        var requests =  jobRequestsRepository.findAllByStatusAndEmployeeIsNotNull(RequestStatusEnum.APPROVED, pageable);
        return requests.map(jobRequestMapper::toResponseDto);
    }
    public Page<JobDataDto> getConfirmedRequestsByDate(LocalDate date, Pageable pageable) {
        if (date == null)
            throw new BadRequestException("Date cannot be null");

        Page<JobRequest> confirmedRequests = jobRequestsRepository.findConfirmedJobRequestsByServiceDate(date, pageable);
        return confirmedRequests.map(jobRequestMapper::toJobDataDto);
    }

    @Transactional
    @Retryable(retryFor = CannotAcquireLockException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    @CacheEvict(value = "jobRequests", allEntries = true)
    public void acceptJob(AcceptRequestDto acceptRequestDto) {
        JobRequest jobRequest = jobRequestsRepository.findByIdForUpdate(acceptRequestDto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));

        if (jobRequest.getStatus() != RequestStatusEnum.PENDING)
            throw new ResourceNotFoundException("Job request is not in PENDING status");

        Employee employee = employeeService.findEmployeeById(acceptRequestDto.getEmployeeId());
        requestValidationService.validateBeforeAcceptance(jobRequest, employee);
        notificationService.notifyAllEmployees("/employee/accepted", jobRequest.getId());
        jobRequestsRepository.save(jobRequest);
    }
    @Recover
    private void recover(CannotAcquireLockException e, AcceptRequestDto acceptRequestDto) {
        throw new ResourceNotFoundException("Failed to accept job " + acceptRequestDto.getRequestId() + " request after retries");
    }
    public void approveInProgressJob(Long requestId) {
        requestValidationService.validateByStatus(requestId, RequestStatusEnum.IN_PROGRESS);
        try {
            jobRequestsRepository.updateStatusById(requestId, RequestStatusEnum.APPROVED.toString());
        } catch (CannotAcquireLockException e) {
            throw new ValidationException("Failed to approve job request " + requestId + " after retries");
        }
    }
    @Transactional
    public void assignRequest(Long requestId, Long employeeId){
        JobRequest jobRequest = jobRequestsRepository.findByIdForUpdate(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));

        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) throw new ResourceNotFoundException("Employee not found");

        requestValidationService.validateBeforeAcceptance(jobRequest, employee);
        jobRequestsRepository.save(jobRequest);
    }
    public void startJob(Long requestId) {
        requestValidationService.validateByStatus(requestId, RequestStatusEnum.APPROVED);

        try {
         jobRequestsRepository.updateStartTimeAndStatusById(
                 LocalDateTime.now().toLocalTime(),
                 requestId,
                 RequestStatusEnum.IN_PROGRESS.toString());
        }
        catch (CannotAcquireLockException e) {
            throw new ValidationException("Failed to start job request " + requestId + " after retries");
        }
    }
    public void endJob(JobCompletionDto jobCompletionDto) {
        var requestId = jobCompletionDto.getRequestId();
        var endTime = jobCompletionDto.getEndTime();
        requestValidationService.validateByStatus(requestId, RequestStatusEnum.IN_PROGRESS);

        try {
            JobRequest jobRequest = jobRequestsRepository.findByIdForUpdate(requestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));
            if (jobRequest.getStartTime() == null) throw new ValidationException("Job request has not been started yet");

            businessRulesService.calculatePrice(jobRequest, endTime);
            jobRequest.setStatus(RequestStatusEnum.COMPLETED);
            jobRequestsRepository.save(jobRequest);

        } catch (CannotAcquireLockException e) {
            throw new ValidationException("Failed to end job request " + requestId + " after retries");
        }
    }

    private List<Long> getAvailableEmployeesForRequest(JobRequest jobRequest) {
        Set<Long> languages = Set.of(jobRequest.getSourceLanguage().getId(), jobRequest.getTargetLanguage().getId());
        return getAvailableEmployeesForDate(languages, jobRequest.getServiceDateTime(), jobRequest.getPlannedDurationInMinutes());
    }
    private List<Long> getAvailableEmployeesForDate(
            Set<Long> languages,
            LocalDateTime date,
            Integer duration) {
        return employeeService.findAvailableEmployees(languages, date, duration);
    }

    private void populateRequest(JobRequest processedRequest, RequestDto requestDto) {
        processedRequest.setClient(clientService.getClientById(requestDto.getClientId()));
        processedRequest.setSourceLanguage(languageService.getLanguageById(requestDto.getSourceLanguageId()));
        processedRequest.setTargetLanguage(languageService.getLanguageById(requestDto.getTargetLanguageId()));
    }


}
