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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final String filePath;
    private final PreferenceTagRepository preferenceTagRepository;
    private final PreferenceTagConverter preferenceTagConverter;
    private final PreferenceVehicleConverter preferenceVehicleConverter;
    private final PreferenceVehicleRepository preferenceVehicleRepository;
    private final Utility utility;
    private static final String USER_NOT_FOUND = "User not found";
    private static final String USERNAME_ALREADY_USED = "Username already in use.";

    public UserService(
            UserRepository userRepository,
            PreferenceTagRepository preferenceTagRepository,
            PreferenceTagConverter preferenceTagConverter,
            PreferenceVehicleConverter preferenceVehicleConverter,
            PreferenceVehicleRepository preferenceVehicleRepository,
            Utility utility
    ) {
        this.userRepository = userRepository;
        this.filePath = System.getProperty("IMAGE_PATH");
        this.preferenceTagRepository = preferenceTagRepository;
        this.preferenceTagConverter = preferenceTagConverter;
        this.preferenceVehicleConverter = preferenceVehicleConverter;
        this.preferenceVehicleRepository = preferenceVehicleRepository;
        this.utility = utility;
    }

    public void save(UserProfile userProfile) {
        if (this.userRepository.existsByUsername(userProfile.getUsername())) {
            throw new UniqueFieldException(USERNAME_ALREADY_USED);
        }
        userRepository.save(userProfile);
    }

    public Optional<UserProfile> findById(Long id) {
        return this.userRepository.findById(id);
    }

    public Optional<UserProfile> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<UserProfile> getUsers(String username, Pageable pageable) {
        if (username == null || username.isEmpty()) {
            return userRepository.findAll(pageable).getContent();
        }
        else {
            return userRepository.findByUsernameContainingIgnoreCase(username, pageable).getContent();
        }
    }


    public void update(UserProfile userProfile, Long id) {
        Optional<UserProfile> userFound = this.userRepository.findById(id);
        if(userFound.isEmpty()) {
            throw new EntryNotFoundException(USER_NOT_FOUND);
        }
        List<UserProfile> alreadyExistingUsers = findUsersWithSameUsernameButDifferentId(userProfile.getUsername(), id);
        if(!alreadyExistingUsers.isEmpty()) {
            throw new UniqueFieldException(USERNAME_ALREADY_USED);
        }
        else{
            UserProfile user = userFound.get();
            utility.updateIfNotNull(userProfile.getUsername(), user::setUsername);
            utility.updateIfNotNull(userProfile.getPassword(), user::setPassword);
            utility.updateIfNotNull(userProfile.getAbout(), user::setAbout);
            utility.updateIfNotNull(userProfile.getCity(), user::setCity);
            utility.updateIfNotNull(userProfile.getEmail(), user::setEmail);
            utility.updateIfNotNull(userProfile.getDateOfBirth(), user::setDateOfBirth);
            utility.updateIfNotNull(userProfile.getFirstName(), user::setFirstName);
            utility.updateIfNotNull(userProfile.getLastName(), user::setLastName);
            userRepository.save(user);
        }
    }

    public void deleteById(Long id) {
        Optional<UserProfile> userFound = this.userRepository.findById(id);
        if(userFound.isPresent()) {
            userRepository.delete(userFound.get());
        }
        else{
            throw new EntryNotFoundException(USER_NOT_FOUND);
        }
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public String hashPassword(String plainPassword) {
        return HashUtil.hashPassword(plainPassword);
    }

    /**
     * @param plainPassword plain password
     * @param storedHash hashed password extracted from the database
     * @return true if the hash of the plainPassword is equal to the value of storedHash
     */
    public boolean verifyPassword(String plainPassword, String storedHash) {
        return HashUtil.verifyPassword(plainPassword, storedHash);
    }

    /**
     * Used to verify if a given username is already in use by a different user
     * @param username new username chosen by the already existing user
     * @param id id of the user who chose the new username
     * @return list of entity with the same username but different id
     */
    private List<UserProfile> findUsersWithSameUsernameButDifferentId(String username, Long id) {
        return userRepository.findByUsernameAndDifferentId(username, id);
    }

    public void deleteUserFolder(Long id){
        File folder = new File(filePath + "/" + id);
        if (folder.exists() && folder.isDirectory()) {
            try {
                deleteDirectory(folder);
            }
            catch (IOException e) {
                throw new RuntimeException("Error deleting user folder");
            }
        }
        else{
            throw new EntryNotFoundException("Folder not found");
        }
    }

    private void deleteDirectory(File directory) throws IOException {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
        if (!directory.delete()) {
            throw new IOException("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }


    @Transactional
    public void addPreferenceTagToUserProfile(Long userProfileId, List<PreferenceTagDTO> preferenceTagDtos) {
        Optional<UserProfile> userProfileOptional = userRepository.findById(userProfileId);
        if (userProfileOptional.isEmpty()){
            throw new EntryNotFoundException(USER_NOT_FOUND);
        }
        for (PreferenceTagDTO preferenceTagDto : preferenceTagDtos) {
            Optional<PreferenceTag> preferenceTagOptional = preferenceTagRepository.findByName(preferenceTagDto.getName());
            if (preferenceTagOptional.isPresent()) {
                UserProfile userProfile = userProfileOptional.get();
                PreferenceTag preferenceTag = preferenceTagOptional.get();
                userProfile.getTags().add(preferenceTag);
                userRepository.save(userProfile);
            }
            else {
                throw new EntryNotFoundException("Preference not found");
            }
        }
    }


    public List<PreferenceTagDTO> getAllUserPreferenceTags(Long userProfileId) {
        UserProfile userProfile = userRepository.findById(userProfileId).orElseThrow(
                () -> new EntryNotFoundException(USER_NOT_FOUND)
        );
        List<PreferenceTagDTO> preferenceTagDTOs = new ArrayList<>();
        for (PreferenceTag preferenceTag : userProfile.getTags()) {
            PreferenceTagDTO tagDTO = preferenceTagConverter.toDto(preferenceTag);
            preferenceTagDTOs.add(tagDTO);
        }
        return preferenceTagDTOs;
    }


    public void removePreferenceTagFromUser(Long userProfileId) {
        userRepository.findById(userProfileId).orElseThrow(
                () -> new EntryNotFoundException(USER_NOT_FOUND)
        );
        userRepository.deleteTagsByUserId(userProfileId);
    }



    @Transactional
    public void addPreferenceVehiclesToUserProfile(Long userProfileId, List<PreferenceVehicleDTO> preferenceVehicleDTOs) {
        Optional<UserProfile> userProfileOptional = userRepository.findById(userProfileId);
        if (userProfileOptional.isEmpty()){
            throw new EntryNotFoundException(USER_NOT_FOUND);
        }
        for (PreferenceVehicleDTO preferenceVehicleDTO : preferenceVehicleDTOs) {
            Optional<PreferenceVehicle> preferenceVehicleOptional = preferenceVehicleRepository.findByName(preferenceVehicleDTO.getName());
            if (preferenceVehicleOptional.isPresent()) {
                UserProfile userProfile = userProfileOptional.get();
                PreferenceVehicle preferenceVehicle = preferenceVehicleOptional.get();
                userProfile.getVehicles().add(preferenceVehicle);
                userRepository.save(userProfile);
            }
            else {
                throw new EntryNotFoundException("Preference not found");
            }
        }
    }


    public List<PreferenceVehicleDTO> getAllUserPreferenceVehicles(Long userProfileId) {
        UserProfile userProfile = userRepository.findById(userProfileId).orElseThrow(
                () -> new EntryNotFoundException(USER_NOT_FOUND)
        );
        List<PreferenceVehicleDTO> preferenceVehicleDTOs = new ArrayList<>();
        for (PreferenceVehicle preferenceVehicle : userProfile.getVehicles()) {
            PreferenceVehicleDTO vehicleDTO = preferenceVehicleConverter.toDto(preferenceVehicle);
            preferenceVehicleDTOs.add(vehicleDTO);
        }
        return preferenceVehicleDTOs;
    }

    public void removePreferenceVehiclesFromUser(Long userProfileId) {
        userRepository.findById(userProfileId).orElseThrow(
                () -> new EntryNotFoundException(USER_NOT_FOUND)
        );
        userRepository.deleteVehiclesByUserId(userProfileId);
    }
}
