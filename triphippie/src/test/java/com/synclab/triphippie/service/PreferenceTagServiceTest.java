package com.synclab.triphippie.service;

import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class PreferenceTagServiceTest {

    @InjectMocks
    private PreferenceTagService preferenceTagService;

    @Mock
    private PreferenceTagRepository preferenceTagRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        PreferenceTag tag1 = new PreferenceTag();
        tag1.setName("Tag1");
        PreferenceTag tag2 = new PreferenceTag();
        tag2.setName("Tag2");
        List<PreferenceTag> tags = Arrays.asList(tag1, tag2);
        when(preferenceTagRepository.findAll()).thenReturn(tags);

        String[] result = preferenceTagService.findAll();

        assertArrayEquals(new String[]{"Tag1", "Tag2"}, result);
        verify(preferenceTagRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(preferenceTagRepository.findAll()).thenReturn(List.of());

        String[] result = preferenceTagService.findAll();

        assertArrayEquals(new String[]{}, result);
        verify(preferenceTagRepository, times(1)).findAll();
    }
}
