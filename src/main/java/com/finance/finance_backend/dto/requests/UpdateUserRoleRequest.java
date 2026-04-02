package com.finance.finance_backend.dto.requests;

import com.finance.finance_backend.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {

    @NotNull(message = "Role is Required!")
    private Role role;

}
