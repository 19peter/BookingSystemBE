package com.pd.BookingSystem.features.jobRequests.service;

import com.pd.BookingSystem.features.jobRequests.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    void notifyAllEmployees(String destination, Long jobRequestId) {
        simpMessagingTemplate.convertAndSend(destination, jobRequestId);
    }
    void notifyAllEmployees(String destination, ResponseDto jobRequest) {
        simpMessagingTemplate.convertAndSend(destination, jobRequest);
    }
}
