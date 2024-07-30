package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.Journey;
import com.synclab.triphippie.model.Trip;
import com.synclab.triphippie.repository.JourneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JourneyServiceTest {

    @InjectMocks
    private JourneyService journeyService;

    @Mock
    private JourneyRepository journeyRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Journey journey = new Journey();
        journey.setId(1L);

        journeyService.save(journey);

        verify(journeyRepository, times(1)).save(journey);
    }


    //TODO: inserisci journey 2 per confronto
    @Test
    void testFindById_Success() {
        Journey journey = new Journey();
        journey.setId(1L);
        when(journeyRepository.findById(1L)).thenReturn(Optional.of(journey));

        Optional<Journey> result = journeyService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(journey, result.get());
        verify(journeyRepository, times(1)).findById(1L);
    }


    @Test
    void testFindById_NotFound() {
        when(journeyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Journey> result = journeyService.findById(1L);

        assertFalse(result.isPresent());
    }


    @Test
    void testFindAllByTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        Journey journey1 = new Journey();
        journey1.setId(1L);
        Journey journey2 = new Journey();
        journey2.setId(2L);
        List<Journey> journeys = List.of(journey1, journey2);
        when(journeyRepository.findByTrip(trip)).thenReturn(journeys);

        List<Journey> result = journeyService.findAllByTrip(trip);

        assertEquals(journeys, result);
    }


    @Test
    void testUpdate_Success() {
        Journey existingJourney = new Journey();
        existingJourney.setId(1L);
        existingJourney.setDescription("Old Description");

        Journey updatedJourney = new Journey();
        updatedJourney.setDescription("New Description");

        when(journeyRepository.findById(1L)).thenReturn(Optional.of(existingJourney));

        journeyService.update(updatedJourney, 1L);

        verify(journeyRepository, times(1)).save(existingJourney);
        assertEquals("New Description", existingJourney.getDescription());
    }


    @Test
    void testUpdate_NotFound() {
        Journey updatedJourney = new Journey();
        when(journeyRepository.findById(1L)).thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () ->
                journeyService.update(updatedJourney, 1L)
        );
        assertEquals("Journey not found", exception.getMessage());
    }


    @Test
    void testDeleteById_Success() {
        Journey journey = new Journey();
        journey.setId(1L);
        when(journeyRepository.findById(1L)).thenReturn(Optional.of(journey));

        journeyService.deleteById(1L);

        verify(journeyRepository, times(1)).delete(journey);
    }


    @Test
    void testDeleteById_NotFound() {
        when(journeyRepository.findById(1L)).thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () ->
                journeyService.deleteById(1L)
        );
        assertEquals("Journey not found", exception.getMessage());
    }
}
