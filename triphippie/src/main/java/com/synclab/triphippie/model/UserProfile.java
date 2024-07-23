package com.synclab.triphippie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column
    private Date dateOfBirth;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String about;

    @Column(length = 50)
    private String city;

    @ManyToMany
    @JoinTable(
            name = "user_profile_preference_tag",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_tag_name")
    )
    @JsonIgnore
    private Set<PreferenceTag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_profile_preference_vehicle",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_vehicle_name")
    )
    @JsonIgnore
    private Set<PreferenceVehicle> vehicles = new HashSet<>();

    public UserProfile() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public Set<PreferenceTag> getTags() {
        return tags;
    }

    public void setTags(Set<PreferenceTag> tags) {
        this.tags = tags;
    }

    public void removeTag(PreferenceTag tag) {
        this.tags.remove(tag);
        tag.getUsers().remove(this);
    }

    public Set<PreferenceVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<PreferenceVehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
