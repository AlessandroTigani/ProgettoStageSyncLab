package com.synclab.triphippie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PreferenceVehicle {
    @Id
    @Size(min=2, max = 20, message = "The length of the name must be between 2 and 20 characters.")
    @NotNull(message = "Name is required.")
    private String name;


    @Size(max = 255, message = "The length of the description must be less than 255 characters.")
    @Column(length = 255, nullable = true)
    private String description;


    @ManyToMany(mappedBy = "vehicles")
    @JsonIgnore
    private Set<UserProfile> users = new HashSet<>();


    public PreferenceVehicle() {}

    public PreferenceVehicle(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserProfile> getUsers() {
        return users;
    }

    public void setUsers(Set<UserProfile> users) {
        this.users = users;
    }
}
