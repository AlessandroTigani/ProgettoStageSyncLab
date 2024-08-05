package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.PreferenceVehicleDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.model.PreferenceVehicle;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenceVehicleConverter {

    PreferenceVehicleRepository preferenceVehicleRepository;

    public PreferenceVehicleConverter(PreferenceVehicleRepository preferenceVehicleRepository) {
        this.preferenceVehicleRepository = preferenceVehicleRepository;
    }

    public PreferenceVehicle toEntity(PreferenceVehicleDTO dto) {
        if (dto == null) {
            return null;
        }
        Optional<PreferenceVehicle> optionalPreferenceVehicle = preferenceVehicleRepository.findByName(dto.getName());
        if (optionalPreferenceVehicle.isPresent()) {
            return optionalPreferenceVehicle.get();
        }
        else {
            throw new EntryNotFoundException("Preference not found");
        }
    }


    public PreferenceVehicleDTO toDto(PreferenceVehicle entity) {
        if (entity == null) {
            return null;
        }
        PreferenceVehicleDTO dto = new PreferenceVehicleDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
