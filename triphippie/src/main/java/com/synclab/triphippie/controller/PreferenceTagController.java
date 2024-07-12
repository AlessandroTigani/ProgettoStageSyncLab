package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.PreferenceTagDTO;
import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.model.PreferenceTag;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.repository.PreferenceTagRepository;
import com.synclab.triphippie.service.PreferenceTagService;
import com.synclab.triphippie.util.PreferenceTagConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


// Non si implementa il concetto di autorizzazione perchè occorrerebbe ampliare il concetto di utente con un utente
// amministratore
@RestController
@RequestMapping("/preferenceTag")
public class PreferenceTagController {
    private final PreferenceTagService preferenceTagService;
    private final PreferenceTagConverter preferenceTagConverter;

    public PreferenceTagController(
            PreferenceTagService preferenceTagService,
            PreferenceTagConverter preferenceTagConverter
    ) {
        this.preferenceTagService = preferenceTagService;
        this.preferenceTagConverter = preferenceTagConverter;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody PreferenceTagDTO preferenceTagDTO) {
        PreferenceTag preferenceTag = this.preferenceTagConverter.toEntity(preferenceTagDTO);
        preferenceTagService.save(preferenceTag);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PreferenceTagDTO>> getAll() {
        List<PreferenceTag> preferenceTags = this.preferenceTagService.findAll();
        List<PreferenceTagDTO> preferenceTagDTOs = new ArrayList<>();
        for (PreferenceTag preferenceTag : preferenceTags) {
            preferenceTagDTOs.add(this.preferenceTagConverter.toDto(preferenceTag));
        }
        return new ResponseEntity<>(preferenceTagDTOs, HttpStatus.FOUND);
    }

    @GetMapping("{id}")
    public ResponseEntity<PreferenceTagDTO> getById(@PathVariable Integer id) {
        PreferenceTag preferenceTag = this.preferenceTagService.findById(id);
        return new ResponseEntity<>(this.preferenceTagConverter.toDto(preferenceTag), HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @Valid @RequestBody PreferenceTagDTO preferenceTagDTO
    ) {
        PreferenceTag preferenceTag = this.preferenceTagConverter.toEntity(preferenceTagDTO);
        preferenceTagService.update(preferenceTag, id);
        return new ResponseEntity<>("Updated", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        preferenceTagService.deleteById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }
}
