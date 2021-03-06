package com.example.portalapi.entity.dto.mapper;

import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOMapper {
    private UserEntityToDTOMapper() {
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsernameDTO(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getJoinDate(),
                user.getRole().toString(),
                user.isLocked(),
                user.isActive(),
                user.getAwards());
    }
}
