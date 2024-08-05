package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.*;
import com.synclab.triphippie.repository.TripRepository;
import com.synclab.triphippie.util.Utility;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final Utility utility;

    public TripService(TripRepository tripRepository, Utility utility) {
        this.tripRepository = tripRepository;
        this.utility = utility;
    }


    /**
     * Creates a specification for filtering Trip entities based on provided start date, end date, and user profile.
     * @param startDate the start date to filter trips. Only trips starting on or after this date will be included.
     * @param endDate the end date to filter trips. Only trips ending on or before this date will be included.
     * @param userProfile the ID of the user profile to filter trips. Only trips associated with this user profile will be included.
     * @return a Specification object that can be used to filter Trip entities based on the given criteria.
     */
    private Specification<Trip> withFilters(LocalDateTime startDate, LocalDateTime endDate, Long userProfile) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (startDate != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
            if (endDate != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
            if (userProfile != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userProfile").get("id"), userProfile));
            return predicate;
        };
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

    public void update(Trip trip, Long id) {
        Trip temp = this.tripRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Trip not found"));
        utility.updateIfNotNull(trip.getDescription(), temp::setDescription);
        utility.updateIfNotNull(trip.getStartDate(), temp::setStartDate);
        utility.updateIfNotNull(trip.getEndDate(), temp::setEndDate);
        utility.updateIfNotNull(trip.getVehicle(), temp::setVehicle);
        utility.updateIfNotNull(trip.getType(), temp::setType);
        utility.updateIfNotNull(trip.getStartDestination(), temp::setStartDestination);
        utility.updateIfNotNull(trip.getEndDestination(), temp::setEndDestination);
        tripRepository.save(temp);
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

    public Long findTotalTrips(){
        return tripRepository.count();
    }

}
