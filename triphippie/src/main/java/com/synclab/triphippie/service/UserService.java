package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.UniqueFieldException;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfile save(UserProfile userProfile) {
        if (this.userRepository.existsByUsername(userProfile.getUsername())) {
            throw new UniqueFieldException("Username already exists");
        }
        return userRepository.save(userProfile);
    }
}
