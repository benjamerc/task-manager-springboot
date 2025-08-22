package com.benjamerc.taskmanager.controller;

import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPatchDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPostDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import com.benjamerc.taskmanager.mapper.UserMapper;
import com.benjamerc.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserPostDTO dto) {
        UserEntity savedUser = userService.save(userMapper.fromPostDto(dto));
        return ResponseEntity
                .created(URI.create("/users/" + savedUser.getId()))
                .body(userMapper.toDto(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity
                .ok(userDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        UserEntity user = userService.findById(id);
        return ResponseEntity
                .ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> fullUpdateUser(@PathVariable("id") Long id, @Valid @RequestBody UserPutDTO dto) {
        UserEntity updatedUser = userService.fullUpdate(id, userMapper.fromPutDto(dto));
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable("id") Long id, @Valid @RequestBody UserPatchDTO dto) {
        UserEntity updatedUser = userService.partialUpdate(id, userMapper.fromPatchDto(dto));
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}