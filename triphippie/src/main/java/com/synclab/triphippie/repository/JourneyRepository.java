package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByTrip(Trip trip);
}
