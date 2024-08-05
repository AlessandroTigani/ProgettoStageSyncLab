package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.*;
import com.synclab.triphippie.repository.TripRepository;
import com.synclab.triphippie.util.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @InjectMocks
    private TripService tripService;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private Utility utility;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Trip trip = new Trip();
        trip.setStartDate(LocalDateTime.now());
        trip.setEndDate(LocalDateTime.now().plusDays(1));
        UserProfile userProfile = new UserProfile();

        tripService.save(trip, userProfile);

        verify(tripRepository, times(1)).save(trip);
        assertEquals(userProfile, trip.getUserProfile());
    }

    @Test
    void testSave_InvalidEndDate() {
        Trip trip = new Trip();
        trip.setStartDate(LocalDateTime.now());
        trip.setEndDate(LocalDateTime.now().minusDays(1));
        UserProfile userProfile = new UserProfile();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tripService.save(trip, userProfile));
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testFindById() {
        Trip trip = new Trip();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trip, result.get());
        verify(tripRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByFilters() {
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        List<Trip> trips = Arrays.asList(trip1, trip2);
        Page<Trip> page = new PageImpl<>(trips);
        when(tripRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        List<Trip> result = tripService.findByFilters(0, 2, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L);

        assertEquals(trips.size(), result.size());
        verify(tripRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void testUpdate() {
        Long tripId = 1L;
        Trip trip = new Trip();
        Trip existingTrip = new Trip();
        trip.setDescription("New Description");
        existingTrip.setDescription("Old Description");

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));

        tripService.update(trip, tripId);

        verify(utility).updateIfNotNull(eq(trip.getDescription()), any());
        verify(tripRepository).save(existingTrip);
    }

    @Test
    void testUpdate_TripNotFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        Trip updatedTrip = new Trip();

        assertThrows(EntryNotFoundException.class, () -> tripService.update(updatedTrip, 1L));
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testDeleteById() {
        Trip trip = new Trip();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        tripService.deleteById(1L);

        verify(tripRepository, times(1)).delete(trip);
    }

    @Test
    void testDeleteById_TripNotFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> tripService.deleteById(1L));
        verify(tripRepository, never()).delete(any(Trip.class));
    }

    @Test
    void testFindAllCompletedTrips() {
        Trip trip = new Trip();
        List<Trip> trips = Arrays.asList(trip);
        when(tripRepository.findByEndDateLessThan(any(LocalDateTime.class))).thenReturn(trips);

        List<Trip> result = tripService.findAllCompletedTrips();

        assertEquals(trips, result);
        verify(tripRepository, times(1)).findByEndDateLessThan(any(LocalDateTime.class));
    }

    @Test
    void testFindAllTripsToComplete() {
        Trip trip = new Trip();
        List<Trip> trips = Arrays.asList(trip);
        when(tripRepository.findByEndDateGreaterThan(any(LocalDateTime.class))).thenReturn(trips);

        List<Trip> result = tripService.findAllTripsToComplete();

        assertEquals(trips, result);
        verify(tripRepository, times(1)).findByEndDateGreaterThan(any(LocalDateTime.class));
    }
}
