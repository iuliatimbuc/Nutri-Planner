package com.example.nutriplanner.controller;


import com.example.nutriplanner.dto.UserGoalsResponseDTO;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.service.impl.UserServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping({"/users"})
@Tag(
        name = "Utilizatori",
        description = "Operatii CRUD pentru utilizatori"
)
public class UserController {
    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Cauta utilizator dupa ID",
            description = "Returneaza utilizatorul cu ID-ul specificat"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "200",
            description = "Utilizator gasit"
    ), @ApiResponse(
            responseCode = "404",
            description = "Utilizatorul nu a fost gasit"
    )})
    @GetMapping({"/{id}"})
    public ResponseEntity findUserById(@Parameter(required = true,description = "ID-ul utilizatorului") @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Returneaza obiectivele nutritionale",
            description = "Returneaza obiectivele zilnice de calorii si macronutrienti ale unui utilizator"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "200",
            description = "Obiective gasite"
    ), @ApiResponse(
            responseCode = "404",
            description = "Utilizatorul nu a fost gasit"
    )})
    @GetMapping({"/goals/{id}"})
    public ResponseEntity getUserGoals(@Parameter(required = true,description = "ID-ul utilizatorului") @PathVariable Long id) {
        try {
            User user = this.userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new UserGoalsResponseDTO(user.getDailyCalorieGoal(), user.getDailyProteinGoal(), user.getDailyCarbsGoal(), user.getDailyFatGoal()));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Creaza utilizator nou",
            description = "Creaza un utilizator nou si calculeaza automat obiectivele nutritionale"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utilizator creat cu succes"
    )
    @PostMapping
    public ResponseEntity saveNewUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.createUser(user));
    }

    @Operation(
            summary = "Actualizeaza utilizator",
            description = "Actualizeaza datele unui utilizator existent si recalculeaza obiectivele"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utilizator actualizat cu succes"
    )
    @PutMapping({"/{id}"})
    public ResponseEntity updateUser(@Parameter(required = true,description = "ID-ul utilizatorului") @PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(id, user));
    }

    @Operation(
            summary = "Sterge utilizator",
            description = "Sterge un utilizator din sistem dupa ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Utilizator sters cu succes"
    )
    @DeleteMapping({"/{id}"})
    public ResponseEntity deleteUserById(@Parameter(required = true,description = "ID-ul utilizatorului") @PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Operatie reusita");
    }
}

