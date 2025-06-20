package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.DiagnosticHistoryService;
import fr.sensorintegration.business.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticHistoryController {
    
    @Autowired
    private DiagnosticHistoryService diagnosticHistoryService;
    
    @Autowired
    private MachineService machineService;
    
    @PostMapping
    public ResponseEntity<DiagnosticHistory> createDiagnosticHistory(@RequestBody DiagnosticHistory diagnosticHistory) {
        return ResponseEntity.ok(diagnosticHistoryService.createDiagnosticHistory(diagnosticHistory));
    }
    
    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachine(@PathVariable String machineId) {
        try {
            Optional<Machine> machine = machineService.getMachineById(machineId);
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(diagnosticHistoryService.findByMachine(machine.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
            @PathVariable String machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Optional<Machine> machine = machineService.getMachineById(machineId);
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDateRange(machine.get(), startDate, endDate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/machine/{machineId}/type/{diagnosticType}")
    public ResponseEntity<List<DiagnosticHistory>> findByMachineAndDiagnosticType(
            @PathVariable String machineId,
            @PathVariable String diagnosticType) {
        try {
            Optional<Machine> machine = machineService.getMachineById(machineId);
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(diagnosticHistoryService.findByMachineAndDiagnosticType(machine.get(), diagnosticType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
