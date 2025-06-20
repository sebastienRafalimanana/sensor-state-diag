package fr.sensorintegration.api.controller;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.DiagnosticHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/diagnostics")
@RequiredArgsConstructor
public class DiagnosticHistoryController {
    
    private final DiagnosticHistoryService diagnosticHistoryService;
    
    @PostMapping
    public ResponseEntity<DiagnosticHistory> createDiagnosticHistory(@RequestBody DiagnosticHistory diagnosticHistory) {
        return ResponseEntity.ok(diagnosticHistoryService.createDiagnosticHistory(diagnosticHistory));
    }
    
    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachine(@PathVariable UUID machineId) {
        Optional<Machine> machine = machineService.findById(machineId);
        if (machine.isPresent()) {
            return ResponseEntity.ok(diagnosticHistoryService.findByMachine(machine.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/type/{diagnosticType}")
    public ResponseEntity<List<DiagnosticHistory>> findByDiagnosticType(@PathVariable String diagnosticType) {
        return ResponseEntity.ok(diagnosticHistoryService.findByDiagnosticType(diagnosticType));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<DiagnosticHistory>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(diagnosticHistoryService.findByDateRange(startDate, endDate));
    }
    
    @GetMapping("/machine/{machineId}/date-range")
    public ResponseEntity<List<DiagnosticHistory>> findByMachineAndDateRange(
            @PathVariable UUID machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Optional<Machine> machine = machineService.findById(machineId);
        if (machine.isPresent()) {
            return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDateRange(machine.get(), startDate, endDate));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/machine/{machineId}/type/{diagnosticType}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachineAndDiagnosticType(
            @PathVariable UUID machineId,
            @PathVariable String diagnosticType) {
        Optional<Machine> machine = machineService.findById(machineId);
        if (machine.isPresent()) {
            return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDiagnosticType(machine.get(), diagnosticType));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
