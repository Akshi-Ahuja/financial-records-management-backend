package com.finance.finance_backend.service.impl;

import com.finance.finance_backend.dto.requests.LoginRequest;
import com.finance.finance_backend.dto.responses.AuthResponse;
import com.finance.finance_backend.entity.UserEntity;
import com.finance.finance_backend.exception.InvalidOperationException;
import com.finance.finance_backend.repository.UserRepository;
import com.finance.finance_backend.security.CustomUserDetails;
import com.finance.finance_backend.security.CustomUserDetailsService;
import com.finance.finance_backend.security.JwtService;
import com.finance.finance_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidOperationException("User not found after authentication"));

            CustomUserDetails userDetails = new CustomUserDetails(user);

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().name());

            String token = jwtService.generateToken(extraClaims, userDetails);

            return AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();

        }catch (BadCredentialsException ex){
            throw new InvalidOperationException("Invalid email or password");
        }catch (DisabledException ex){
            throw new InvalidOperationException("Your account is inactive.");
        }
    }
}
