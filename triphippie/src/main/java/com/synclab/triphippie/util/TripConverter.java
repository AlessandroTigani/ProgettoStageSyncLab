package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.dto.TripDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.*;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import com.synclab.triphippie.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripConverter {

    private final UserService userService;
    private final PreferenceTagRepository preferenceTagRepository;
    private final PreferenceVehicleRepository preferenceVehicleRepository;

    public TripConverter(UserService userService, PreferenceTagRepository preferenceTagRepository, PreferenceVehicleRepository preferenceVehicleRepository) {
        this.userService = userService;
        this.preferenceTagRepository = preferenceTagRepository;
        this.preferenceVehicleRepository = preferenceVehicleRepository;
    }

    public Trip toEntity(TripDTO dto) {
        if (dto == null)
            return null;
        Trip trip = new Trip();
        trip.setId(dto.getId());
        Optional<UserProfile> userProfileOptional = userService.findById(dto.getUserId());
        if(userProfileOptional.isEmpty()) {
            throw new EntryNotFoundException("User not found.");
        }
        trip.setUserProfile(userProfileOptional.get());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());

        Optional<PreferenceVehicle> optionalVehicle = preferenceVehicleRepository.findByName(dto.getVehicle());
        if (optionalVehicle.isEmpty()) {
            throw new EntryNotFoundException("Preference vehicle not found.");
        }
        PreferenceVehicle tempVehicle = optionalVehicle.get();
        trip.setVehicle(tempVehicle);

        Optional<PreferenceTag> optionalTag = preferenceTagRepository.findByName(dto.getType());
        if (optionalTag.isEmpty()) {
            throw new EntryNotFoundException("Preference not found.");
        }
        PreferenceTag tempTag = optionalTag.get();
        trip.setType(tempTag);

        Destination tempStartDestination = new Destination(
                dto.getStartDestination().getName(),
                dto.getStartDestination().getLatitude(),
                dto.getStartDestination().getLongitude()
        );
        trip.setStartDestination(tempStartDestination);
        Destination tempEndDestination = new Destination(
                dto.getEndDestination().getName(),
                dto.getEndDestination().getLatitude(),
                dto.getEndDestination().getLongitude()
        );
        trip.setEndDestination(tempEndDestination);
        trip.setDescription(dto.getDescription());
        return trip;
    }


    public TripDTO toDto(Trip trip) {
        if (trip == null)
            return null;
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setUserId(trip.getUserProfile().getId());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setVehicle(trip.getVehicle().getName());
        dto.setType(trip.getType().getName());
        DestinationDTO tempStartDestination = new DestinationDTO(
                trip.getStartDestination().getName(),
                trip.getStartDestination().getLatitude(),
                trip.getStartDestination().getLongitude()
        );
        dto.setStartDestination(tempStartDestination);
        DestinationDTO tempEndDestination = new DestinationDTO(
                trip.getEndDestination().getName(),
                trip.getEndDestination().getLatitude(),
                trip.getEndDestination().getLongitude()
        );
        dto.setEndDestination(tempEndDestination);
        dto.setDescription(trip.getDescription());
        return dto;
    }
}
