package com.finance.finance_backend.config;

import com.finance.finance_backend.entity.UserEntity;
import com.finance.finance_backend.enums.Role;
import com.finance.finance_backend.enums.UserStatus;
import com.finance.finance_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            UserEntity admin = UserEntity.builder()
                    .name("Default Admin")
                    .email("admin@zorvyn.in")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("Default admin user created successfully!");
        }
    }
}
