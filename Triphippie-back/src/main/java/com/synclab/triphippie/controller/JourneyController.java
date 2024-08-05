package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.JourneyDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.synclab.triphippie.service.JourneyService;
import com.synclab.triphippie.service.TripService;
import com.synclab.triphippie.util.JourneyConverter;
import com.synclab.triphippie.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    private final JourneyService journeyService;
    private final JourneyConverter journeyConverter;
    private final JwtUtil jwtUtil;
    private final TripService tripService;
    private static final String JOURNEY_NOT_FOUND = "Journey not found.";  //


    public JourneyController(
            JourneyService journeyService,
            JourneyConverter journeyConverter,
            JwtUtil jwtUtil,
            TripService tripService) {
        this.journeyService = journeyService;
        this.journeyConverter = journeyConverter;
        this.jwtUtil = jwtUtil;
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<JourneyDTO> create(
            @Valid @RequestBody JourneyDTO dto,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        boolean isTokenExpired = jwtUtil.isTokenExpired(authorizationHeader);
        if(isTokenExpired){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Trip> tripFound = tripService.findById(dto.getTripId());
        if(tripFound.isEmpty())
            throw new EntryNotFoundException("Trip not found.");
        Journey entity = journeyConverter.toEntity(dto);
        entity.setId(1L);
        journeyService.save(entity);
        JourneyDTO journeyDTO = journeyConverter.toDto(entity);
        return new ResponseEntity<>(journeyDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JourneyDTO>> getAllByTripId(
            @RequestParam Long tripId
    ) {
        Optional<Trip> tripFound = tripService.findById(tripId);
        if(tripFound.isEmpty())
            throw new EntryNotFoundException("Trip not found.");
        List<Journey> journeys = journeyService.findAllByTrip(tripFound.get());
        List<JourneyDTO> journeyDTOS = new ArrayList<>();
        for(Journey journey : journeys){
            JourneyDTO dto = journeyConverter.toDto(journey);
            journeyDTOS.add(dto);
        }
        return new ResponseEntity<>(journeyDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JourneyDTO> getAllByJourneyId(
            @PathVariable("id") Long id
    ) {
        Optional<Journey> journeyFound = journeyService.findById(id);
        if(journeyFound.isEmpty())
            throw new EntryNotFoundException(JOURNEY_NOT_FOUND);
        JourneyDTO journeyDTO = journeyConverter.toDto(journeyFound.get());
        return new ResponseEntity<>(journeyDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JourneyDTO> update(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Valid @RequestBody JourneyDTO dto
    ){
        Optional<Journey> journeyFound = journeyService.findById(id);
        if(journeyFound.isEmpty())
            throw new EntryNotFoundException(JOURNEY_NOT_FOUND);
        boolean isTokenExpired = jwtUtil.isTokenExpired(authorizationHeader);
        if(isTokenExpired){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Journey journeyToUpdate = journeyConverter.toEntity(dto);
        journeyService.update(journeyToUpdate, id);
        JourneyDTO journeyDTO = journeyConverter.toDto(journeyToUpdate);
        return new ResponseEntity<>(journeyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Optional<Journey> journeyFound = journeyService.findById(id);
        if(journeyFound.isEmpty())
            throw new EntryNotFoundException(JOURNEY_NOT_FOUND);
        boolean isTokenExpired = jwtUtil.isTokenExpired(authorizationHeader);
        if(isTokenExpired){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        journeyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
