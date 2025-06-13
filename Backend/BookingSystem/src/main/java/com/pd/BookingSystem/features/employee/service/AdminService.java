package com.pd.BookingSystem.features.employee.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.client.dto.ClientDetailsDto;
import com.pd.BookingSystem.features.client.service.ClientService;
import com.pd.BookingSystem.features.employee.dto.EmployeeDetailsDto;
import com.pd.BookingSystem.features.employee.dto.EmployeeRegistrationDto;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.employee.mapper.EmployeeMapper;
import com.pd.BookingSystem.features.employee.repository.EmployeeRepository;
import com.pd.BookingSystem.features.user.dto.UserDetailsDto;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserService userService;
    @Autowired
    ClientService clientService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    EmployeeMapper employeeMapper;


    public void registerEmployee(EmployeeRegistrationDto dto) {

        if (userService.doesUserExist(dto.getEmail()))
            throw new ValidationException("User already exists");

        try {
            employeeRepository.save(createEmployeeObject(dto));
        } catch (Exception e) {
            throw new ValidationException("Error while saving employee");
        }
    }


    public List<EmployeeDetailsDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    public List<UserDetailsDto> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<ClientDetailsDto> getAllClients() {
        return clientService.getAllClients();
    }


    private Employee createEmployeeObject(EmployeeRegistrationDto dto) {
        Employee employee = new Employee();
        employee.setEmail(dto.getEmail());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setRate(Double.parseDouble(dto.getRate()));
        employee.setRole(dto.getRole());
        employee.setStatus(dto.getStatus());
        return employee;
    }

}
