package com.synclab.triphippie.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PreferenceTagDTO {
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is required")
    @Size(min=2, max = 20, message = "The length of the name must be between 2 and 20 characters.")
    private String name;


    @Size(max = 255, message = "The length of the description must be less than 255 characters.")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
