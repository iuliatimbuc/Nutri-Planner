package com.login.auth.controller;

import com.login.auth.dto.LoginRequestDTO;
import com.login.auth.model.User;
import com.login.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Login user", description = "Autentifica un user pe baza credentialelor si returneaza datele acestuia")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Autentificare reusita"),
            @ApiResponse(responseCode = "401", description = "Credentiale invalide")})
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try{
            User user = this.authService.login(loginRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (NoSuchElementException var3) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
