package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.MachineService;
import fr.sensorintegration.business.service.TemporalWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// @RestController - Temporairement désactivé pour tests
// @RequestMapping("/api/workflows")
// @CrossOrigin(origins = "*")
public class TemporalWorkflowController {
    
    // @Autowired - Temporairement désactivé
    private TemporalWorkflowService temporalWorkflowService;
    
    // @Autowired - Temporairement désactivé
    private MachineService machineService;
    
    // @PostMapping("/diagnostic/start") - Temporairement désactivé
    public ResponseEntity<Map<String, String>> startDiagnosticWorkflow(
            /* @RequestBody */ DiagnosticRequest request) {
        try {
            Optional<Machine> machine = machineService.getMachineById(String.valueOf(UUID.fromString(request.getMachineId())));
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            String workflowId = temporalWorkflowService.startDiagnosticWorkflow(
                machine.get(),
                request.getDiagnosticType(),
                request.getIssue(),
                request.getTechnician()
            );
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", "STARTED",
                "message", "Workflow de diagnostic démarré avec succès"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors du démarrage du workflow",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @PostMapping("/diagnostic/execute-sync") - Temporairement désactivé
    public ResponseEntity<DiagnosticHistory> executeDiagnosticWorkflowSync(
            /* @RequestBody */ DiagnosticRequest request) {
        try {
            Optional<Machine> machine = machineService.getMachineById(String.valueOf(UUID.fromString(request.getMachineId())));
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            DiagnosticHistory result = temporalWorkflowService.executeDiagnosticWorkflowSync(
                machine.get(),
                request.getDiagnosticType(),
                request.getIssue(),
                request.getTechnician()
            );
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // @PutMapping("/diagnostic/{workflowId}/status") - Temporairement désactivé
    public ResponseEntity<Map<String, String>> updateDiagnosticStatus(
            /* @PathVariable */ String workflowId,
            /* @RequestBody */ StatusUpdateRequest request) {
        try {
            temporalWorkflowService.updateDiagnosticStatus(
                workflowId,
                request.getStatus(),
                request.getInterventionDetails()
            );
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", "UPDATED",
                "message", "Statut du diagnostic mis à jour avec succès"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors de la mise à jour du statut",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @PostMapping("/maintenance/schedule") - Temporairement désactivé
    public ResponseEntity<Map<String, String>> scheduleMaintenanceWorkflow(
            /* @RequestBody */ MaintenanceScheduleRequest request) {
        try {
            Optional<Machine> machine = machineService.getMachineById(request.getMachineId());
            if (machine.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            String workflowId = temporalWorkflowService.scheduleMaintenanceWorkflow(
                machine.get(),
                request.getMaintenanceDate()
            );
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", "SCHEDULED",
                "message", "Maintenance programmée avec succès",
                "scheduledDate", request.getMaintenanceDate().toString()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors de la programmation de la maintenance",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @PostMapping("/diagnostic/bulk-start") - Temporairement désactivé
    public ResponseEntity<Map<String, Object>> startBulkDiagnosticWorkflow(
            /* @RequestBody */ BulkDiagnosticRequest request) {
        try {
            List<Machine> machines = request.getMachineIds().stream()
                .map(id -> {
                    try {
                        return machineService.getMachineById(id);
                    } catch (Exception e) {
                        return Optional.<Machine>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
            
            if (machines.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "Aucune machine valide trouvée",
                        "providedIds", request.getMachineIds()
                    ));
            }
            
            String bulkWorkflowId = temporalWorkflowService.startBulkDiagnosticWorkflow(
                machines,
                request.getDiagnosticType(),
                request.getTechnician()
            );
            
            return ResponseEntity.ok(Map.of(
                "bulkWorkflowId", bulkWorkflowId,
                "status", "STARTED",
                "message", "Traitement en lot démarré avec succès",
                "processedMachines", machines.size(),
                "totalRequested", request.getMachineIds().size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors du démarrage du traitement en lot",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @GetMapping("/{workflowId}/status") - Temporairement désactivé
    public ResponseEntity<Map<String, Object>> getWorkflowStatus(/* @PathVariable */ String workflowId) {
        try {
            String status = temporalWorkflowService.getWorkflowStatus(workflowId);
            boolean isRunning = temporalWorkflowService.isWorkflowRunning(workflowId);
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", status,
                "isRunning", isRunning,
                "timestamp", LocalDateTime.now()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors de la récupération du statut",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @PostMapping("/{workflowId}/cancel") - Temporairement désactivé
    public ResponseEntity<Map<String, String>> cancelWorkflow(
            /* @PathVariable */ String workflowId,
            /* @RequestBody(required = false) */ Map<String, String> requestBody) {
        try {
            String reason = requestBody != null ? requestBody.getOrDefault("reason", "Annulation demandée par l'utilisateur") 
                                                : "Annulation demandée par l'utilisateur";
            
            temporalWorkflowService.cancelWorkflow(workflowId, reason);
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", "CANCELLED",
                "message", "Workflow annulé avec succès",
                "reason", reason
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors de l'annulation du workflow",
                    "message", e.getMessage()
                ));
        }
    }
    
    // @PostMapping("/{workflowId}/terminate") - Temporairement désactivé
    public ResponseEntity<Map<String, String>> terminateWorkflow(
            /* @PathVariable */ String workflowId,
            /* @RequestBody(required = false) */ Map<String, String> requestBody) {
        try {
            String reason = requestBody != null ? requestBody.getOrDefault("reason", "Terminaison demandée par l'utilisateur") 
                                                : "Terminaison demandée par l'utilisateur";
            
            temporalWorkflowService.terminateWorkflow(workflowId, reason);
            
            return ResponseEntity.ok(Map.of(
                "workflowId", workflowId,
                "status", "TERMINATED",
                "message", "Workflow terminé avec succès",
                "reason", reason
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Erreur lors de la terminaison du workflow",
                    "message", e.getMessage()
                ));
        }
    }
    
    // Classes internes pour les requêtes
    
    public static class DiagnosticRequest {
        private String machineId;
        private String diagnosticType;
        private String issue;
        private String technician;
        
        // Getters et setters
        public String getMachineId() { return machineId; }
        public void setMachineId(String machineId) { this.machineId = machineId; }
        
        public String getDiagnosticType() { return diagnosticType; }
        public void setDiagnosticType(String diagnosticType) { this.diagnosticType = diagnosticType; }
        
        public String getIssue() { return issue; }
        public void setIssue(String issue) { this.issue = issue; }
        
        public String getTechnician() { return technician; }
        public void setTechnician(String technician) { this.technician = technician; }
    }
    
    public static class StatusUpdateRequest {
        private String status;
        private String interventionDetails;
        
        // Getters et setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getInterventionDetails() { return interventionDetails; }
        public void setInterventionDetails(String interventionDetails) { this.interventionDetails = interventionDetails; }
    }
    
    public static class MaintenanceScheduleRequest {
        private String machineId;
        private LocalDateTime maintenanceDate;
        
        // Getters et setters
        public String getMachineId() { return machineId; }
        public void setMachineId(String machineId) { this.machineId = machineId; }
        
        public LocalDateTime getMaintenanceDate() { return maintenanceDate; }
        public void setMaintenanceDate(LocalDateTime maintenanceDate) { this.maintenanceDate = maintenanceDate; }
    }
    
    public static class BulkDiagnosticRequest {
        private List<String> machineIds;
        private String diagnosticType;
        private String technician;
        
        // Getters et setters
        public List<String> getMachineIds() { return machineIds; }
        public void setMachineIds(List<String> machineIds) { this.machineIds = machineIds; }
        
        public String getDiagnosticType() { return diagnosticType; }
        public void setDiagnosticType(String diagnosticType) { this.diagnosticType = diagnosticType; }
        
        public String getTechnician() { return technician; }
        public void setTechnician(String technician) { this.technician = technician; }
    }
}