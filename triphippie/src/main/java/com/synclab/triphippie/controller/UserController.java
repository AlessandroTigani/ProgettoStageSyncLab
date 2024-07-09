package com.synclab.triphippie.controller;

import com.synclab.triphippie.dto.UserDTO;
import com.synclab.triphippie.model.UserProfile;
import com.synclab.triphippie.service.UserService;
import com.synclab.triphippie.util.UserConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @PostMapping
    public ResponseEntity<UserProfile> create(@Valid @RequestBody UserDTO postDTO) {
        System.out.println(postDTO);
        UserProfile post = this.userConverter.toEntity(postDTO);
        UserProfile createdPost = userService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<UserProfile> getAllWithFilters(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "usersSize", required = true) Integer usersSize,
        @RequestParam(value = "page", required = true) Integer page
    ) {
        Maximum number of users that the endpoint can return
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}
