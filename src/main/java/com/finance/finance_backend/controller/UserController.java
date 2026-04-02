package com.finance.finance_backend.controller;

import com.finance.finance_backend.dto.requests.CreateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRoleRequest;
import com.finance.finance_backend.dto.requests.UpdateUserStatusRequest;
import com.finance.finance_backend.dto.responses.UserResponse;
import com.finance.finance_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserResponse> updateRole(@PathVariable Long userId,
                                                   @Valid @RequestBody UpdateUserRoleRequest newRole){
        return ResponseEntity.ok(userService.updateRole(userId, newRole));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponse> updateStatus(@PathVariable Long userId,
                                                     @Valid @RequestBody UpdateUserStatusRequest request){
        return ResponseEntity.ok(userService.updateStatus(userId, request));
    }
}
