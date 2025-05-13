package com.basic.myspringboot.controller;

import com.basic.myspringboot.controller.dto.UserDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserServiceController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> create(@Valid @RequestBody
                                           UserDTO.UserCreateRequest request) {
        UserDTO.UserResponse createUser = userService.createUser(request);
        return ResponseEntity.ok(createUser);
    }

    @GetMapping
    public List<UserDTO.UserResponse> getUsers() {
        return userService.getAllUsers().stream()
                .map(UserDTO.UserResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO.UserResponse getUserById(@PathVariable Long id) {
        User existUser = userService.getUserById(id);
        return new UserDTO.UserResponse(existUser);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserDTO.UserResponse> updateUser(@PathVariable String email,
                                           @Valid @RequestBody UserDTO.UserUpdateRequest userDetail) {
        UserDTO.UserResponse updateUser = userService.updateUserByEmail(email, userDetail);
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/email/{email}")
    public UserDTO.UserResponse getEmailById(@PathVariable String email) {
        return new UserDTO.UserResponse(
                userService.getUserByEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
