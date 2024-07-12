package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.TokenDTORequest;
import com.synclab.triphippie.dto.UserDTORequest;
import com.synclab.triphippie.dto.UserDTOResponse;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.service.UserService;
import com.synclab.triphippie.util.JwtUtil;
import com.synclab.triphippie.util.UserConverter;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<UserDTOResponse> getById(@PathVariable("id") int id) {
        UserProfile userProfile = userService.findById(id);
        UserDTOResponse userProfileDTO = userConverter.toDto(userProfile);
        return ResponseEntity.ok(userProfileDTO);
    }

    /*
    @GetMapping
    public ResponseEntity<UserProfile> getAllWithFilters(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "usersSize", required = true) Integer usersSize,
        @RequestParam(value = "page", required = true) Integer page
    ) {
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
    */

    @PostMapping("/login")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody TokenDTORequest tokenDTORequest) {
        Optional<UserProfile> user = userService.findByUsername(tokenDTORequest.getUsername());
        if (
            user.isEmpty() ||
            !userService.verifyPassword(tokenDTORequest.getPassword(), user.get().getPassword())
        ) {
            return new ResponseEntity<>("Authentication failure", HttpStatus.BAD_REQUEST);
        }
        else {
            String token = jwtUtil.generateToken(tokenDTORequest.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") int id,
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
            @PathVariable("id") int id,
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
            @PathVariable("id") int id,
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
    public ResponseEntity<Resource> getProfileImage(@PathVariable("id") int id) throws IOException {
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
            @PathVariable("id") int id,
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


}
