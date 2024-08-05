package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.model.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinationConverterTest {

    private final DestinationConverter destinationConverter = new DestinationConverter();


    @Test
    void testToEntity() {
        DestinationDTO dto = new DestinationDTO();
        dto.setName("Eiffel Tower");
        dto.setLatitude(48.8584);
        dto.setLongitude(2.2945);

        Destination result = destinationConverter.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getLatitude(), result.getLatitude());
        assertEquals(dto.getLongitude(), result.getLongitude());
    }


    @Test
    void testToEntity_NullInput() {
        Destination result = destinationConverter.toEntity(null);
        assertNull(result);
    }


    @Test
    void testToDTO() {
        Destination entity = new Destination();
        entity.setName("Eiffel Tower");
        entity.setLatitude(48.8584);
        entity.setLongitude(2.2945);

        DestinationDTO result = destinationConverter.toDTO(entity);

        assertNotNull(result);
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getLatitude(), result.getLatitude());
        assertEquals(entity.getLongitude(), result.getLongitude());
    }


    @Test
    void testToDTO_NullInput() {
        DestinationDTO result = destinationConverter.toDTO(null);
        assertNull(result);
    }
}
