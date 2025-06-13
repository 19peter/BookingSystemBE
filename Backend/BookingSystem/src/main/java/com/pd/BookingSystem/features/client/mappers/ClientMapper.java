package com.pd.BookingSystem.features.client.mappers;

import com.pd.BookingSystem.features.client.dto.ClientDetailsDto;
import com.pd.BookingSystem.features.client.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
     ClientDetailsDto toClientDetailsDto(Client client);
}
