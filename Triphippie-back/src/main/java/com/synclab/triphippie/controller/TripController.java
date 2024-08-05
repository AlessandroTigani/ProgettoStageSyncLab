package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.TripDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.service.PreferenceTagService;
import com.synclab.triphippie.service.PreferenceVehicleService;
import com.synclab.triphippie.service.TripService;
import com.synclab.triphippie.service.UserService;
import com.synclab.triphippie.util.JwtUtil;
import com.synclab.triphippie.util.TripConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;
    private final TripConverter tripConverter;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PreferenceTagService preferenceTagService;
    private final PreferenceVehicleService preferenceVehicleService;

    public TripController(
            TripService tripService,
            TripConverter tripConverter,
            JwtUtil jwtUtil,
            UserService userService,
            PreferenceTagService preferenceTagService,
            PreferenceVehicleService preferenceVehicleService
    ) {
        this.tripService = tripService;
        this.tripConverter = tripConverter;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.preferenceTagService= preferenceTagService;
        this.preferenceVehicleService = preferenceVehicleService;
    }

    @PostMapping
    public ResponseEntity<TripDTO> create(
            @Valid @RequestBody TripDTO tripDTO,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Long userId = jwtUtil.extractId(authorizationHeader);
        tripDTO.setUserId(userId);
        Trip trip = this.tripConverter.toEntity(tripDTO);
        Optional<UserProfile> userProfile = userService.findById(trip.getUserProfile().getId());
        if(userProfile.isEmpty()) {
            throw new EntryNotFoundException("User not found");
        }
        boolean isTokenValid = jwtUtil.validateToken(authorizationHeader, userProfile.get().getUsername());
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        tripService.save(trip, userProfile.get());
        TripDTO tripDTOSaved = this.tripConverter.toDto(trip);
        return new ResponseEntity<>(tripDTOSaved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getById(@PathVariable("id") Long id) {
        Optional<Trip> trip = tripService.findById(id);
        if(trip.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        TripDTO tripDTO = tripConverter.toDto(trip.get());
        return ResponseEntity.ok(tripDTO);
    }


    @GetMapping
    public List<TripDTO> getTrips(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "tripsSize") int tripsSize,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "userId", required = false) Long userId
    ){
        if(userId != null && userService.findById(userId).isEmpty()){
            throw new EntryNotFoundException("User not found");
        }
        if(startDate != null && endDate != null && startDate.isAfter(endDate)){
            throw new EntryNotFoundException("Start date is after end date");
        }
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        startDateTime = startDate == null ? null : startDate.atStartOfDay();
        endDateTime = endDate == null ? null : endDate.atStartOfDay();
        List<Trip> trips = tripService.findByFilters(page, tripsSize, startDateTime, endDateTime, userId);
        List<TripDTO> tripDTOs = new ArrayList<>();
        for (Trip trip : trips) {
            tripDTOs.add(this.tripConverter.toDto(trip));
        }
        return tripDTOs;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @RequestBody TripDTO tripDTO,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Optional<Trip> tripFound = tripService.findById(id);
        if(tripFound.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        boolean isTokenValid = jwtUtil.validateToken(authorizationHeader, tripFound.get().getUserProfile().getUsername());
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        tripDTO.setUserId(tripFound.get().getUserProfile().getId());
        Trip tripToUpdate = tripConverter.toEntity(tripDTO);
        tripService.update(tripToUpdate, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Optional<Trip> tripFound = tripService.findById(id);
        if(tripFound.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        boolean isTokenValid = jwtUtil.validateToken(authorizationHeader, tripFound.get().getUserProfile().getUsername());
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.tripService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TripDTO>> getAllCompletedTrips() {
        List<Trip> trips = this.tripService.findAllCompletedTrips();
        List<TripDTO> tripDTOs = new ArrayList<>();
        for (Trip trip : trips) {
            tripDTOs.add(this.tripConverter.toDto(trip));
        }
        return new ResponseEntity<>(tripDTOs, HttpStatus.FOUND);
    }


    @GetMapping("/total")
    public ResponseEntity<Long> getTotalTrips() {
        Long totalTrips = this.tripService.findTotalTrips();
        return new ResponseEntity<>(totalTrips, HttpStatus.OK);
    }

    @GetMapping("/to-complete")
    public ResponseEntity<List<TripDTO>> getAllTripsToComplete() {
        List<Trip> trips = this.tripService.findAllTripsToComplete();
        List<TripDTO> tripDTOs = new ArrayList<>();
        for (Trip trip : trips) {
            tripDTOs.add(this.tripConverter.toDto(trip));
        }
        return new ResponseEntity<>(tripDTOs, HttpStatus.FOUND);
    }

    @GetMapping("/vehicles")
    public ResponseEntity<String[]> getAllVehiclesOptions() {
        String[] options = this.preferenceVehicleService.findAll();
        return new ResponseEntity<>(options, HttpStatus.FOUND);
    }

    @GetMapping("/types")
    public ResponseEntity<String[]> getAllTypesOptions() {
        String[] options = this.preferenceTagService.findAll();
        return new ResponseEntity<>(options, HttpStatus.FOUND);
    }

}
