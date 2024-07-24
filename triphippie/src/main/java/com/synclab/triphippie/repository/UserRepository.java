package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.UserProfile;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByUsername(String username);

    Optional<UserProfile> findByUsername(String username);

    @Query("SELECT u FROM UserProfile u WHERE u.username = :username AND u.id <> :id")
    List<UserProfile> findByUsernameAndDifferentId(@Param("username") String username, @Param("id") Long id);

    Page<UserProfile> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_profile_preference_tag WHERE user_profile_id = :userId", nativeQuery = true)
    void deleteTagsByUserId(@Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_profile_preference_vehicle WHERE user_profile_id = :userId", nativeQuery = true)
    void deleteVehiclesByUserId(@Param("userId") Long userId);

}
