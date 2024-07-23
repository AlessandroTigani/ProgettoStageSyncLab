package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.*;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.service.UserService;
import com.synclab.triphippie.util.JwtUtil;
import com.synclab.triphippie.util.UserConverter;
import org.apache.catalina.User;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final JwtUtil jwtUtil;
    private final String filePath;

    public UserController(
        UserService userService,
        UserConverter userConverter,
        JwtUtil jwtUtil
    ) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.jwtUtil = jwtUtil;
        this.filePath = System.getProperty("IMAGE_PATH");
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody UserDTORequest userDTORequest) {
        UserProfile userProfile = this.userConverter.toEntity(userDTORequest);
        userProfile.setPassword(userService.hashPassword(userProfile.getPassword()));
        userService.save(userProfile);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTOResponse> getById(@PathVariable("id") Long id) {
        UserProfile userProfile = userService.findById(id);
        UserDTOResponse userProfileDTO = userConverter.toDto(userProfile);
        return ResponseEntity.ok(userProfileDTO);
    }


    @GetMapping
    public List<UserDTOResponse> getUsersWithFilters(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "userSize", defaultValue = "10") int size,
            @RequestParam(name = "username", required = false) String username) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserProfile> userProfiles = userService.getUsers(username, pageable);
        List<UserDTOResponse> userDTOResponses = new ArrayList<>();
        for(UserProfile userProfile : userProfiles) {
            userDTOResponses.add(userConverter.toDto(userProfile));
        }
        return userDTOResponses;
    }


    @PostMapping("/login")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody TokenDTO tokenDTO) {
        Optional<UserProfile> user = userService.findByUsername(tokenDTO.getUsername());
        if (
            user.isEmpty() ||
            !userService.verifyPassword(tokenDTO.getPassword(), user.get().getPassword())
        ) {
            return new ResponseEntity<>("Authentication failure", HttpStatus.BAD_REQUEST);
        }
        else {
            String token = jwtUtil.generateToken(tokenDTO.getUsername(), user.get().getId());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @RequestBody UserDTORequest userDTORequest,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        UserProfile userProfileFound = userService.findById(id);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserProfile userProfileToUpdate = userConverter.toEntity(userDTORequest);
        if(userDTORequest.getPassword() != null)
            userProfileToUpdate.setPassword(userService.hashPassword(userDTORequest.getPassword()));
        userService.update(userProfileToUpdate, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        UserProfile userProfileFound = userService.findById(id);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.userService.deleteById(id);
        this.userService.deleteUserFolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{id}/profileImage")
    public ResponseEntity<String> createProfileImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file received", HttpStatus.BAD_REQUEST);
        }
        UserProfile userProfileFound = userService.findById(id);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        File destFile = new File(this.filePath + "/" + id + "/profileImage.png");
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        file.transferTo(destFile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{id}/profileImage")
    public ResponseEntity<Resource> getProfileImage(@PathVariable("id") Long id) throws IOException {
        UserProfile userProfileFound = userService.findById(id);
        Path path = Paths.get(this.filePath + "/" + id + "/profileImage.png");
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }


    @DeleteMapping("/{id}/profileImage")
    public ResponseEntity<String> deleteProfileImage(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) throws IOException {
        UserProfile userProfileFound = userService.findById(id);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        File destFile = new File(this.filePath + "/" + id + "/profileImage.png");
        if (destFile.exists()) {
            destFile.delete();
        }
        else {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/preference-tags/{userProfileId}")
    public ResponseEntity<String> addTagsToUserProfile(
            @PathVariable Long userProfileId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Valid @RequestBody List<PreferenceTagDTO> preferenceTagDTOs
    ) {
        UserProfile userProfileFound = userService.findById(userProfileId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.addPreferenceTagToUserProfile(userProfileId, preferenceTagDTOs);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }


    @GetMapping("/preference-tags/{userId}")
    public ResponseEntity<Object> getAllUserPreferenceTagsByUserId(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ){
        UserProfile userProfileFound = userService.findById(userId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<PreferenceTagDTO> preferenceTagDTOs = userService.getAllUserPreferenceTags(userId);
        return new ResponseEntity<>(preferenceTagDTOs, HttpStatus.FOUND);
    }


    @DeleteMapping("/preference-tags/{userId}")
    public ResponseEntity<String> deletePreferenceTagFromUserProfile(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ){
        UserProfile userProfileFound = userService.findById(userId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.removePreferenceTagFromUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping("/preference-vehicles/{userProfileId}")
    public ResponseEntity<String> addVehiclesToUserProfile(
            @PathVariable Long userProfileId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Valid @RequestBody List<PreferenceVehicleDTO> preferenceVehicleDTOs
    ) {
        UserProfile userProfileFound = userService.findById(userProfileId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.addPreferenceVehiclesToUserProfile(userProfileId, preferenceVehicleDTOs);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/preference-vehicles/{userId}")
    public ResponseEntity<Object> getAllUserPreferenceVehiclesByUserId(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ){
        UserProfile userProfileFound = userService.findById(userId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<PreferenceVehicleDTO> preferenceVehicleDTOs = userService.getAllUserPreferenceVehicles(userId);
        return new ResponseEntity<>(preferenceVehicleDTOs, HttpStatus.FOUND);
    }


    @DeleteMapping("/preference-vehicles/{userId}")
    public ResponseEntity<String> deletePreferenceVehiclesFromUserProfile(
            @PathVariable("userId") Long userId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ){
        UserProfile userProfileFound = userService.findById(userId);
        if(!jwtUtil.validateToken(authorizationHeader, userProfileFound.getUsername())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.removePreferenceVehiclesFromUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
