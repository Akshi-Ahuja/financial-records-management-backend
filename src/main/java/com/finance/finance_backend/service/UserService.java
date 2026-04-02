package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.requests.CreateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRoleRequest;
import com.finance.finance_backend.dto.requests.UpdateUserStatusRequest;
import com.finance.finance_backend.dto.responses.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserResponse updateRole(Long userId, UpdateUserRoleRequest request);

    UserResponse updateStatus(Long userId, UpdateUserStatusRequest request);
}
