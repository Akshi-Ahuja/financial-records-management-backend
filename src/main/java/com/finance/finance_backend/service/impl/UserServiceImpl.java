package com.finance.finance_backend.service.impl;

import com.finance.finance_backend.dto.requests.CreateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRequest;
import com.finance.finance_backend.dto.requests.UpdateUserRoleRequest;
import com.finance.finance_backend.dto.requests.UpdateUserStatusRequest;
import com.finance.finance_backend.dto.responses.UserResponse;
import com.finance.finance_backend.entity.UserEntity;
import com.finance.finance_backend.enums.Role;
import com.finance.finance_backend.enums.UserStatus;
import com.finance.finance_backend.exception.DuplicateEmailException;
import com.finance.finance_backend.exception.InvalidEmailDomainException;
import com.finance.finance_backend.exception.UserNotFoundException;
import com.finance.finance_backend.repository.UserRepository;
import com.finance.finance_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("Email already exists!");
        }
        if(!request.getEmail().toLowerCase().endsWith("@zorvyn.in")) {
            throw new InvalidEmailDomainException("Emails should end with '@zorvyn.in'!");
        }
        UserEntity newUser = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole() != null ? request.getRole() : Role.VIEWER)
                .status(UserStatus.ACTIVE)
                .build();

        UserEntity saved = userRepository.save(newUser);

        return toUserResponse(saved);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        UserEntity found = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        return toUserResponse(found);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        UserEntity found = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        if (request.getName() != null && !request.getName().isBlank()){
            found.setName(request.getName());
        }

        if(request.getEmail() != null && !request.getEmail().isBlank()){
            if (!found.getEmail().equals(request.getEmail()) &&
                    userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateEmailException("Email already exists");
            }
            found.setEmail(request.getEmail());
        }

        UserEntity updated = userRepository.save(found);

        return toUserResponse(updated);
    }

    @Override
    public UserResponse updateRole(Long userId, UpdateUserRoleRequest request) {
        UserEntity found = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!"));
        found.setRole(request.getRole());
        UserEntity updated = userRepository.save(found);
        return toUserResponse(updated);
    }

    @Override
    public UserResponse updateStatus(Long userId, UpdateUserStatusRequest request) {
        UserEntity found = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!"));
        found.setStatus(request.getStatus());
        UserEntity updated = userRepository.save(found);
        return toUserResponse(updated);
    }

    private UserResponse toUserResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
