package com.pd.BookingSystem.features.jobRequests.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.configurations.service.ConfigurationService;
import com.pd.BookingSystem.features.jobRequests.entity.JobRequest;
import com.pd.BookingSystem.features.language.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class BusinessRulesService {
    @Autowired
    ConfigurationService configurationService;

    void calculatePrice(JobRequest jobRequest, LocalTime endTime) {
        int duration = endTime.getMinute() - jobRequest.getStartTime().getMinute();

        if (duration < 0)
            throw new ValidationException("End time cannot be before start time");

        double hourlyRate = configurationService.getDefaultHourlyRate();
        int hourBufferMinutes = configurationService.getHourBufferMinutes();

        if (duration <= 60 + hourBufferMinutes) {
            jobRequest.setPrice(hourlyRate);
            return;
        }

        int remainingMinutes = duration - 60;

        int adjustedMinutes = remainingMinutes - hourBufferMinutes;

        if (adjustedMinutes <= 0) {
            jobRequest.setPrice(hourlyRate);
            return;
        }

        // Round up to the next 30-minute block
        int blocksOf30Min = (int) Math.ceil(adjustedMinutes / 30.0);
        // Each block is charged at half the hourly rate
        double extraCost = blocksOf30Min * (hourlyRate * 0.5);

        jobRequest.setPrice(hourlyRate + extraCost);
    }



}
