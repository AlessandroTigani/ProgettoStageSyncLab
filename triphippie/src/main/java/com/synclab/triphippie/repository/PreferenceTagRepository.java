package com.synclab.triphippie.repository;

import com.synclab.triphippie.model.PreferenceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceTagRepository extends JpaRepository<PreferenceTag, Integer> {
}
