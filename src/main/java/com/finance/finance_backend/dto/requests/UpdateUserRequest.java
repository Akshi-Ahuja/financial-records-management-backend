package com.finance.finance_backend.dto.requests;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;

    @Email(message = "Invalid Email Format!")
    private String email;
}
