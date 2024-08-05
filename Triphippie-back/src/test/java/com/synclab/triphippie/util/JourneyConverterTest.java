package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.dto.JourneyDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Destination;
import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JourneyConverterTest {

    @InjectMocks
    private JourneyConverter journeyConverter;

    @Mock
    private TripService tripService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity_Success() {
        JourneyDTO dto = new JourneyDTO();
        dto.setId(1L);
        dto.setTripId(10L);
        dto.setStepNumber(2);
        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setName("Paris");
        destinationDTO.setLatitude(48.8566);
        destinationDTO.setLongitude(2.3522);
        dto.setDestination(destinationDTO);
        dto.setDescription("Trip to Paris");
        Trip trip = new Trip();
        trip.setId(10L);
        Journey expectedJourney = new Journey();
        expectedJourney.setId(1L);
        expectedJourney.setTrip(trip);
        expectedJourney.setStepNumber(2);
        expectedJourney.setDestination(new Destination("Paris", 48.8566, 2.3522));
        expectedJourney.setDescription("Trip to Paris");

        when(tripService.findById(10L)).thenReturn(Optional.of(trip));

        Journey result = journeyConverter.toEntity(dto);

        assertNotNull(result);
        assertEquals(expectedJourney.getId(), result.getId());
        assertEquals(expectedJourney.getTrip().getId(), result.getTrip().getId());
        assertEquals(expectedJourney.getStepNumber(), result.getStepNumber());
        assertEquals(expectedJourney.getDestination().getName(), result.getDestination().getName());
        assertEquals(expectedJourney.getDescription(), result.getDescription());
    }


    @Test
    void testToEntity_TripNotFound() {
        JourneyDTO dto = new JourneyDTO();
        dto.setId(1L);
        dto.setTripId(10L);

        when(tripService.findById(10L)).thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () ->
                journeyConverter.toEntity(dto)
        );
        assertEquals("Trip not found.", exception.getMessage());
    }


    @Test
    void testToDto() {
        // Arrange
        Journey journey = new Journey();
        journey.setId(1L);
        journey.setStepNumber(2);
        journey.setDestination(new Destination("Paris", 48.8566, 2.3522));
        journey.setDescription("Trip to Paris");
        Trip trip = new Trip();
        trip.setId(10L);
        journey.setTrip(trip);
        JourneyDTO expectedDTO = new JourneyDTO();
        expectedDTO.setId(1L);
        expectedDTO.setTripId(10L);
        expectedDTO.setStepNumber(2);
        expectedDTO.setDestination(new DestinationDTO("Paris", 48.8566, 2.3522));
        expectedDTO.setDescription("Trip to Paris");

        JourneyDTO result = journeyConverter.toDto(journey);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTripId(), result.getTripId());
        assertEquals(expectedDTO.getStepNumber(), result.getStepNumber());
        assertEquals(expectedDTO.getDestination().getName(), result.getDestination().getName());
        assertEquals(expectedDTO.getDescription(), result.getDescription());
    }


    @Test
    void testToDto_NullInput() {
        JourneyDTO dto = journeyConverter.toDto(null);
        assertNull(dto);
    }
}
