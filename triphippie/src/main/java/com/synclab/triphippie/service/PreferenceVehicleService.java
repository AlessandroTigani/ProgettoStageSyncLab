package com.synclab.triphippie.service;

import com.synclab.triphippie.model.PreferenceVehicle;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceVehicleService {
    private final PreferenceVehicleRepository preferenceVehicleRepository;

    public PreferenceVehicleService(PreferenceVehicleRepository preferenceVehicleRepository) {
        this.preferenceVehicleRepository = preferenceVehicleRepository;
    }

    public String[] findAll() {
        List<PreferenceVehicle> preferenceVehicles = this.preferenceVehicleRepository.findAll();
        String[] tags = new String[preferenceVehicles.size()];
        for (int i = 0; i < preferenceVehicles.size(); i++) {
            tags[i] = preferenceVehicles.get(i).getName();
        }
        return tags;
    }
}
