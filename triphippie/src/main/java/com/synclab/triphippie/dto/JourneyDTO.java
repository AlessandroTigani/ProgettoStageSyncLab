package com.synclab.triphippie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JourneyDTO {

    private Long id;

    @NotNull(message = "Trip Id cannot be null")
    private Long tripId;

    @NotNull(message = "Step number cannot be null")
    private Integer stepNumber;

    @NotNull(message = "Destination cannot be null")
    private DestinationDTO destination;


    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDestination(DestinationDTO destination) {
        this.destination = destination;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull(message = "Trip Id cannot be null") Long getTripId() {
        return tripId;
    }

    public void setTripId(@NotNull(message = "Trip Id cannot be null") Long tripId) {
        this.tripId = tripId;
    }

    public @NotNull(message = "Step number cannot be null") Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(@NotNull(message = "Step number cannot be null") Integer stepNumber) {
        this.stepNumber = stepNumber;
    }
}
