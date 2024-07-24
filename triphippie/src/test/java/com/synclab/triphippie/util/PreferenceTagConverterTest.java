package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.PreferenceTagDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreferenceTagConverterTest {

    @InjectMocks
    private PreferenceTagConverter preferenceTagConverter;

    @Mock
    private PreferenceTagRepository preferenceTagRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity_Success() {
        // Arrange
        PreferenceTagDTO dto = new PreferenceTagDTO();
        dto.setName("Adventure");
        dto.setDescription("Exciting adventures");

        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("Adventure");
        preferenceTag.setDescription("Exciting adventures");

        when(preferenceTagRepository.findByName("Adventure")).thenReturn(Optional.of(preferenceTag));

        // Act
        PreferenceTag result = preferenceTagConverter.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(preferenceTag.getName(), result.getName());
        assertEquals(preferenceTag.getDescription(), result.getDescription());
    }

    @Test
    void testToEntity_NotFound() {
        // Arrange
        PreferenceTagDTO dto = new PreferenceTagDTO();
        dto.setName("Hiking");

        when(preferenceTagRepository.findByName("Hiking")).thenReturn(Optional.empty());

        // Act & Assert
        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () ->
                preferenceTagConverter.toEntity(dto)
        );
        assertEquals("Preference not found", exception.getMessage());
    }

    @Test
    void testToDto() {
        // Arrange
        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("Adventure");
        preferenceTag.setDescription("Exciting adventures");

        // Act
        PreferenceTagDTO dto = preferenceTagConverter.toDto(preferenceTag);

        // Assert
        assertNotNull(dto);
        assertEquals(preferenceTag.getName(), dto.getName());
        assertEquals(preferenceTag.getDescription(), dto.getDescription());
    }

    @Test
    void testToDto_NullInput() {
        // Act
        PreferenceTagDTO dto = preferenceTagConverter.toDto(null);

        // Assert
        assertNull(dto);
    }
}
