package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.dto.LoginRequestDTO;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.service.AuthService;
import com.example.nutriplanner.service.UserService;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImp(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        User user = this.userService.getUserByEmail(loginRequestDTO.getEmail());
        if (!this.passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new NoSuchElementException("Invalid credentials");
        } else {
            return user;
        }
    }
}
