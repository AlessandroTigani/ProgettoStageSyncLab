package com.synclab.triphippie.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
