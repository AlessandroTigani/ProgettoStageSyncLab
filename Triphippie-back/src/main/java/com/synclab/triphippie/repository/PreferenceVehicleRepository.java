package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.PreferenceVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceVehicleRepository extends JpaRepository<PreferenceVehicle, String> {
    Optional<PreferenceVehicle> findByName(String name);
}
