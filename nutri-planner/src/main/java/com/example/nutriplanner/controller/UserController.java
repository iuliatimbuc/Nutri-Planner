package com.example.nutriplanner.controller;

import com.example.nutriplanner.dto.UserCreationDTO;
import com.example.nutriplanner.dto.UserDTO;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.mapper.UserMapper;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.service.impl.UserServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping({"/users"})
@Tag(name = "Utilizatori", description = "Operatii CRUD pentru utilizatori")
public class UserController {
    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    @Operation(summary = "Creaza utilizator nou", description = "Creaza un utilizator nou si calculeaza automat obiectivele nutritionale")
    @ApiResponse(responseCode = "200", description = "Utilizator creat cu succes")
    @PostMapping
    public ResponseEntity<UserDTO> saveNewUser(@RequestBody UserCreationDTO dto) {
        User user = UserMapper.toCreationEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(userService.createUser(user)));
    }

    @Operation(summary = "Cauta utilizator dupa ID", description = "Returneaza utilizatorul cu ID-ul specificat")
    @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Utilizator gasit"),
                @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost gasit")})
    @GetMapping({"/{id}"})
    public ResponseEntity findUserById(@Parameter(required = true,description = "ID-ul utilizatorului") @PathVariable Long id) throws ApiExceptionResponse {
        return ResponseEntity.ok(UserMapper.toDto(userService.getUserById(id)));
    }

    @Operation(summary = "Obtine un user dupa email", description = "Cauta un user in baza de date dupa adresa de email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User gasit cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit")
    })
    @GetMapping("/email/{email:.+}")
    public ResponseEntity getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}

