package com.synclab.triphippie.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column
    private String preferences;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column
    private String lastLocationName;

    @Column(nullable = false)
    private String firstLocationName;

    @Column
    private double lastLocationLatitude;

    @Column
    private double firstLocationLatitude;

    @Column
    private double lastLocationLongitude;

    @Column
    private double firstLocationLongitude;

    // Constructor
    public Trip() {}

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getLastLocationName() {
        return lastLocationName;
    }

    public void setLastLocationName(String lastLocationName) {
        this.lastLocationName = lastLocationName;
    }

    public String getFirstLocationName() {
        return firstLocationName;
    }

    public void setFirstLocationName(String firstLocationName) {
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
