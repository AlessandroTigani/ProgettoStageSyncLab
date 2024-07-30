package com.synclab.triphippie.dto;

import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.model.PreferenceVehicle;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TripDTO {

    private Long id;

    private Long userId;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "Date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Vehicle cannot be null")
    private String vehicle;

    @NotNull(message = "Trip type cannot be null")
    private String type;

    @NotNull(message = "Start destination cannot be null")
    private DestinationDTO startDestination;

    @NotNull(message = "End destination cannot be null")
    private DestinationDTO endDestination;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public @NotNull(message = "Start date cannot be null") @Future(message = "Date must be in the future") LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "Start date cannot be null") @Future(message = "Date must be in the future") LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "End date cannot be null") @Future(message = "Date must be in the future") LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "End date cannot be null") @Future(message = "Date must be in the future") LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public @NotNull(message = "Vehicle cannot be null") String getVehicle() {
        return vehicle;
    }

    public void setVehicle(@NotNull(message = "Vehicle cannot be null") String vehicle) {
        this.vehicle = vehicle;
    }

    public @NotNull(message = "Trip type cannot be null") String getType() {
        return type;
    }

    public void setType(@NotNull(message = "Trip type cannot be null") String type) {
        this.type = type;
    }

    public @NotNull(message = "Start destination cannot be null") DestinationDTO getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(@NotNull(message = "Start destination cannot be null") DestinationDTO startDestination) {
        this.startDestination = startDestination;
    }

    public @NotNull(message = "End destination cannot be null") DestinationDTO getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(@NotNull(message = "End destination cannot be null") DestinationDTO endDestination) {
        this.endDestination = endDestination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
