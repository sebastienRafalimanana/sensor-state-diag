package fr.sensorintegration.service;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.repository.DiagnosticHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiagnosticHistoryService {
    
    @Autowired
    private DiagnosticHistoryRepository diagnosticHistoryRepository;
    
    public DiagnosticHistory createDiagnosticHistory(DiagnosticHistory diagnosticHistory) {
        return diagnosticHistoryRepository.save(diagnosticHistory);
    }
    
    public List<DiagnosticHistory> findByMachine(Machine machine) {
        return diagnosticHistoryRepository.findByMachine(machine);
    }
    
    public List<DiagnosticHistory> findByDiagnosticType(String diagnosticType) {
        return diagnosticHistoryRepository.findByDiagnosticType(diagnosticType);
    }
    
    public List<DiagnosticHistory> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return diagnosticHistoryRepository.findByTimestampBetween(startDate, endDate);
    }
    
    public List<DiagnosticHistory> findByMachineAndDateRange(Machine machine, LocalDateTime startDate, LocalDateTime endDate) {
        return diagnosticHistoryRepository.findByMachineAndDateRange(machine, startDate, endDate);
    }
    
    public List<DiagnosticHistory> findByMachineAndDiagnosticType(Machine machine, String diagnosticType) {
        return diagnosticHistoryRepository.findByMachineAndDiagnosticType(machine, diagnosticType);
    }
}
