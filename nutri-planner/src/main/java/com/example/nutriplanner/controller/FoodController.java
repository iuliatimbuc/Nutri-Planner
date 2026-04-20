package com.example.nutriplanner.controller;


import com.example.nutriplanner.model.Food;
import com.example.nutriplanner.service.impl.FoodServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping({"/foods"})
@Tag(
        name = "Food",
        description = "Operatii CRUD pentru alimente"
)
public class FoodController {
    private final FoodServiceImp foodService;

    public FoodController(FoodServiceImp foodService) {
        this.foodService = foodService;
    }

    @Operation(
            summary = "Returneaza lista tuturor alimentelor"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista returnata cu succes"
    )
    @GetMapping({""})
    public ResponseEntity<List<Food>> showFoods() {
        return ResponseEntity.ok(this.foodService.getAllFoods());
    }

    @Operation(
            summary = "Adauga un aliment nou"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "201",
            description = "Aliment creat cu succes"
    ), @ApiResponse(
            responseCode = "400",
            description = "Date invalide"
    )})
    @PostMapping({"/save"})
    public ResponseEntity<Food> saveFood(@RequestBody Food food) {
        Food created = this.foodService.createFood(food);
        return ResponseEntity.status(201).body(created);
    }
}

