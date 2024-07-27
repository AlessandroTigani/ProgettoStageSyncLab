package com.synclab.triphippie.service;

import com.synclab.triphippie.model.PreferenceVehicle;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class PreferenceVehicleServiceTest {

    @InjectMocks
    private PreferenceVehicleService preferenceVehicleService;

    @Mock
    private PreferenceVehicleRepository preferenceVehicleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        PreferenceVehicle vehicle1 = new PreferenceVehicle();
        vehicle1.setName("Vehicle1");
        PreferenceVehicle vehicle2 = new PreferenceVehicle();
        vehicle2.setName("Vehicle2");
        List<PreferenceVehicle> vehicles = Arrays.asList(vehicle1, vehicle2);
        when(preferenceVehicleRepository.findAll()).thenReturn(vehicles);

        String[] result = preferenceVehicleService.findAll();

        assertArrayEquals(new String[]{"Vehicle1", "Vehicle2"}, result);
        verify(preferenceVehicleRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(preferenceVehicleRepository.findAll()).thenReturn(List.of());

        String[] result = preferenceVehicleService.findAll();

        assertArrayEquals(new String[]{}, result);
        verify(preferenceVehicleRepository, times(1)).findAll();
    }
}
