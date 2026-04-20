package com.example.nutriplanner.controller;


import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.dto.SaveLogRequestDTO;
import com.example.nutriplanner.service.FoodService;
import com.example.nutriplanner.service.UserService;
import com.example.nutriplanner.service.impl.DailyLogServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping({"/logs"})
@Tag(
        name = "Daily Log",
        description = "Operatii pentru jurnalul zilnic de alimente"
)
public class DailyLogController {
    private final DailyLogServiceImp dailyLogService;
    private final UserService userService;
    private final FoodService foodService;

    public DailyLogController(DailyLogServiceImp dailyLogService, UserService userService, FoodService foodService) {
        this.dailyLogService = dailyLogService;
        this.userService = userService;
        this.foodService = foodService;
    }

    @Operation(
            summary = "Obtine logurile unui user grupate pe mese, pentru o data specifica"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "200",
            description = "Loguri returnate cu succes"
    ), @ApiResponse(
            responseCode = "404",
            description = "Userul nu a fost gasit"
    )})
    @GetMapping({"/{userId}"})
    public ResponseEntity<Map<String, Object>> showUserLogs(@Parameter(description = "ID-ul userului") @PathVariable Long userId, @Parameter(description = "Data in format YYYY-MM-DD, default azi") @RequestParam(required = false) String date) {
        LocalDate logDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Map<String, Object> response = new LinkedHashMap();
        response.put("selectedUser", this.userService.getUserById(userId));
        response.put("foods", this.foodService.getAllFoods());
        response.put("mealTypes", MealType.values());
        response.put("breakfastLogs", this.dailyLogService.getLogsByMealAndDate(userId, MealType.BREAKFAST, logDate));
        response.put("lunchLogs", this.dailyLogService.getLogsByMealAndDate(userId, MealType.LUNCH, logDate));
        response.put("dinnerLogs", this.dailyLogService.getLogsByMealAndDate(userId, MealType.DINNER, logDate));
        response.put("snackLogs", this.dailyLogService.getLogsByMealAndDate(userId, MealType.SNACK, logDate));
        response.put("totalCalories", this.dailyLogService.getTotalCaloriesByDate(userId, logDate));
        response.put("totalProtein", this.dailyLogService.getTotalProteinByDate(userId, logDate));
        response.put("totalCarbs", this.dailyLogService.getTotalCarbsByDate(userId, logDate));
        response.put("totalFat", this.dailyLogService.getTotalFatByDate(userId, logDate));
        response.put("breakfastCalories", this.dailyLogService.getCaloriesByMealAndDate(userId, MealType.BREAKFAST, logDate));
        response.put("lunchCalories", this.dailyLogService.getCaloriesByMealAndDate(userId, MealType.LUNCH, logDate));
        response.put("dinnerCalories", this.dailyLogService.getCaloriesByMealAndDate(userId, MealType.DINNER, logDate));
        response.put("snackCalories", this.dailyLogService.getCaloriesByMealAndDate(userId, MealType.SNACK, logDate));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Adauga un nou log alimentar pentru un user"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "201",
            description = "Log salvat cu succes"
    ), @ApiResponse(
            responseCode = "404",
            description = "User sau aliment negasit"
    )})
    @PostMapping({"/save"})
    public ResponseEntity<String> saveLog(@RequestBody SaveLogRequestDTO request) {
        this.dailyLogService.addLog(request.getUserId(), request.getFoodId(), request.getQuantity(), request.getMealType(), request.getDate());
        return ResponseEntity.status(201).body("Log salvat cu succes");
    }

    @Operation(
            summary = "Sumar calorii pe luna"
    )
    @GetMapping({"/{userId}/monthly-summary"})
    public ResponseEntity<Map<String, Double>> getMonthlySummary(@PathVariable Long userId, @RequestParam int year, @RequestParam int month) {
        Map<LocalDate, Double> summary = this.dailyLogService.getMonthlyCaloriesSummary(userId, year, month);
        Map<String, Double> result = new LinkedHashMap();
        summary.forEach((date, cal) -> result.put(date.toString(), cal));
        return ResponseEntity.ok(result);
    }
}

