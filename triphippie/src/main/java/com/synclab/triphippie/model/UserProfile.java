package com.synclab.triphippie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
