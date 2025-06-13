package com.pd.BookingSystem.features.jobRequests.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.jobRequests.dto.AnalyticsFilterDto;
import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.enums.RequestStatusEnum;
import com.pd.BookingSystem.features.jobRequests.mappers.JobRequestMapper;
import com.pd.BookingSystem.features.jobRequests.projections.AnalyticsBillingData;
import com.pd.BookingSystem.features.jobRequests.projections.AnalyticsLanguageSummary;
import com.pd.BookingSystem.features.jobRequests.repository.JobRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobAnalyticsService {

    @Autowired
    private JobRequestsRepository analyticsRepository;
    @Autowired
    private JobRequestMapper mapper;


    //Done
    public Page<ResponseDto> getJobRequestsByMonth(AnalyticsFilterDto filters, Pageable pageable) {
        validateFilters(filters);

        // Calculate month start and end dates
        LocalDate monthStart = LocalDate.of(filters.getYear(), filters.getMonth(), 1);
        LocalDate monthEnd = monthStart.plusMonths(1);

        Page<JobRequest> jobRequests =  analyticsRepository.findJobRequestsByDateRangeWithFilters(
                monthStart,
                monthEnd,
                filters.getEmployeeId(),
                filters.getClientId(),
                filters.getStartDate(),
                filters.getEndDate(),
                pageable
        );

        return jobRequests.map(mapper::toResponseDto);
    }

    public Page<ResponseDto> getJobRequestsByDateRange(LocalDate startDate, LocalDate endDate,
                                                      Long employeeId, Long clientId, Pageable pageable) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date must be provided");
        }

        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        // For date range queries, we use the second method with null month parameters
        LocalDate rangeEnd = endDate.plusDays(1); // Include the end date

        var jobRequests = analyticsRepository.findJobRequestsByDateRangeWithFilters(
                startDate,
                rangeEnd,
                employeeId,
                clientId,
                null, // Additional start date filter not needed
                null, // Additional end date filter not needed
                pageable
        );

        return jobRequests.map(mapper::toResponseDto);
    }
    //Done
    public Map<String, Object> getMonthlyAnalyticsSummary(AnalyticsFilterDto filters) {
        validateFilters(filters);

        LocalDate monthStart = LocalDate.of(filters.getYear(), filters.getMonth(), 1);
        LocalDate monthEnd = monthStart.plusMonths(1);

        // Get aggregate data in a single query
        AnalyticsBillingData summaryData = analyticsRepository.getAnalyticsSummary(
                monthStart, monthEnd,
                filters.getEmployeeId(),
                filters.getClientId(),
                filters.getStartDate(),
                filters.getEndDate()
        );

        // Get status counts
        List<Object[]> statusCounts = analyticsRepository.getStatusCounts(
                monthStart, monthEnd,
                filters.getEmployeeId(),
                filters.getClientId(),
                filters.getStartDate(),
                filters.getEndDate()
        );

        Map<String, Object> summary = createSummaryMap(summaryData);
        Map<String, Long> statusCountMap = createStatusCountMap(statusCounts);
        summary.putAll(statusCountMap);

        return summary;
    }

    public Map<String, Object> getDateRangeAnalyticsSummary(LocalDate startDate, LocalDate endDate,
                                                            Long employeeId, Long clientId) {
        if (startDate == null || endDate == null) throw new ValidationException("Start date and end date must be provided");


        LocalDate rangeEnd = endDate.plusDays(1);

        // Get aggregate data in a single query
        AnalyticsBillingData summaryData = analyticsRepository.getAnalyticsSummary(
                startDate, rangeEnd, employeeId, clientId, null, null
        );

        // Get status counts
        List<Object[]> statusCounts = analyticsRepository.getStatusCounts(
                startDate, rangeEnd, employeeId, clientId, null, null
        );

        Map<String, Object> summary = createSummaryMap(summaryData);
        Map<String, Long> statusCountMap = createStatusCountMap(statusCounts);
        summary.putAll(statusCountMap);

        return summary;
    }

    public Map<String, List<AnalyticsLanguageSummary>> getLanguageSummary() {
        List<AnalyticsLanguageSummary> languageSummary = analyticsRepository.getLanguageSummary();

        Map<String, List<AnalyticsLanguageSummary>> groupedLanguages = languageSummary.stream()
                .filter(row -> "SOURCE".equals(row.getType()) || "TARGET".equals(row.getType()))
                .collect(Collectors.groupingBy(row -> {
                    if ("SOURCE".equals(row.getType())) {
                        return "sourceLanguages";
                    } else if ("TARGET".equals(row.getType())) {
                        return "targetLanguages";
                    } else {
                        return "unknown"; // optional: handle unexpected types
                    }
                }));

        // Ensure both keys are always present
        groupedLanguages.putIfAbsent("sourceLanguages", new ArrayList<>());
        groupedLanguages.putIfAbsent("targetLanguages", new ArrayList<>());

        return groupedLanguages;
    }

    private void validateFilters(AnalyticsFilterDto filters) {
        if (filters.getYear() == null || filters.getMonth() == null) {
            throw new ValidationException("Year and month must be provided");
        }

        if (filters.getMonth() < 1 || filters.getMonth() > 12) {
            throw new ValidationException("Month must be between 1 and 12");
        }

        if (filters.getYear() < 1900 || filters.getYear() > LocalDate.now().getYear() + 10) {
            throw new ValidationException("Invalid year provided");
        }
    }

    private Map<String, Object> createSummaryMap(AnalyticsBillingData data) {
        Map<String, Object> summary = new HashMap<>();
        if (data != null) {
            summary.put("totalRequests", data.getCount() != null ? data.getCount() : 0L);
            summary.put("totalPlannedHours", data.getPlannedDurationInMinutes() != null ? data.getPlannedDurationInMinutes() / 60.0 : 0.0);
            summary.put("totalActualHours", data.getActualDurationInMinutes() != null ? data.getActualDurationInMinutes() / 60.0 : 0.0);
            summary.put("totalRevenue", data.getTotalPrice() != null ? data.getTotalPrice() : 0.0);
        }
        return summary;
    }

    private Map<String, Long> createStatusCountMap(List<Object[]> statusCounts) {
        Map<String, Long> statusCountMap = new HashMap<>();
        for (Object[] statusCount : statusCounts) {
            RequestStatusEnum status = (RequestStatusEnum) statusCount[0];
            Long count = (Long) statusCount[1];
            statusCountMap.put(status.name().toLowerCase() + "Requests", count);
        }
        return statusCountMap;
    }
}
