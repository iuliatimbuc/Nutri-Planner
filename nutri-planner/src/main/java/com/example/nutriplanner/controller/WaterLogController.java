package com.example.nutriplanner.controller;


import com.example.nutriplanner.dto.WaterLogRequestDTO;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.mapper.WaterLogMapper;
import com.example.nutriplanner.model.WaterLog;
import com.example.nutriplanner.service.impl.WaterLogServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping({"/water"})
@Tag(name = "Water Log", description = "Operatii pentru tracking-ul consumului de apa")

public class WaterLogController {
    private final WaterLogServiceImp waterLogService;

    public WaterLogController(WaterLogServiceImp waterLogService) {
        this.waterLogService = waterLogService;
    }

    @Operation(summary = "Total apa consumata azi",description = "Returneaza totalul in ml consumat azi de un user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total returnat cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit")})
    @GetMapping({"/{userId}/today"})
    public ResponseEntity getTodayTotal(@PathVariable Long userId, @RequestParam(required = false) String date) throws ApiExceptionResponse {
        LocalDate logDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(waterLogService.getTotalByDate(userId, logDate));

    }

    @Operation(summary = "Adauga un log de apa", description = "Inregistreaza un consum de apa pentru un user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Log adaugat cu succes"),
            @ApiResponse(responseCode = "400", description = "Amount invalid"),
            @ApiResponse(responseCode = "404", description = "User negasit")})
    @PostMapping({"/{userId}"})
    public ResponseEntity addWaterLog(@PathVariable Long userId, @RequestBody WaterLogRequestDTO request) throws ApiExceptionResponse{
        WaterLog log = waterLogService.addWaterLog(userId, request.getAmountMl(), request.getDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(WaterLogMapper.toDto(log));
    }

    @Operation(summary = "Updateaza log de apa", description = "Aduna sau scade apa din log-ul zilei pentru un user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log actualizat cu succes"),
            @ApiResponse(responseCode = "404", description = "User sau log negasit"),
            @ApiResponse(responseCode = "400", description = "Amount invalid")})
    @PutMapping("/{userId}")
    public ResponseEntity updateWaterLog(@PathVariable Long userId, @RequestBody WaterLogRequestDTO request) throws ApiExceptionResponse {
        WaterLog log = waterLogService.updateWaterLog(userId, request.getAmountMl(), request.getDate());
        return ResponseEntity.ok(log);
    }
}
