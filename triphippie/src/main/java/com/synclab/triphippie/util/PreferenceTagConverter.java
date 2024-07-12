package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.PreferenceTagDTO;
import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class PreferenceTagConverter {

    /**
     * Convert the received DTO to a PreferenceTag entity.
     */
    public PreferenceTag toEntity(PreferenceTagDTO dto) {
        if (dto == null) {
            return null;
        }
        PreferenceTag entity = new PreferenceTag();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }


    /**
     * Convert a UserProfile to a DTO suited to be a response object.
     */
    public PreferenceTagDTO toDto(PreferenceTag entity) {
        if (entity == null) {
            return null;
        }
        PreferenceTagDTO dto = new PreferenceTagDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
