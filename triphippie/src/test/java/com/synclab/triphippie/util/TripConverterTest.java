package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.dto.TripDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.*;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import com.synclab.triphippie.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TripConverterTest {

    @InjectMocks
    private TripConverter tripConverter;

    @Mock
    private UserService userService;

    @Mock
    private PreferenceTagRepository preferenceTagRepository;

    @Mock
    private PreferenceVehicleRepository preferenceVehicleRepository;

    public TripConverterTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity_Success() {
        // Arrange
        TripDTO dto = new TripDTO();
        dto.setId(1L);
        dto.setUserId(2L);
        dto.setStartDate(LocalDateTime.of(2024, 1, 1, 1, 1));
        dto.setEndDate(LocalDateTime.of(2024, 1, 10, 1, 1));
        dto.setVehicle("Car");
        dto.setType("Adventure");
        dto.setStartDestination(new DestinationDTO("Paris", 48.8566, 2.3522));
        dto.setEndDestination(new DestinationDTO("Berlin", 52.52, 13.405));
        dto.setDescription("Trip description");

        UserProfile userProfile = new UserProfile();
        userProfile.setId(2L);

        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("Car");

        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("Adventure");

        when(userService.findById(2L)).thenReturn(Optional.of(userProfile));
        when(preferenceVehicleRepository.findByName("Car")).thenReturn(Optional.of(preferenceVehicle));
        when(preferenceTagRepository.findByName("Adventure")).thenReturn(Optional.of(preferenceTag));

        // Act
        Trip trip = tripConverter.toEntity(dto);

        // Assert
        assertNotNull(trip);
        assertEquals(dto.getId(), trip.getId());
        assertEquals(userProfile, trip.getUserProfile());
        assertEquals(dto.getStartDate(), trip.getStartDate());
        assertEquals(dto.getEndDate(), trip.getEndDate());
        assertEquals(preferenceVehicle, trip.getVehicle());
        assertEquals(preferenceTag, trip.getType());
        assertEquals(dto.getStartDestination().getName(), trip.getStartDestination().getName());
        assertEquals(dto.getStartDestination().getLatitude(), trip.getStartDestination().getLatitude());
        assertEquals(dto.getStartDestination().getLongitude(), trip.getStartDestination().getLongitude());
        assertEquals(dto.getEndDestination().getName(), trip.getEndDestination().getName());
        assertEquals(dto.getEndDestination().getLatitude(), trip.getEndDestination().getLatitude());
        assertEquals(dto.getEndDestination().getLongitude(), trip.getEndDestination().getLongitude());
        assertEquals(dto.getDescription(), trip.getDescription());
    }

    @Test
    void testToEntity_UserNotFound() {
        // Arrange
        TripDTO dto = new TripDTO();
        dto.setUserId(2L);
        when(userService.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntryNotFoundException.class, () -> tripConverter.toEntity(dto));
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void testToEntity_VehicleNotFound() {
        // Arrange
        TripDTO dto = new TripDTO();
        dto.setVehicle("Car");
        dto.setUserId(2L);
        when(userService.findById(2L)).thenReturn(Optional.of(new UserProfile()));
        when(preferenceVehicleRepository.findByName("Car")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntryNotFoundException.class, () -> tripConverter.toEntity(dto));
        assertEquals("Preference vehicle not found.", exception.getMessage());
    }

    @Test
    void testToEntity_TagNotFound() {
        // Arrange
        TripDTO dto = new TripDTO();
        dto.setType("Adventure");
        dto.setUserId(2L);
        dto.setVehicle("Car");
        when(userService.findById(2L)).thenReturn(Optional.of(new UserProfile()));
        when(preferenceVehicleRepository.findByName("Car")).thenReturn(Optional.of(new PreferenceVehicle()));
        when(preferenceTagRepository.findByName("Adventure")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntryNotFoundException.class, () -> tripConverter.toEntity(dto));
        assertEquals("Preference not found.", exception.getMessage());
    }

    @Test
    void testToDto() {
        // Arrange
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setStartDate(LocalDateTime.of(2024, 1, 1, 1, 1));
        trip.setEndDate(LocalDateTime.of(2024, 1, 10, 1, 1));
        trip.setDescription("Trip description");

        UserProfile userProfile = new UserProfile();
        userProfile.setId(2L);
        trip.setUserProfile(userProfile);

        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("Car");
        trip.setVehicle(preferenceVehicle);

        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("Adventure");
        trip.setType(preferenceTag);

        Destination startDestination = new Destination("Paris", 48.8566, 2.3522);
        Destination endDestination = new Destination("Berlin", 52.52, 13.405);
        trip.setStartDestination(startDestination);
        trip.setEndDestination(endDestination);

        // Act
        TripDTO dto = tripConverter.toDto(trip);

        // Assert
        assertNotNull(dto);
        assertEquals(trip.getId(), dto.getId());
        assertEquals(trip.getUserProfile().getId(), dto.getUserId());
        assertEquals(trip.getStartDate(), dto.getStartDate());
        assertEquals(trip.getEndDate(), dto.getEndDate());
        assertEquals(trip.getVehicle().getName(), dto.getVehicle());
        assertEquals(trip.getType().getName(), dto.getType());
        assertEquals(trip.getStartDestination().getName(), dto.getStartDestination().getName());
        assertEquals(trip.getStartDestination().getLatitude(), dto.getStartDestination().getLatitude());
        assertEquals(trip.getStartDestination().getLongitude(), dto.getStartDestination().getLongitude());
        assertEquals(trip.getEndDestination().getName(), dto.getEndDestination().getName());
        assertEquals(trip.getEndDestination().getLatitude(), dto.getEndDestination().getLatitude());
        assertEquals(trip.getEndDestination().getLongitude(), dto.getEndDestination().getLongitude());
        assertEquals(trip.getDescription(), dto.getDescription());
    }

    @Test
    void testToDto_NullInput() {
        // Act
        TripDTO dto = tripConverter.toDto(null);

        // Assert
        assertNull(dto);
    }
}