package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.TripDTO;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class TripConverter {
    /**
     * Convert the received DTO to Entity.
     */
    public Trip toEntity(TripDTO dto) {
        if (dto == null) {
            return null;
        }
        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setDescription(dto.getDescription());
        trip.setEndDateTime(dto.getEndDateTime());
        trip.setStartDateTime(dto.getStartDateTime());
        trip.setFirstLocationLatitude(dto.getFirstLocationLatitude());
        trip.setFirstLocationLongitude(dto.getFirstLocationLongitude());
        trip.setLastLocationLatitude(dto.getLastLocationLatitude());
        trip.setLastLocationLongitude(dto.getLastLocationLongitude());
        trip.setFirstLocationName(dto.getFirstLocationName());
        trip.setLastLocationName(dto.getLastLocationName());
        trip.setPreferences(dto.getPreferences());
        trip.setUserId(dto.getUserId());
        return trip;
    }

    /**
     * Convert an Entity to a DTO suited to be a response object.
     */
    public TripDTO toDto(Trip trip) {
        if (trip == null) {
            return null;
        }
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setDescription(trip.getDescription());
        dto.setEndDateTime(trip.getEndDateTime());
        dto.setStartDateTime(trip.getStartDateTime());
        dto.setFirstLocationLatitude(trip.getFirstLocationLatitude());
        dto.setFirstLocationLongitude(trip.getFirstLocationLongitude());
        dto.setLastLocationLatitude(trip.getLastLocationLatitude());
        dto.setLastLocationLongitude(trip.getLastLocationLongitude());
        dto.setFirstLocationName(trip.getFirstLocationName());
        dto.setLastLocationName(trip.getLastLocationName());
        dto.setPreferences(trip.getPreferences());
        dto.setUserId(trip.getUserId());
        return dto;
    }
}
