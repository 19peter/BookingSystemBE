package com.pd.BookingSystem.features.employee.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.employee.dto.EmployeeDetailsDto;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.employee.mapper.EmployeeMapper;
import com.pd.BookingSystem.features.employee.repository.EmployeeRepository;
import com.pd.BookingSystem.features.language.entity.Language;
import com.pd.BookingSystem.features.language.service.LanguageService;
import com.pd.BookingSystem.features.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    LanguageService languageService;

    public Employee findEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) return employee.get();

        else throw new ResourceNotFoundException("Employee not found");

    }

    public List<Long> findEmployeesByLanguage(List<Language> languages) {
        if (languages == null || languages.isEmpty())
            throw new ValidationException("Language list must not be null or empty");


        Set<Long> languageIds = languages.stream()
                .map(Language::getId)
                .collect(Collectors.toSet());

        return employeeRepository.findEmployeesWithAllLanguages(languageIds, (long) languageIds.size());
    }

    public List<EmployeeDetailsDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .toList();
    }

    public List<Long> findAvailableEmployees(
            Set<Long> languageIds,
            LocalDateTime startTime,
            Integer duration) {
        return employeeRepository.findAvailableEmployees(languageIds, languageIds.size(), startTime, duration.toString());
    }

    public Set<Language> findEmployeeLanguages(Long employeeId) {
        return languageService.findLanguagesByEmployeeId(employeeId);
    }

}
