package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.UserDTO;
import com.synclab.triphippie.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {

    // Convert from UserDTO to User entity
    public UserProfile toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(dto.getId());
        userProfile.setUsername(dto.getUsername());
        userProfile.setPassword(dto.getPassword());
        userProfile.setFirstName(dto.getFirstName());
        userProfile.setLastName(dto.getLastName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setDateOfBirth(dto.getDateOfBirth());
        userProfile.setAbout(dto.getAbout());
        userProfile.setCity(dto.getCity());
        return userProfile;
    }

    // Convert from User entity to UserDTO
    public UserDTO toDto(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(userProfile.getId());
        dto.setUsername(userProfile.getUsername());
        dto.setFirstName(userProfile.getFirstName());
        dto.setLastName(userProfile.getLastName());
        dto.setEmail(userProfile.getEmail());
        dto.setDateOfBirth(userProfile.getDateOfBirth());
        dto.setAbout(userProfile.getAbout());
        dto.setCity(userProfile.getCity());
        return dto;
    }
}
