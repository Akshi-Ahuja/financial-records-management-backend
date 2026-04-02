package com.finance.finance_backend.dto.requests;

import com.finance.finance_backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Name is Required")
    private String name;

    @Email(message = "Invalid Email Format!")
    @NotBlank(message = "Email is Required!")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Role role;
}
