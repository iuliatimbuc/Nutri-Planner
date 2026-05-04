package com.login.auth.controller;

import com.login.auth.dto.LoginRequestDTO;
import com.login.auth.dto.UserDTO;
import com.login.auth.model.User;
import com.login.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping({"/auth"})
@Tag(name = "Authentication", description = "Operatii de autentificare")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            User user = authService.login(loginRequestDTO);
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
