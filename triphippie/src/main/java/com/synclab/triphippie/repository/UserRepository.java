package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Integer> {
    boolean existsByUsername(String username);
}
