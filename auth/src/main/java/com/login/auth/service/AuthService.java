package com.login.auth.service;

import com.login.auth.dto.LoginRequestDTO;
import com.login.auth.model.User;

public interface AuthService {
    User login(LoginRequestDTO loginRequestDTO);
}

