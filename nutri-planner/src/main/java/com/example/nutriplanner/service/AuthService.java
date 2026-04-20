package com.example.nutriplanner.service;

import com.example.nutriplanner.dto.LoginRequestDTO;
import com.example.nutriplanner.model.User;

public interface AuthService {
    User login(LoginRequestDTO loginRequestDTO);
}
