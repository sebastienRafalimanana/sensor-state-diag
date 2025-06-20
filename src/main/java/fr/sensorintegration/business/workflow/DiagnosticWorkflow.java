package fr.sensorintegration.business.workflow;

import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.entity.DiagnosticHistory;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.time.LocalDateTime;

@WorkflowInterface
public interface DiagnosticWorkflow {
    
    @WorkflowMethod
    DiagnosticHistory handleDiagnostic(Machine machine, String diagnosticType, String issue, String technician);
    
    @WorkflowMethod
    void updateDiagnosticStatus(String workflowId, String status, String interventionDetails);
    
    @WorkflowMethod
    void scheduleMaintenance(Machine machine, LocalDateTime maintenanceDate);
}
