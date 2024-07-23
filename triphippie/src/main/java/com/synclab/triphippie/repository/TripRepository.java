package com.synclab.triphippie.repository;


import com.synclab.triphippie.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> , JpaSpecificationExecutor<Trip> {

    @Query("SELECT t FROM Trip t WHERE t.endDate < :todayDate")
    List<Trip> findAllCompletedTrips(@Param("todayDate") LocalDateTime todayDate);

    @Query("SELECT t FROM Trip t WHERE t.endDate > :todayDate")
    List<Trip> findAllTripsToComplete(@Param("todayDate") LocalDateTime todayDate);

    @Query("SELECT t FROM Trip t WHERE " +
            "(t.startDate >= :startDate) AND " +
            "(t.endDate <= :endDate) AND " +
            "(:userProfile IS null OR t.userProfile.id = :userProfile)")
    Page<Trip> findByFilters(@Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             @Param("userProfile") Long userProfile, // Changed to Integer to handle null values
                             Pageable pageable);



}
