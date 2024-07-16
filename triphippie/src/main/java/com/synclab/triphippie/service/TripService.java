package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.exception.UniqueFieldException;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void save(Trip trip) {
        if(trip.getStartDateTime().isAfter(trip.getEndDateTime())){
            throw new RuntimeException("Invalid ending date for the trip.");
        }
        tripRepository.save(trip);
    }

    public Trip findById(Long id) {
        Optional<Trip> tripFound = this.tripRepository.findById(id);
        if (tripFound.isPresent()) {
            return tripFound.get();
        }
        else{
            throw new EntryNotFoundException("Trip not found");
        }
    }

    public void update(Trip trip, Long id) {
        Optional<Trip> tripFound = this.tripRepository.findById(id);
        if(tripFound.isEmpty()) {
            throw new EntryNotFoundException("Trip not found");
        }
        else{
            Trip temp = tripFound.get();
            if (trip.getDescription() != null) {
                temp.setDescription(trip.getDescription());
            }
            if (trip.getPreferences() != null) {
                temp.setPreferences(trip.getPreferences());
            }
            if (trip.getStartDateTime() != null) {
                temp.setStartDateTime(trip.getStartDateTime());
            }
            if (trip.getEndDateTime() != null) {
                temp.setEndDateTime(trip.getEndDateTime());
            }
            if (trip.getFirstLocationName() != null) {
                temp.setFirstLocationName(trip.getFirstLocationName());
            }
            if (trip.getLastLocationName() != null) {
                temp.setLastLocationName(trip.getLastLocationName());
            }
            temp.setFirstLocationLatitude(trip.getFirstLocationLatitude());
            temp.setFirstLocationLongitude(trip.getFirstLocationLongitude());
            temp.setLastLocationLatitude(trip.getLastLocationLatitude());
            temp.setLastLocationLongitude(trip.getLastLocationLongitude());
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
}
