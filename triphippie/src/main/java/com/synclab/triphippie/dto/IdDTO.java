package com.synclab.triphippie.dto;

import jakarta.validation.constraints.NotNull;

public class IdDTO {

    @NotNull
    private Long tripId;

    public IdDTO() {}


    public @NotNull Long getTripId() {
        return tripId;
    }

    public void setTripId(@NotNull Long tripId) {
        this.tripId = tripId;
    }
}
