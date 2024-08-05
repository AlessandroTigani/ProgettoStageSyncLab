package com.synclab.triphippie.util;

import com.synclab.triphippie.dto.DestinationDTO;
import com.synclab.triphippie.model.Destination;

public class DestinationConverter {

    public Destination toEntity(DestinationDTO dto){
        if(dto == null)
            return null;
        Destination destination = new Destination();
        destination.setName(dto.getName());
        destination.setLatitude(dto.getLatitude());
        destination.setLongitude(dto.getLongitude());
        return destination;
    }

    public DestinationDTO toDTO(Destination entity){
        if(entity == null)
            return null;
        DestinationDTO dto = new DestinationDTO();
        dto.setName(entity.getName());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        return dto;
    }
}
