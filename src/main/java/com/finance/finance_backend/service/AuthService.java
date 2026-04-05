package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.requests.LoginRequest;
import com.finance.finance_backend.dto.responses.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
