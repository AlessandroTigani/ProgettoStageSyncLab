package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.PreferenceTagDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import com.synclab.triphippie.service.PreferenceTagService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenceTagConverter {

    PreferenceTagRepository preferenceTagRepository;

    public PreferenceTagConverter(PreferenceTagRepository preferenceTagRepository) {
        this.preferenceTagRepository = preferenceTagRepository;
    }

    public PreferenceTag toEntity(PreferenceTagDTO dto) {
        if (dto == null) {
            return null;
        }
        Optional<PreferenceTag> optionalPreferenceTag = preferenceTagRepository.findByName(dto.getName());
        if (optionalPreferenceTag.isPresent()) {
            return optionalPreferenceTag.get();
        }
        else {
            throw new EntryNotFoundException("Preference not found");
        }
    }


    public PreferenceTagDTO toDto(PreferenceTag entity) {
        if (entity == null) {
            return null;
        }
        PreferenceTagDTO dto = new PreferenceTagDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
