package fr.sensorintegration.business.activity;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DiagnosticActivity {
    
    @ActivityMethod
    DiagnosticHistory createDiagnostic(String diagnosticId, Machine machine, String diagnosticType, String issue, String technician);
    
    @ActivityMethod
    String analyzeDiagnostic(DiagnosticHistory diagnostic);
    
    @ActivityMethod
    DiagnosticHistory updateDiagnosticStatus(String diagnosticId, String status, String interventionDetails);
    
    @ActivityMethod
    void sendAlertNotification(Machine machine, String diagnosticType, String severity);
    
    @ActivityMethod
    boolean checkMachineStatus(Machine machine);
    
    @ActivityMethod
    void generateDiagnosticReport(String diagnosticId);
}