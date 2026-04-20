package com.example.nutriplanner.controller;



import com.example.nutriplanner.dto.LoginRequestDTO;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping({"/auth"})
@Tag(
        name = "Authentication",
        description = "Operatii de autentificare"
)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login user",
            description = "Autentifica un user pe baza credentialelor si returneaza datele acestuia"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "200",
            description = "Autentificare reusita"
    ), @ApiResponse(
            responseCode = "401",
            description = "Credentiale invalide"
    )})
    @PostMapping({"/login"})
    public ResponseEntity login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            User user = this.authService.login(loginRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (NoSuchElementException var3) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
