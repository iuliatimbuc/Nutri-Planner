package com.login.auth.service;

import com.login.auth.dto.LoginRequestDTO;
import com.login.auth.model.User;
import com.login.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;

@Service
public class AuthServiceImp implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new NoSuchElementException("Invalid credentials");
        }

        return user;
    }
}