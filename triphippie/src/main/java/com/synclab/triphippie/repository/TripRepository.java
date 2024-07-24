package com.synclab.triphippie.repository;


import com.synclab.triphippie.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> , JpaSpecificationExecutor<Trip> {

    List<Trip> findByEndDateLessThan (LocalDateTime todayDate);

    List<Trip> findByEndDateGreaterThan(LocalDateTime todayDate);

}

