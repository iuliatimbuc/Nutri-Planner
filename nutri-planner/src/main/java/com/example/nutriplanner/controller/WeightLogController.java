package com.example.nutriplanner.controller;

import com.example.nutriplanner.dto.WeightLogRequestDTO;
import com.example.nutriplanner.model.WeightLog;
import com.example.nutriplanner.service.impl.WeightLogServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
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
@RequestMapping({"/weight"})
@Tag(
        name = "Weight Log",
        description = "Operatii pentru tracking-ul greutatii"
)
public class WeightLogController {
    private final WeightLogServiceImp weightLogService;

    public WeightLogController(WeightLogServiceImp weightLogService) {
        this.weightLogService = weightLogService;
    }

    @Operation(
            summary = "Adauga un log de greutate",
            description = "Salveaza greutatea de azi pentru un user"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "201",
            description = "Log adaugat cu succes"
    ), @ApiResponse(
            responseCode = "400",
            description = "Log deja existent pentru azi"
    ), @ApiResponse(
            responseCode = "404",
            description = "User negasit"
    )})
    @PostMapping({"/{userId}"})
    public ResponseEntity addWeightLog(@PathVariable Long userId, @RequestParam(required = false) String date, @RequestBody WeightLogRequestDTO request) {
        try {
            LocalDate logDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            WeightLog log = this.weightLogService.addWeightLog(userId, request.getWeightKg(), logDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(log);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Istoricul greutatii",
            description = "Returneaza toate logurile de greutate ale unui user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Istoric returnat cu succes"
    )
    @GetMapping({"/{userId}/history"})
    public ResponseEntity<List<WeightLog>> getHistory(@Parameter(description = "ID-ul userului") @PathVariable Long userId) {
        return ResponseEntity.ok(this.weightLogService.getWeightHistory(userId));
    }

    @Operation(
            summary = "Actualizeaza un log de greutate"
    )
    @ApiResponses({@ApiResponse(
            responseCode = "200",
            description = "Log actualizat"
    ), @ApiResponse(
            responseCode = "404",
            description = "Log negasit"
    )})
    @PutMapping({"/{logId}"})
    public ResponseEntity updateWeightLog(@Parameter(description = "ID-ul logului") @PathVariable Long logId, @RequestBody WeightLogRequestDTO request) {
        try {
            WeightLog updated = this.weightLogService.updateWeightLog(logId, request.getWeightKg());
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

