package com.example.nutriplanner.controller;

import com.example.nutriplanner.constants.MealType;
import com.example.nutriplanner.dto.DailyLogDTO;
import com.example.nutriplanner.dto.FoodDTO;
import com.example.nutriplanner.dto.SaveLogRequestDTO;
import com.example.nutriplanner.dto.UpdateLogRequestDTO;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.mapper.DailyLogMapper;
import com.example.nutriplanner.mapper.FoodMapper;
import com.example.nutriplanner.mapper.UserMapper;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/logs")
@Tag(name = "Daily Log", description = "Operatii pentru jurnalul zilnic de alimente")
public class DailyLogController {

    private final DailyLogServiceImp dailyLogService;
    private final UserService userService;
    private final FoodService foodService;

    public DailyLogController(DailyLogServiceImp dailyLogService, UserService userService, FoodService foodService) {
        this.dailyLogService = dailyLogService;
        this.userService = userService;
        this.foodService = foodService;
    }

    @Operation(summary = "Obtine logurile unui user grupate pe mese, pentru o data specifica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loguri returnate cu succes"),
            @ApiResponse(responseCode = "404", description = "Userul nu a fost gasit")})
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> showUserLogs(
            @Parameter(description = "ID-ul userului") @PathVariable Long userId,
            @Parameter(description = "Data in format YYYY-MM-DD, default azi") @RequestParam(required = false) String date) throws ApiExceptionResponse {

        LocalDate logDate = date != null ? LocalDate.parse(date) : LocalDate.now();

        List<FoodDTO> foods = foodService.getAllFoods()
                .stream()
                .map(FoodMapper::toDto)
                .collect(Collectors.toList());

        List<DailyLogDTO> breakfastLogs = dailyLogService.getLogsByMealAndDate(userId, MealType.BREAKFAST, logDate)
                .stream().map(DailyLogMapper::toDto).collect(Collectors.toList());

        List<DailyLogDTO> lunchLogs = dailyLogService.getLogsByMealAndDate(userId, MealType.LUNCH, logDate)
                .stream().map(DailyLogMapper::toDto).collect(Collectors.toList());

        List<DailyLogDTO> dinnerLogs = dailyLogService.getLogsByMealAndDate(userId, MealType.DINNER, logDate)
                .stream().map(DailyLogMapper::toDto).collect(Collectors.toList());

        List<DailyLogDTO> snackLogs = dailyLogService.getLogsByMealAndDate(userId, MealType.SNACK, logDate)
                .stream().map(DailyLogMapper::toDto).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("selectedUser", UserMapper.toDto(userService.getUserById(userId)));
        response.put("foods", foods);
        response.put("mealTypes", MealType.values());
        response.put("breakfastLogs", breakfastLogs);
        response.put("lunchLogs", lunchLogs);
        response.put("dinnerLogs", dinnerLogs);
        response.put("snackLogs", snackLogs);
        response.put("totalCalories", dailyLogService.getTotalCaloriesByDate(userId, logDate));
        response.put("totalProtein", dailyLogService.getTotalProteinByDate(userId, logDate));
        response.put("totalCarbs", dailyLogService.getTotalCarbsByDate(userId, logDate));
        response.put("totalFat", dailyLogService.getTotalFatByDate(userId, logDate));
        response.put("breakfastCalories", dailyLogService.getCaloriesByMealAndDate(userId, MealType.BREAKFAST, logDate));
        response.put("lunchCalories", dailyLogService.getCaloriesByMealAndDate(userId, MealType.LUNCH, logDate));
        response.put("dinnerCalories", dailyLogService.getCaloriesByMealAndDate(userId, MealType.DINNER, logDate));
        response.put("snackCalories", dailyLogService.getCaloriesByMealAndDate(userId, MealType.SNACK, logDate));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Adauga un nou log alimentar pentru un user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Log salvat cu succes"),
            @ApiResponse(responseCode = "404", description = "User sau aliment negasit")})
    @PostMapping("/save")
    public ResponseEntity<DailyLogDTO> saveLog(@RequestBody SaveLogRequestDTO request) throws ApiExceptionResponse{
        return ResponseEntity.status(201).body(
                DailyLogMapper.toDto(dailyLogService.addLog(request.getUserId(), request.getFoodId(),
                                request.getQuantity(), request.getMealType(), request.getDate())
                )
        );
    }

    @Operation(summary = "Actualizeaza cantitatea unui log")
    @PutMapping("/update")
    public ResponseEntity<DailyLogDTO> updateLog(@RequestBody UpdateLogRequestDTO request) throws ApiExceptionResponse{
        return ResponseEntity.ok(DailyLogMapper.toDto(
                        dailyLogService.updateLog(request.getLogId(), request.getQuantity(), request.getMealType())
                )
        );
    }

    @Operation(summary = "Sterge un log")
    @DeleteMapping("/delete/{logId}")
    public ResponseEntity<String> deleteLog(@PathVariable Long logId) throws ApiExceptionResponse{
        dailyLogService.deleteLog(logId);
        return ResponseEntity.ok("Log sters cu succes");
    }

    @Operation(summary = "Sumar calorii pe luna")
    @GetMapping("/{userId}/monthly-summary")
    public ResponseEntity<Map<String, Double>> getMonthlySummary(@PathVariable Long userId, @RequestParam int year, @RequestParam int month) throws ApiExceptionResponse{
        Map<LocalDate, Double> summary = dailyLogService.getMonthlyCaloriesSummary(userId, year, month);
        Map<String, Double> result = new LinkedHashMap<>();
        summary.forEach((date, cal) -> result.put(date.toString(), cal));
        return ResponseEntity.ok(result);
    }
}