package com.pd.BookingSystem.features.employee.controller;

import com.pd.BookingSystem.constants.Constants;
import com.pd.BookingSystem.features.client.dto.ClientDetailsDto;
import com.pd.BookingSystem.features.client.service.ClientService;
import com.pd.BookingSystem.features.employee.dto.EmployeeDetailsDto;
import com.pd.BookingSystem.features.employee.service.EmployeeService;
import com.pd.BookingSystem.features.jobRequests.dto.AnalyticsFilterDto;
import com.pd.BookingSystem.features.jobRequests.dto.AssignRequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.JobDataDto;
import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.projections.AnalyticsLanguageSummary;
import com.pd.BookingSystem.features.jobRequests.service.JobAnalyticsService;
import com.pd.BookingSystem.features.jobRequests.service.JobRequestService;
import com.pd.BookingSystem.features.user.dto.UserDetailsDto;
import com.pd.BookingSystem.features.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constants.API_VERSION + "/admin")
public class AdminController {

    @Autowired
    ClientService clientService;
    @Autowired
    UserService userService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    JobRequestService jobRequestService;
    @Autowired
    JobAnalyticsService analyticsService;

    @GetMapping("/clients/all")
    public ResponseEntity<List<ClientDetailsDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/employees/all")
    public ResponseEntity<List<EmployeeDetailsDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    @GetMapping("/requests/pending")
    public ResponseEntity<Page<ResponseDto>> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate,serviceTime") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = createPageable(page, size, sort, direction);
        Page<ResponseDto> pendingRequestIds = jobRequestService.getAllPendingRequests(pageable);
        return ResponseEntity.ok(pendingRequestIds);
    }

    @GetMapping("/requests/in_progress")
    public ResponseEntity<Page<ResponseDto>> getInProgressRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate,serviceTime") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = createPageable(page, size, sort, direction);
        Page<ResponseDto> pendingRequestIds = jobRequestService.getAllInProgressRequests(pageable);
        return ResponseEntity.ok(pendingRequestIds);
    }

    @GetMapping("/requests/approved")
    public ResponseEntity<Page<ResponseDto>> getApprovedRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate,serviceTime") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = createPageable(page, size, sort, direction);
        Page<ResponseDto> pendingRequestIds = jobRequestService.getAllApprovedRequests(pageable);
        return ResponseEntity.ok(pendingRequestIds);
    }

    @GetMapping("/requests/confirmed/{date}")
    public ResponseEntity<Page<JobDataDto>> getConfirmedRequestsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "service_time") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        // Create Pageable with sorting
        Pageable pageable = createPageable(page, size, sort, direction);
        Page<JobDataDto> confirmedRequests = jobRequestService.getConfirmedRequestsByDate(date, pageable);
        return ResponseEntity.ok(confirmedRequests);
    }

    @PatchMapping("/request/in_progress/approve")
    public ResponseEntity<String> approveRequest(@RequestBody Long requestId) {
        // Logic to approve a request
        jobRequestService.approveInProgressJob(requestId);
        return ResponseEntity.ok("Request approved successfully");
    }

    @PatchMapping("/request/assign")
    public ResponseEntity<String> assignRequest(@RequestBody AssignRequestDto assignRequestDto) {
        // Logic to assign a request
        jobRequestService.assignRequest(assignRequestDto.getRequestId(), assignRequestDto.getEmployeeId());
        return ResponseEntity.ok("Request assigned successfully");
    }

    @PostMapping("/posts")
    @SendTo("/employee")
    public ResponseEntity<String> createPost() {

        simpMessagingTemplate.convertAndSend("/employee", "New post created");
        return ResponseEntity.ok("Post created successfully");
    }

    @GetMapping("/analytics/monthly")
    public ResponseEntity<Page<ResponseDto>> getMonthlyJobRequests(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate,serviceTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        // Create Pageable with sorting
        Pageable pageable = createPageable(page, size, sort, direction);

        AnalyticsFilterDto filters = new AnalyticsFilterDto(year, month);
        filters.setEmployeeId(employeeId);
        filters.setClientId(clientId);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);

        Page<ResponseDto> jobRequests = analyticsService.getJobRequestsByMonth(filters, pageable);
        return ResponseEntity.ok(jobRequests);
    }

    @GetMapping("/analytics/monthly/summary")
    public ResponseEntity<Map<String, Object>> getMonthlyAnalyticsSummary(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        AnalyticsFilterDto filters = new AnalyticsFilterDto(year, month);
        filters.setEmployeeId(employeeId);
        filters.setClientId(clientId);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);

        Map<String, Object> summary = analyticsService.getMonthlyAnalyticsSummary(filters);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/analytics/date-range")
    public ResponseEntity<Page<ResponseDto>> getJobRequestsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate,serviceTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        // Create Pageable with sorting
        Pageable pageable = createPageable(page, size, sort, direction);

        Page<ResponseDto> jobRequests = analyticsService.getJobRequestsByDateRange(
                startDate, endDate, employeeId, clientId, pageable);
        return ResponseEntity.ok(jobRequests);
    }

    @GetMapping("/analytics/date-range/summary")
    public ResponseEntity<Map<String, Object>> getDateRangeAnalyticsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long clientId) {

        Map<String, Object> summary = analyticsService.getDateRangeAnalyticsSummary(
                startDate, endDate, employeeId, clientId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/analytics/languages")
    public ResponseEntity<Map<String, List<AnalyticsLanguageSummary>> > getLanguageAnalytics() {
        Map<String, List<AnalyticsLanguageSummary>>  languageAnalytics = analyticsService.getLanguageSummary();
        return ResponseEntity.ok(languageAnalytics);
    }


    private Pageable createPageable(int page, int size, String sort, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        String[] sortFields = sort.split(",");
        Sort sortBy = Sort.by(sortDirection, sortFields);
        return PageRequest.of(page, size, sortBy);
    }
}
