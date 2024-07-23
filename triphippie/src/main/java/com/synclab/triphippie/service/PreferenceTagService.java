package com.synclab.triphippie.service;

import com.synclab.triphippie.exception.EntryNotFoundException;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceTagService {
    private final PreferenceTagRepository preferenceTagRepository;

    public PreferenceTagService(PreferenceTagRepository preferenceTagRepository) {
        this.preferenceTagRepository = preferenceTagRepository;
    }


    public String[] findAll() {
        List<PreferenceTag> preferenceTags = this.preferenceTagRepository.findAll();
        String[] tags = new String[preferenceTags.size()];
        for (int i = 0; i < preferenceTags.size(); i++) {
            tags[i] = preferenceTags.get(i).getName();
        }
        return tags;
    }

}
