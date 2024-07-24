package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.IdDTO;
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
    public ResponseEntity<String> create(
            @Valid @RequestBody JourneyDTO dto,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        if(jwtUtil.isTokenExpired(authorizationHeader)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Trip> tripFound = tripService.findById(dto.getTripId());
        if(tripFound.isEmpty())
            throw new EntryNotFoundException("Trip not found.");
        Journey entity = journeyConverter.toEntity(dto);
        entity.setId(1L);
        journeyService.save(entity);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JourneyDTO>> getAllByTripId(
            @RequestBody IdDTO id
    ) {
        Optional<Trip> tripFound = tripService.findById(id.getTripId());
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
            throw new EntryNotFoundException("Journey not found.");
        JourneyDTO journeyDTO = journeyConverter.toDto(journeyFound.get());
        return new ResponseEntity<>(journeyDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Valid @RequestBody JourneyDTO dto
    ){
        Optional<Journey> journeyFound = journeyService.findById(id);
        if(journeyFound.isEmpty())
            throw new EntryNotFoundException("Journey not found.");
        if(jwtUtil.isTokenExpired(authorizationHeader)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Journey journeyToUpdate = journeyConverter.toEntity(dto);
        journeyService.update(journeyToUpdate, id);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Optional<Journey> journeyFound = journeyService.findById(id);
        if(journeyFound.isEmpty())
            throw new EntryNotFoundException("Journey not found.");
        if(jwtUtil.isTokenExpired(authorizationHeader)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        journeyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
