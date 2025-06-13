package com.pd.BookingSystem.features.user.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.features.user.mapper.UserMapper;
import com.pd.BookingSystem.features.user.dto.UserDetailsDto;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public String getClientEmail(Long id) {
        Optional<String> email = userRepository.findById(id).map(User::getEmail);
        if (email.isPresent()) return email.get();
        else throw new ResourceNotFoundException("User not found");
    }

    public Long getClientId(String email) {
        Optional<Long> id = userRepository.findByEmail(email).map(User::getId);
        if (id.isPresent()) return id.get();
        else throw new ResourceNotFoundException("User not found");
    }

    public Boolean doesUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public List<UserDetailsDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> userMapper.userToUserDetailsDto(user))
                .toList();
    }
}
