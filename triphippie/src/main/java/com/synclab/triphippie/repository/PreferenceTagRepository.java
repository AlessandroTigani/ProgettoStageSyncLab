package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.PreferenceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceTagRepository extends JpaRepository<PreferenceTag, String> {
    Optional<PreferenceTag> findByName(String name);
}
