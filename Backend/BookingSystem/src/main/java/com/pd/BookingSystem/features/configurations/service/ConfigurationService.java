package com.pd.BookingSystem.features.configurations.service;

import com.pd.BookingSystem.features.configurations.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
    @Autowired
    ConfigurationRepository configurationRepository;
    public double getDefaultHourlyRate() {
        var config = configurationRepository.findOptionalByKey("hourlyRate")
                .orElseThrow(() -> new RuntimeException("Configuration for hourly rate not found"));

        return Double.parseDouble(config.getValue());
    }

    public double getClientCancellationFee() {
        var config = configurationRepository.findOptionalByKey("clientCancellationFee")
                .orElseThrow(() -> new RuntimeException("Configuration for client cancellation fee not found"));

        return Double.parseDouble(config.getValue());
    }

    public double getServiceProviderFee() {
        var config = configurationRepository.findOptionalByKey("serviceProviderFee")
                .orElseThrow(() -> new RuntimeException("Configuration for service provider fee not found"));

        return Double.parseDouble(config.getValue());
    }

    public int getHourBufferMinutes() {
        var config = configurationRepository.findOptionalByKey("hourBufferMinutes")
                .orElseThrow(() -> new RuntimeException("Configuration for hour buffer minutes not found"));

        return Integer.parseInt(config.getValue());
    }
}
