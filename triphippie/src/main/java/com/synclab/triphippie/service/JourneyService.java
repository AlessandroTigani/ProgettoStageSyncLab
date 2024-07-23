package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.repository.JourneyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;

    public JourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    public void save(Journey journey) {
        journeyRepository.save(journey);
    }

    public Optional<Journey> findById(Long id) {
        return this.journeyRepository.findById(id);
    }

    public List<Journey> findAllByTrip(Trip trip) {
        return this.journeyRepository.findByTrip(trip);
    }

    public void update(Journey journey, Long id) {
        Optional<Journey> journeyFound = this.journeyRepository.findById(id);
        if(journeyFound.isEmpty()) {
            throw new EntryNotFoundException("Journey not found");
        }
        else{
            Journey temp = journeyFound.get();
            if (journey.getTrip() != null)
                temp.setTrip(journey.getTrip());
            if(journey.getStepNumber() != null)
                temp.setStepNumber(journey.getStepNumber());
            if (journey.getDestination() != null)
                temp.setDestination(journey.getDestination());
            if (journey.getDescription() != null)
                temp.setDescription(journey.getDescription());
            journeyRepository.save(temp);
        }
    }

    public void deleteById(Long id) {
        Optional<Journey> journeyFound = this.journeyRepository.findById(id);
        if(journeyFound.isPresent()) {
            journeyRepository.delete(journeyFound.get());
        }
        else{
            throw new EntryNotFoundException("Journey not found");
        }
    }

}
