package com.finance.finance_backend.dto.requests;

import com.finance.finance_backend.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {

    @NotNull(message = "Status is required!")
    private UserStatus status;

}
