package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.TripDTO;
import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.service.TripService;
import com.synclab.triphippie.service.UserService;
import com.synclab.triphippie.util.JwtUtil;
import com.synclab.triphippie.util.TripConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;
    private final TripConverter tripConverter;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public TripController(TripService tripService, TripConverter tripConverter, JwtUtil jwtUtil, UserService userService) {
        this.tripService = tripService;
        this.tripConverter = tripConverter;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody TripDTO tripDTO) {
        Trip trip = this.tripConverter.toEntity(tripDTO);
        tripService.save(trip);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getById(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Trip trip = tripService.findById(id);
        UserProfile userProfile = userService.findById(trip.getUserId());
        if(!jwtUtil.validateToken(authorizationHeader, userProfile.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        TripDTO tripDTO = tripConverter.toDto(trip);
        return ResponseEntity.ok(tripDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @RequestBody TripDTO tripDTO,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Trip tripFound = tripService.findById(id);
        UserProfile userProfile = userService.findById(tripFound.getUserId());
        if(!jwtUtil.validateToken(authorizationHeader, userProfile.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Trip tripToUpdate = tripConverter.toEntity(tripDTO);
        tripService.update(tripToUpdate, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Trip tripFound = tripService.findById(id);
        UserProfile userProfile = userService.findById(tripFound.getUserId());
        if(!jwtUtil.validateToken(authorizationHeader, userProfile.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.tripService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
