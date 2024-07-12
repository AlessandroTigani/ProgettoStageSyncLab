package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.exception.UniqueFieldException;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.repository.UserRepository;
import com.synclab.triphippie.util.HashUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final String filePath;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.filePath = System.getProperty("IMAGE_PATH");
    }

    public void save(UserProfile userProfile) {
        if (this.userRepository.existsByUsername(userProfile.getUsername())) {
            throw new UniqueFieldException("Username already exists");
        }
        userRepository.save(userProfile);
    }

    public UserProfile findById(int id) {
        Optional<UserProfile> userFound =  this.userRepository.findById(id);
        if (userFound.isPresent()) {
            return userFound.get();
        }
        else {
            throw new EntryNotFoundException("User not found");
        }
    }

    public Optional<UserProfile> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public void update(UserProfile userProfile, int id) {
        Optional<UserProfile> userFound = this.userRepository.findById(id);
        if(userFound.isEmpty()) {
            throw new EntryNotFoundException("User not found");
        }
        List<UserProfile> alreadyExistingUsers = findUsersWithSameUsernameButDifferentId(userProfile.getUsername(), id);
        if(!alreadyExistingUsers.isEmpty()) {
            throw new UniqueFieldException("Username already in use");
        }
        else{
            UserProfile user = userFound.get();
            if (userProfile.getUsername() != null) {
                user.setUsername(userProfile.getUsername());
            }
            if (userProfile.getPassword() != null) {
                user.setPassword(userProfile.getPassword());
            }
            if (userProfile.getAbout() != null) {
                user.setAbout(userProfile.getAbout());
            }
            if (userProfile.getCity() != null) {
                user.setCity(userProfile.getCity());
            }
            if (userProfile.getEmail() != null) {
                user.setEmail(userProfile.getEmail());
            }
            if (userProfile.getDateOfBirth() != null) {
                user.setDateOfBirth(userProfile.getDateOfBirth());
            }
            if (userProfile.getFirstName() != null) {
                user.setFirstName(userProfile.getFirstName());
            }
            if (userProfile.getLastName() != null) {
                user.setLastName(userProfile.getLastName());
            }
            userRepository.save(user);
        }
    }

    public void deleteById(int id) {
        Optional<UserProfile> userFound = this.userRepository.findById(id);
        if(userFound.isPresent()) {
            userRepository.delete(userFound.get());
        }
        else{
            throw new EntryNotFoundException("User not found");
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
    public List<UserProfile> findUsersWithSameUsernameButDifferentId(String username, Integer id) {
        return userRepository.findByUsernameAndDifferentId(username, id);
    }

    public void deleteUserFolder(int id){
        File folder = new File(filePath + "/" + id);
        if (folder.exists() && folder.isDirectory()) {
            try {
                deleteDirectory(folder);
            } catch (IOException e) {
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
}
