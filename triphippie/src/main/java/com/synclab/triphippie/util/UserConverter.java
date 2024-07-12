package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {

    /**
     * Convert the received DTO to a UserProfile entity.
     */
    public UserProfile toEntity(UserDTORequest dto) {
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

    /**
     * Convert a UserProfile to a DTO suited to be a response object.
     */
    public UserDTOResponse toDto(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        UserDTOResponse dto = new UserDTOResponse();
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
