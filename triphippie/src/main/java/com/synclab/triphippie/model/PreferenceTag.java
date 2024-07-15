package com.synclab.triphippie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.catalina.User;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PreferenceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min=2, max = 20, message = "The length of the name must be between 2 and 20 characters.")
    @Column(length = 20, nullable = false)
    @NotNull(message = "Name is required.")
    private String name;


    @Size(max = 255, message = "The length of the description must be less than 255 characters.")
    @Column(length = 255, nullable = true)
    private String description;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<UserProfile> users = new HashSet<>();

    public PreferenceTag() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
