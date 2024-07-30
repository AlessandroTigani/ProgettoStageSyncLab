package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.PreferenceVehicleDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceVehicle;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreferenceVehicleConverterTest {

    @InjectMocks
    private PreferenceVehicleConverter preferenceVehicleConverter;

    @Mock
    private PreferenceVehicleRepository preferenceVehicleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity_Success() {
        // Arrange
        PreferenceVehicleDTO dto = new PreferenceVehicleDTO();
        dto.setName("Car");
        dto.setDescription("A nice car");

        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("Car");
        preferenceVehicle.setDescription("A nice car");

        when(preferenceVehicleRepository.findByName("Car")).thenReturn(Optional.of(preferenceVehicle));

        // Act
        PreferenceVehicle result = preferenceVehicleConverter.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(preferenceVehicle.getName(), result.getName());
        assertEquals(preferenceVehicle.getDescription(), result.getDescription());
    }

    @Test
    void testToEntity_NotFound() {
        // Arrange
        PreferenceVehicleDTO dto = new PreferenceVehicleDTO();
        dto.setName("Bike");

        when(preferenceVehicleRepository.findByName("Bike")).thenReturn(Optional.empty());

        // Act & Assert
        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () ->
                preferenceVehicleConverter.toEntity(dto)
        );
        assertEquals("Preference not found", exception.getMessage());
    }

    @Test
    void testToDto() {
        // Arrange
        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("Car");
        preferenceVehicle.setDescription("A nice car");

        // Act
        PreferenceVehicleDTO dto = preferenceVehicleConverter.toDto(preferenceVehicle);

        // Assert
        assertNotNull(dto);
        assertEquals(preferenceVehicle.getName(), dto.getName());
        assertEquals(preferenceVehicle.getDescription(), dto.getDescription());
    }

    @Test
    void testToDto_NullInput() {
        // Act
        PreferenceVehicleDTO dto = preferenceVehicleConverter.toDto(null);

        // Assert
        assertNull(dto);
    }
}
