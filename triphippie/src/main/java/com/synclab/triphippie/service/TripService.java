package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.*;
import com.synclab.triphippie.repository.TripRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository, UserService userService) {
        this.tripRepository = tripRepository;
    }

    public void save(Trip trip, UserProfile userProfile) {
        if(trip.getStartDate().isAfter(trip.getEndDate())){
            throw new RuntimeException("Invalid ending date for the trip.");
        }
        trip.setUserProfile(userProfile);
        tripRepository.save(trip);
    }

    public Optional<Trip> findById(Long id) {
        return this.tripRepository.findById(id);
    }

    public List<Trip> findByFilters(Integer page, Integer tripsSize, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        Pageable pageable = PageRequest.of(page, tripsSize);
        Specification<Trip> spec = withFilters(startDate, endDate, userId);
        return tripRepository.findAll(spec, pageable).getContent();
    }

    public static Specification<Trip> withFilters(LocalDateTime startDate, LocalDateTime endDate, Long userProfile) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Start with an always-true predicate

            if (startDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            if (endDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
            }
            if (userProfile != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userProfile").get("id"), userProfile));
            }

            return predicate;
        };
    }



    public void update(Trip trip, Long id) {
        Optional<Trip> tripFound = this.tripRepository.findById(id);
        if(tripFound.isEmpty()) {
            throw new EntryNotFoundException("Trip not found");
        }
        else{
            Trip temp = tripFound.get();
            if (trip.getDescription() != null)
                temp.setDescription(trip.getDescription());
            if(trip.getStartDate() != null)
                temp.setStartDate(trip.getStartDate());
            if(trip.getEndDate() != null)
                temp.setEndDate(trip.getEndDate());
            if(trip.getVehicle() != null)
                temp.setVehicle(trip.getVehicle());
            if(trip.getType() != null)
                temp.setType(trip.getType());
            if(trip.getStartDestination() != null)
                temp.setStartDestination(trip.getStartDestination());
            if(trip.getEndDestination() != null)
                temp.setEndDestination(trip.getEndDestination());
            tripRepository.save(temp);
        }
    }

    public void deleteById(Long id) {
        Optional<Trip> tripFound = this.tripRepository.findById(id);
        if(tripFound.isPresent()) {
            tripRepository.delete(tripFound.get());
        }
        else{
            throw new EntryNotFoundException("Trip not found");
        }
    }

    public List<Trip> findAllCompletedTrips() {
        return tripRepository.findByEndDateLessThan(LocalDateTime.now());
    }

    public List<Trip> findAllTripsToComplete() {
        return tripRepository.findByEndDateGreaterThan(LocalDateTime.now());
    }

}
