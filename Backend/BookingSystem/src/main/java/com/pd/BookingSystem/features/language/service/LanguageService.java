package com.pd.BookingSystem.features.language.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.features.language.entity.Language;
import com.pd.BookingSystem.features.language.repository.InterpreterLanguagePriceRepository;
import com.pd.BookingSystem.features.language.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LanguageService {

    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    InterpreterLanguagePriceRepository interpreterLanguagePriceRepository;
    public Language getLanguageById(Long id) {
        return languageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Language not found"));
    }

    public Set<Language> findLanguagesByEmployeeId(Long employeeId) {
        return languageRepository.findLanguagesByEmployeeId(employeeId);
    }

    public Double getPricePerHourByLanguageId(Long languageId) {
        var languagePrice = interpreterLanguagePriceRepository.findByLanguageId(languageId)
                .orElseThrow(() -> new ResourceNotFoundException("Price per hour not found for language ID: " + languageId));

        return languagePrice.getPricePerHour();
    }
}
