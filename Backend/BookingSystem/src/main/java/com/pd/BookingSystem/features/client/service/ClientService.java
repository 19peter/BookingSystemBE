package com.pd.BookingSystem.features.client.service;

import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.client.dto.ClientDetailsDto;
import com.pd.BookingSystem.features.client.dto.RegisterClientDto;
import com.pd.BookingSystem.features.client.entity.Client;
import com.pd.BookingSystem.features.client.mappers.ClientMapper;
import com.pd.BookingSystem.features.client.repository.ClientRepository;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    UserService userService;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    public void registerClient(RegisterClientDto dto) {
        if (userService.doesUserExist(dto.getEmail()))
            throw new ValidationException("User already exists");

        try {
            clientRepository.save(createClientObject(dto));
        } catch (Exception e) {
            throw new ValidationException("Error while saving client");
        }

    }

    public ClientDetailsDto getClientDetailsById(Long id) {
        Optional<Client> client = clientRepository.findById(id);

        if (client.isPresent())  {
            String email = userService.getClientEmail(id);
            ClientDetailsDto clientDetailsDto = clientMapper.toClientDetailsDto(client.get());
            clientDetailsDto.setEmail(email);
            return clientDetailsDto;
        }
        else throw new ResourceNotFoundException("Client not found");

    }

    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) return client.get();
        else throw new ResourceNotFoundException("Client not found");
    }

    public ClientDetailsDto getClientDetailsByEmail(String email) {
        Long id = userService.getClientId(email);
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent())  {
            ClientDetailsDto clientDetailsDto = clientMapper.toClientDetailsDto(client.get());
            clientDetailsDto.setEmail(email);
            return clientDetailsDto;
        }
        else throw new ResourceNotFoundException("Client not found");
    }

    public Boolean doesClientExist(String email) {
        return userService.doesUserExist(email);
    }

    public List<ClientDetailsDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> clientMapper.toClientDetailsDto(client))
                .toList();
    }
    private Client createClientObject(RegisterClientDto dto) {
        Client client = new Client();
        client.setEmail(dto.getEmail());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setType(dto.getType());
        return client;
    }

}
