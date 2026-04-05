package com.finance.finance_backend.config;

import com.finance.finance_backend.security.CustomAccessDeniedHandler;
import com.finance.finance_backend.security.CustomAuthenticationEntryPoint;
import com.finance.finance_backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/users/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/*/role").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/records").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/records").hasAnyRole("ADMIN","ANALYST","VIEWER")
                        .requestMatchers(HttpMethod.GET, "/records/*").hasAnyRole("ADMIN","ANALYST","VIEWER")
                        .requestMatchers(HttpMethod.PUT, "/records/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/records/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/dashboard/summary").hasAnyRole("ADMIN","ANALYST","VIEWER")
                        .requestMatchers(HttpMethod.GET,"/dashboard/recent-activity").hasAnyRole("ADMIN","ANALYST","VIEWER")
                        .requestMatchers(HttpMethod.GET,"/dashboard/category-totals").hasAnyRole("ADMIN","ANALYST")
                        .requestMatchers(HttpMethod.GET,"/dashboard/monthly-trend").hasAnyRole("ADMIN","ANALYST")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
