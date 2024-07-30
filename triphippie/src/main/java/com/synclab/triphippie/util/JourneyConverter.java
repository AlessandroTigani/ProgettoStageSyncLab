package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.dto.JourneyDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Destination;
import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.service.TripService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class JourneyConverter {

    private final TripService tripService;

    public JourneyConverter(TripService tripService) {
        this.tripService = tripService;
    }

    public Journey toEntity(JourneyDTO dto) {
        if (dto == null) {
            return null;
        }
        Journey journey = new Journey();
        journey.setId(dto.getId());
        Optional<Trip> foundTrip = tripService.findById(dto.getTripId());
        if(foundTrip.isEmpty())
            throw new EntryNotFoundException("Trip not found.");
        journey.setTrip(foundTrip.get());
        journey.setStepNumber(dto.getStepNumber());
        Destination tempDestination = new Destination(
                dto.getDestination().getName(),
                dto.getDestination().getLatitude(),
                dto.getDestination().getLongitude()
        );
        journey.setDestination(tempDestination);
        journey.setDescription(dto.getDescription());
        return journey;
    }


    public JourneyDTO toDto(Journey journey) {
        if (journey == null) {
            return null;
        }
        JourneyDTO dto = new JourneyDTO();
        dto.setId(journey.getId());
        dto.setTripId(journey.getTrip().getId());
        DestinationDTO tempDestination = new DestinationDTO(
                journey.getDestination().getName(),
                journey.getDestination().getLatitude(),
                journey.getDestination().getLongitude()
        );
        dto.setStepNumber(journey.getStepNumber());
        dto.setDestination(tempDestination);
        dto.setDescription(journey.getDescription());
        return dto;
    }
}
