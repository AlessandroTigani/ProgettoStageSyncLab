package com.synclab.triphippie.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "preference_vehicle_name")
    private PreferenceVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "preference_tag_name")
    private PreferenceTag type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_destination_id", referencedColumnName = "id")
    private Destination startDestination;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_destination_id", referencedColumnName = "id")
    private Destination endDestination;

    @Column
    private String description;


    // Constructor
    public Trip() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public PreferenceVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(PreferenceVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public PreferenceTag getType() {
        return type;
    }

    public void setType(PreferenceTag type) {
        this.type = type;
    }

    public Destination getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(Destination startDestination) {
        this.startDestination = startDestination;
    }

    public Destination getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(Destination endDestination) {
        this.endDestination = endDestination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
