package com.pd.BookingSystem.config;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.features.client.entity.Client;
import com.pd.BookingSystem.features.client.repository.ClientRepository;
import com.pd.BookingSystem.features.employee.entity.Employee;
import com.pd.BookingSystem.features.employee.repository.EmployeeRepository;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userCheck = userRepository.findByEmail(username);

        if (userCheck.isEmpty()) throw new ResourceNotFoundException("User not found");

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        User user = userCheck.get();

        if (user instanceof Employee) {
            Employee employee = employeeRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
            authorities.add(new SimpleGrantedAuthority(employee.getRole().toString()));
        } else {
            Client client = clientRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
            authorities.add(new SimpleGrantedAuthority("CLIENT"));
            authorities.add(new SimpleGrantedAuthority(client.getType().toString()));
        }


        return new User(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );

    }


}
