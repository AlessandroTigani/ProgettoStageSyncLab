package com.synclab.triphippie.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TripDTO {

    private Integer id;

    @NotNull(message = "User Id cannot be null")
    private Integer userId;

    private String description;

    @Future(message = "Date must be in the future")
    private LocalDateTime endDateTime;

    private String preferences;

    @Future(message = "Date must be in the future")
    private LocalDateTime startDateTime;


    @NotNull(message = "Location name cannot be null")
    @NotBlank(message = "Location name is required")
    @Size(min=2, max = 50, message = "The length of the location name must be between 2 and 50 characters.")
    private String firstLocationName;

    private double lastLocationLatitude;

    private double firstLocationLatitude;

    private String lastLocationName;

    private double lastLocationLongitude;

    private double firstLocationLongitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull(message = "User Id cannot be null") Integer getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User Id cannot be null") Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @Future(message = "Date must be in the future") LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(@Future(message = "Date must be in the future") LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public @Future(message = "Date must be in the future") LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(@Future(message = "Date must be in the future") LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getLastLocationName() {
        return lastLocationName;
    }

    public void setLastLocationName(String lastLocationName) {
        this.lastLocationName = lastLocationName;
    }

    public @NotNull(message = "Location name cannot be null") @NotBlank(message = "Location name is required") @Size(min = 2, max = 50, message = "The length of the location name must be between 2 and 50 characters.") String getFirstLocationName() {
        return firstLocationName;
    }

    public void setFirstLocationName(@NotNull(message = "Location name cannot be null") @NotBlank(message = "Location name is required") @Size(min = 2, max = 50, message = "The length of the location name must be between 2 and 50 characters.") String firstLocationName) {
        this.firstLocationName = firstLocationName;
    }

    public double getLastLocationLatitude() {
        return lastLocationLatitude;
    }

    public void setLastLocationLatitude(double lastLocationLatitude) {
        this.lastLocationLatitude = lastLocationLatitude;
    }

    public double getFirstLocationLatitude() {
        return firstLocationLatitude;
    }

    public void setFirstLocationLatitude(double firstLocationLatitude) {
        this.firstLocationLatitude = firstLocationLatitude;
    }

    public double getLastLocationLongitude() {
        return lastLocationLongitude;
    }

    public void setLastLocationLongitude(double lastLocationLongitude) {
        this.lastLocationLongitude = lastLocationLongitude;
    }

    public double getFirstLocationLongitude() {
        return firstLocationLongitude;
    }

    public void setFirstLocationLongitude(double firstLocationLongitude) {
        this.firstLocationLongitude = firstLocationLongitude;
    }
}
