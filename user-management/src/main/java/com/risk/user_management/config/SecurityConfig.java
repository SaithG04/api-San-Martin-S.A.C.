package com.risk.user_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desactiva CSRF (útil para desarrollo)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/users/register").permitAll()  // Permitir acceso sin autenticación a /register
                        .anyRequest().authenticated()  // Proteger todos los demás endpoints
                )
                .httpBasic(withDefaults());  // Configura HTTP Basic

        return http.build();
    }
}