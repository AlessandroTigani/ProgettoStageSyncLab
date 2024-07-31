package com.synclab.triphippie.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JourneyDTO {

    private Long id;

    @NotNull(message = "Trip Id cannot be null")
    private Long tripId;

    private Integer stepNumber;

    @NotNull(message = "Destination cannot be null")
    private DestinationDTO destination;

    private String description;


}
