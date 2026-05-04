package com.example.nutriplanner.controller;

import com.example.nutriplanner.dto.WeightLogDTO;
import com.example.nutriplanner.dto.WeightLogRequestDTO;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.mapper.WeightLogMapper;
import com.example.nutriplanner.model.WeightLog;
import com.example.nutriplanner.service.impl.WeightLogServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/weight")
@Tag(name = "Weight Log", description = "Operatii pentru tracking-ul greutatii")
public class WeightLogController {

    private final WeightLogServiceImp weightLogService;

    public WeightLogController(WeightLogServiceImp weightLogService) {
        this.weightLogService = weightLogService;
    }

    @Operation(summary = "Adauga un log de greutate", description = "Salveaza greutatea pentru un user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Log adaugat cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit"),
            @ApiResponse(responseCode = "400", description = "Date invalide")})
    @PostMapping("/{userId}")
    public ResponseEntity<WeightLogDTO> addWeightLog(@PathVariable Long userId, @RequestParam(required = false) String date, @RequestBody WeightLogRequestDTO request) throws ApiExceptionResponse {
        LocalDate logDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        WeightLog log = weightLogService.addWeightLog(userId, request.getWeightKg(), logDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(WeightLogMapper.toDto(log));
    }

    @Operation(summary = "Actualizeaza un log de greutate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log actualizat"),
            @ApiResponse(responseCode = "404", description = "Log negasit")})
    @PutMapping("/{logId}")
    public ResponseEntity<WeightLogDTO> updateWeightLog(@Parameter(description = "ID-ul logului") @PathVariable Long logId, @RequestBody WeightLogRequestDTO request) throws ApiExceptionResponse{
        WeightLog updated = weightLogService.updateWeightLog(logId, request.getWeightKg());
        return ResponseEntity.ok(WeightLogMapper.toDto(updated));
    }

    @Operation(summary = "Istoricul greutatii", description = "Returneaza toate logurile de greutate ale unui user")
    @ApiResponse(responseCode = "200", description = "Istoric returnat cu succes")
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WeightLogDTO>> getHistory(@Parameter(description = "ID-ul userului") @PathVariable Long userId) throws ApiExceptionResponse{
        List<WeightLogDTO> history = weightLogService.getWeightHistory(userId)
                .stream()
                .map(WeightLogMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }
}