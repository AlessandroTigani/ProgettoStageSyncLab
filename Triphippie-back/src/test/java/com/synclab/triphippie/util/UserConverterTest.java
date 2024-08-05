package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.UserProfile;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private final UserConverter userConverter = new UserConverter();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void testToEntity() throws ParseException {
        // Arrange
        UserDTORequest dto = new UserDTORequest();
        dto.setId(1L);
        dto.setUsername("john_doe");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        Date dateOfBirth = sdf.parse("1990-01-01");
        dto.setDateOfBirth(dateOfBirth);
        dto.setAbout("About John Doe");
        dto.setCity("New York");

        // Act
        UserProfile userProfile = userConverter.toEntity(dto);

        // Assert
        assertEquals(dto.getId(), userProfile.getId());
        assertEquals(dto.getUsername(), userProfile.getUsername());
        assertEquals(dto.getPassword(), userProfile.getPassword());
        assertEquals(dto.getFirstName(), userProfile.getFirstName());
        assertEquals(dto.getLastName(), userProfile.getLastName());
        assertEquals(dto.getEmail(), userProfile.getEmail());
        assertEquals(dto.getDateOfBirth(), userProfile.getDateOfBirth());
        assertEquals(dto.getAbout(), userProfile.getAbout());
        assertEquals(dto.getCity(), userProfile.getCity());
    }

    @Test
    void testToEntity_NullInput() {
        // Act
        UserProfile userProfile = userConverter.toEntity(null);

        // Assert
        assertNull(userProfile);
    }



    @Test
    void testToDto() throws ParseException {
        // Arrange
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setUsername("john_doe");
        userProfile.setPassword("password123"); // Note: Password should not be included in DTO
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        userProfile.setEmail("john.doe@example.com");
        Date dateOfBirth = sdf.parse("1990-01-01");
        userProfile.setDateOfBirth(dateOfBirth);
        userProfile.setAbout("About John Doe");
        userProfile.setCity("New York");

        // Act
        UserDTOResponse dto = userConverter.toDto(userProfile);

        // Assert
        assertEquals(userProfile.getId(), dto.getId());
        assertEquals(userProfile.getUsername(), dto.getUsername());
        assertEquals(userProfile.getFirstName(), dto.getFirstName());
        assertEquals(userProfile.getLastName(), dto.getLastName());
        assertEquals(userProfile.getEmail(), dto.getEmail());
        assertEquals(userProfile.getDateOfBirth(), dto.getDateOfBirth());
        assertEquals(userProfile.getAbout(), dto.getAbout());
        assertEquals(userProfile.getCity(), dto.getCity());
    }

    @Test
    void testToDto_NullInput() {
        // Act
        UserDTOResponse dto = userConverter.toDto(null);

        // Assert
        assertNull(dto);
    }
}