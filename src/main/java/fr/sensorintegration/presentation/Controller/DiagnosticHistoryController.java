package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.service.DiagnosticHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticHistoryController {
    
    @Autowired
    private DiagnosticHistoryService diagnosticHistoryService;
    
    @PostMapping
    public ResponseEntity<DiagnosticHistory> createDiagnosticHistory(@RequestBody DiagnosticHistory diagnosticHistory) {
        return ResponseEntity.ok(diagnosticHistoryService.createDiagnosticHistory(diagnosticHistory));
    }
    
    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachine(@PathVariable String machineId) {
        // TODO: Add machine service to fetch machine by ID
        return ResponseEntity.ok(diagnosticHistoryService.findByMachine(null));
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
            @PathVariable String machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        // TODO: Add machine service to fetch machine by ID
        return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDateRange(null, startDate, endDate));
    }
    
    @GetMapping("/machine/{machineId}/type/{diagnosticType}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachineAndDiagnosticType(
            @PathVariable String machineId,
            @PathVariable String diagnosticType) {
        // TODO: Add machine service to fetch machine by ID
        return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDiagnosticType(null, diagnosticType));
    }
}
