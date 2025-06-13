package com.pd.BookingSystem.features.employee.controller;

import com.pd.BookingSystem.constants.Constants;
import com.pd.BookingSystem.features.employee.dto.AcceptRequestDto;
import com.pd.BookingSystem.features.employee.dto.JobCompletionDto;
import com.pd.BookingSystem.features.jobRequests.dto.RequestDto;
import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.jobRequests.mappers.JobRequestMapper;
import com.pd.BookingSystem.features.jobRequests.service.JobRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.API_VERSION + "/employee")
public class EmployeeController {

    @Autowired
    JobRequestService jobRequestService;
    @Autowired
    JobRequestMapper jobRequestMapper;

    @GetMapping("/requests")
    public ResponseEntity<List<ResponseDto>> getAllJobRequests(HttpServletRequest request) {
        Long id = Long.parseLong((String) request.getAttribute("userId"));
        var jobRequests = jobRequestService.getRequestsForEmployee(id);
        List<ResponseDto> requestDtoList = jobRequestMapper.toResponseDtoList(jobRequests);
        return ResponseEntity.ok(requestDtoList);
    }

    @PostMapping("/request/accept")
    public ResponseEntity<HttpStatus> acceptRequest(@RequestBody AcceptRequestDto acceptRequestDto,
                                                    HttpServletRequest request) {
        Long id = Long.parseLong((String) request.getAttribute("userId"));
        acceptRequestDto.setEmployeeId(id);
        jobRequestService.acceptJob(acceptRequestDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping("/job/start")
    public ResponseEntity<HttpStatus> startJob(@RequestBody Long id) {
        jobRequestService.startJob(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/job/complete")
    public ResponseEntity<HttpStatus> completeJob(@RequestBody JobCompletionDto jobCompletionDto) {
        jobRequestService.endJob(jobCompletionDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
