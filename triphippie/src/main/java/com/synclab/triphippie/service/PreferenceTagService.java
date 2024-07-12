package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreferenceTagService {
    private final PreferenceTagRepository preferenceTagRepository;

    public PreferenceTagService(PreferenceTagRepository preferenceTagRepository) {
        this.preferenceTagRepository = preferenceTagRepository;
    }

    public void save(PreferenceTag preferenceTag) {
        preferenceTagRepository.save(preferenceTag);
    }

    public PreferenceTag findById(int id) {
        Optional<PreferenceTag> preferenceTag =  this.preferenceTagRepository.findById(id);
        if (preferenceTag.isPresent()) {
            return preferenceTag.get();
        }
        else {
            throw new EntryNotFoundException("Preference Tag not found");
        }
    }

    public List<PreferenceTag> findAll() {
        return this.preferenceTagRepository.findAll();
    }

    public void update(PreferenceTag preferenceTag, int id) {
        Optional<PreferenceTag> preferenceTagFound = preferenceTagRepository.findById(id);
        if(preferenceTagFound.isEmpty()) {
            throw new EntryNotFoundException("Preference Tag not found");
        }
        else{
            PreferenceTag entity = preferenceTagFound.get();
            if (preferenceTag.getName() != null) {
                entity.setName(preferenceTag.getName());
            }
            if (preferenceTag.getDescription() != null) {
                entity.setDescription(preferenceTag.getDescription());
            }
            preferenceTagRepository.save(entity);
        }
    }

    public void deleteById(int id) {
        Optional<PreferenceTag> preferenceTag = preferenceTagRepository.findById(id);
        if(preferenceTag.isPresent()) {
            preferenceTagRepository.delete(preferenceTag.get());
        }
        else{
            throw new EntryNotFoundException("Preference Tag not found");
        }
    }
}
