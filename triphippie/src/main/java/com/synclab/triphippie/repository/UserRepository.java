package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Integer> {

    boolean existsByUsername(String username);

    Optional<UserProfile> findByUsername(String username);

    @Query("SELECT u FROM UserProfile u WHERE u.username = :username AND u.id <> :id")
    List<UserProfile> findByUsernameAndDifferentId(@Param("username") String username, @Param("id") Integer id);
}
