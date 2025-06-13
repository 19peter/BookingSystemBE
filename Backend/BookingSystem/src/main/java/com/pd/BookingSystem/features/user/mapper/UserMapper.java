package com.pd.BookingSystem.features.user.mapper;

import com.pd.BookingSystem.features.user.dto.UserDetailsDto;
import com.pd.BookingSystem.features.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDetailsDto userToUserDto(User user);

    User userDtoToUser(UserDetailsDto userDto);

    UserDetailsDto userToUserDetailsDto(User user);

    User userDetailsDtoToUser(UserDetailsDto userDetailsDto);
}
