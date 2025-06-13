package com.pd.BookingSystem.features.client.controller;

import com.pd.BookingSystem.constants.Constants;
import com.pd.BookingSystem.features.jobRequests.dto.*;
import com.pd.BookingSystem.features.client.service.ClientService;
import com.pd.BookingSystem.features.jobRequests.entity.InPersonRequest;
import com.pd.BookingSystem.features.jobRequests.entity.PhoneCallRequest;
import com.pd.BookingSystem.features.jobRequests.entity.TranslationRequest;
import com.pd.BookingSystem.features.jobRequests.entity.VideoCallRequest;
import com.pd.BookingSystem.features.jobRequests.service.JobRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_VERSION + "/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    JobRequestService jobRequestService;


    @PostMapping("/request/in_person")
    public ResponseEntity<String> createInPersonRequest(@RequestBody InPersonRequestDto dto, HttpServletRequest request) {
        dto.setClientId(Long.parseLong((String) request.getAttribute("userId")));
        jobRequestService.initializeRequest(new InPersonRequest(), dto);
        return ResponseEntity.ok("In-person request created successfully");
    }


    @PostMapping("/request/phone_call")
    public ResponseEntity<String> createPhoneCallRequest(@RequestBody PhoneCallRequestDto dto, HttpServletRequest request) {
        dto.setClientId ((Long) request.getAttribute("userId"));
        jobRequestService.initializeRequest(new PhoneCallRequest(), dto);
        return ResponseEntity.ok("In-person request created successfully");
    }


    @PostMapping("/request/translation")
    public ResponseEntity<String> createTranslationRequest(@RequestBody TranslationRequestDto dto, HttpServletRequest request) {
        dto.setClientId ((Long) request.getAttribute("userId"));
        jobRequestService.initializeRequest(new TranslationRequest(), dto);
        return ResponseEntity.ok("In-person request created successfully");
    }


    @PostMapping("/request/video_call")
    public ResponseEntity<String> createVideoCallRequest(@RequestBody VideoCallRequestDto dto, HttpServletRequest request) {
        dto.setClientId ((Long) request.getAttribute("userId"));
        jobRequestService.initializeRequest(new VideoCallRequest(), dto);
        return ResponseEntity.ok("In-person request created successfully");
    }
}
