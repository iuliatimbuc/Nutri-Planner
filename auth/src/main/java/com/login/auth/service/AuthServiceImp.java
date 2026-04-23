package com.login.auth.service;

import com.login.auth.dto.LoginRequestDTO;
import com.login.auth.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Service
public class AuthServiceImp implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public AuthServiceImp(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = new RestTemplate();
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        // request catre backendul principal sa vedem daca exista user ul
        try {
            User user = restTemplate.getForObject(
                    "http://localhost:8080/users/email/" + loginRequestDTO.getEmail(),
                    User.class
            );

            if (user == null) {
                throw new NoSuchElementException("User not found");
            }
            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new NoSuchElementException("Invalid credentials");
            }
            return user;
        } catch (Exception e) {
            throw new NoSuchElementException("Invalid credentials");
        }
    }
}