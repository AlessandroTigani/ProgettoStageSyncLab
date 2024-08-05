package com.synclab.triphippie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Integer stepNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_destination_id", referencedColumnName = "id")
    private Destination destination;

    @Column
    private String description;

}
