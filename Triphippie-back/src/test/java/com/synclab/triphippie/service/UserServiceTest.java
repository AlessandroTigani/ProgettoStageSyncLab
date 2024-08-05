package com.synclab.triphippie.service;

import com.synclab.triphippie.dto.PreferenceTagDTO;
import com.synclab.triphippie.dto.PreferenceVehicleDTO;
import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.exception.UniqueFieldException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.model.PreferenceVehicle;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import com.synclab.triphippie.repository.PreferenceVehicleRepository;
import com.synclab.triphippie.repository.UserRepository;
import com.synclab.triphippie.util.HashUtil;
import com.synclab.triphippie.util.PreferenceTagConverter;
import com.synclab.triphippie.util.PreferenceVehicleConverter;
import com.synclab.triphippie.util.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreferenceTagRepository preferenceTagRepository;

    @Mock
    private PreferenceTagConverter preferenceTagConverter;

    @Mock
    private PreferenceVehicleConverter preferenceVehicleConverter;

    @Mock
    private PreferenceVehicleRepository preferenceVehicleRepository;

    @Mock
    private Utility utility;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setProperty("IMAGE_PATH", "path/to/images");
    }

    @Test
    void testSave() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testUser");
        when(userRepository.existsByUsername("testUser")).thenReturn(false);

        userService.save(userProfile);

        verify(userRepository, times(1)).save(userProfile);
    }

    @Test
    void testSave_UsernameAlreadyExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testUser");
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThrows(UniqueFieldException.class, () -> userService.save(userProfile));
        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void testFindById() {
        // Arrange
        UserProfile userProfile = new UserProfile();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        // Act
        Optional<UserProfile> result = userService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userProfile, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByUsername() {
        UserProfile userProfile = new UserProfile();
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userProfile));

        Optional<UserProfile> result = userService.findByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals(userProfile, result.get());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUsers() {
        UserProfile user1 = new UserProfile();
        UserProfile user2 = new UserProfile();
        List<UserProfile> users = Arrays.asList(user1, user2);
        Page<UserProfile> page = new PageImpl<>(users);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<UserProfile> result = userService.getUsers("", Pageable.unpaged());

        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("updatedUser");

        ArrayList<UserProfile> alreadyExistingUsers = mock(ArrayList.class);

        UserProfile existingUser = new UserProfile();
        existingUser.setId(1L);
        existingUser.setUsername("updatedUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsernameAndDifferentId("updatedUser", 1L)).thenReturn(alreadyExistingUsers);
        when(alreadyExistingUsers.isEmpty()).thenReturn(true);


        userService.update(userProfile, 1L);

        verify(userRepository, times(1)).save(existingUser);
        assertEquals("updatedUser", existingUser.getUsername());
    }

    @Test
    void testUpdate_UserNotFound() {
        UserProfile userProfile = new UserProfile();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.update(userProfile, 1L));
        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void testDeleteById() {
        UserProfile userProfile = new UserProfile();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(userProfile);
    }

    @Test
    void testDeleteById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.deleteById(1L));
        verify(userRepository, never()).delete(any(UserProfile.class));
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        boolean result = userService.existsByUsername("testUser");

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername("testUser");
    }

    @Test
    void testHashPassword() {
        String plainPassword = "password";
        String hashedPassword = HashUtil.hashPassword(plainPassword);
        try (MockedStatic<HashUtil> mocked = mockStatic(HashUtil.class)){
            when(HashUtil.hashPassword(plainPassword)).thenReturn(hashedPassword);
            String result = userService.hashPassword(plainPassword);
            assertEquals(hashedPassword, result);
        }
    }

    @Test
    void testVerifyPassword() {
        String plainPassword = "password";
        String hashedPassword = HashUtil.hashPassword(plainPassword);
        try (MockedStatic<HashUtil> mocked = mockStatic(HashUtil.class)){
            when(HashUtil.verifyPassword(plainPassword, hashedPassword)).thenReturn(true);
            boolean result = userService.verifyPassword(plainPassword, hashedPassword);
            assertTrue(result);
        }
    }


    @Test
    void testAddPreferenceTagToUserProfile() {
        UserProfile userProfile = new UserProfile();
        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("tag1");
        List<PreferenceTagDTO> preferenceTagDTOs = List.of(new PreferenceTagDTO("tag1"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceTagRepository.findByName("tag1")).thenReturn(Optional.of(preferenceTag));

        userService.addPreferenceTagToUserProfile(1L, preferenceTagDTOs);

        assertTrue(userProfile.getTags().contains(preferenceTag));
        verify(userRepository, times(1)).save(userProfile);
    }

    @Test
    void testAddPreferenceTagToUserProfile_UserNotFound() {
        List<PreferenceTagDTO> preferenceTagDTOs = List.of(new PreferenceTagDTO("tag1"));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.addPreferenceTagToUserProfile(1L, preferenceTagDTOs));
    }

    @Test
    void testAddPreferenceTagToUserProfile_TagNotFound() {
        UserProfile userProfile = new UserProfile();
        List<PreferenceTagDTO> preferenceTagDTOs = List.of(new PreferenceTagDTO("tag1"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceTagRepository.findByName("tag1")).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.addPreferenceTagToUserProfile(1L, preferenceTagDTOs));
    }

    @Test
    void testGetAllUserPreferenceTags() {
        UserProfile userProfile = new UserProfile();
        PreferenceTag preferenceTag = new PreferenceTag();
        preferenceTag.setName("tag1");
        userProfile.getTags().add(preferenceTag);
        PreferenceTagDTO preferenceTagDTO = new PreferenceTagDTO("tag1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceTagConverter.toDto(preferenceTag)).thenReturn(preferenceTagDTO);

        List<PreferenceTagDTO> result = userService.getAllUserPreferenceTags(1L);

        assertEquals(1, result.size());
        assertEquals("tag1", result.getFirst().getName());
    }

    @Test
    void testRemovePreferenceTagFromUser() {
        UserProfile userProfile = new UserProfile();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        userService.removePreferenceTagFromUser(1L);

        verify(userRepository, times(1)).deleteTagsByUserId(1L);
    }

    @Test
    void testAddPreferenceVehiclesToUserProfile() {
        UserProfile userProfile = new UserProfile();
        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("vehicle1");
        List<PreferenceVehicleDTO> preferenceVehicleDTOs = List.of(new PreferenceVehicleDTO("vehicle1"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceVehicleRepository.findByName("vehicle1")).thenReturn(Optional.of(preferenceVehicle));

        userService.addPreferenceVehiclesToUserProfile(1L, preferenceVehicleDTOs);

        assertTrue(userProfile.getVehicles().contains(preferenceVehicle));
        verify(userRepository, times(1)).save(userProfile);
    }

    @Test
    void testAddPreferenceVehiclesToUserProfile_UserNotFound() {
        List<PreferenceVehicleDTO> preferenceVehicleDTOs = List.of(new PreferenceVehicleDTO("vehicle1"));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.addPreferenceVehiclesToUserProfile(1L, preferenceVehicleDTOs));
    }

    @Test
    void testAddPreferenceVehiclesToUserProfile_VehicleNotFound() {
        UserProfile userProfile = new UserProfile();
        List<PreferenceVehicleDTO> preferenceVehicleDTOs = List.of(new PreferenceVehicleDTO("vehicle1"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceVehicleRepository.findByName("vehicle1")).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> userService.addPreferenceVehiclesToUserProfile(1L, preferenceVehicleDTOs));
    }

    @Test
    void testGetAllUserPreferenceVehicles() {
        UserProfile userProfile = new UserProfile();
        PreferenceVehicle preferenceVehicle = new PreferenceVehicle();
        preferenceVehicle.setName("vehicle1");
        userProfile.getVehicles().add(preferenceVehicle);
        PreferenceVehicleDTO preferenceVehicleDTO = new PreferenceVehicleDTO("vehicle1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(preferenceVehicleConverter.toDto(preferenceVehicle)).thenReturn(preferenceVehicleDTO);

        List<PreferenceVehicleDTO> result = userService.getAllUserPreferenceVehicles(1L);

        assertEquals(1, result.size());
        assertEquals("vehicle1", result.getFirst().getName());
    }

    @Test
    void testRemovePreferenceVehiclesFromUser() {
        UserProfile userProfile = new UserProfile();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        userService.removePreferenceVehiclesFromUser(1L);

        verify(userRepository, times(1)).deleteVehiclesByUserId(1L);
    }
}
