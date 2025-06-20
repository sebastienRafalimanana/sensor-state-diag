package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface DiagnosticHistoryRepository extends JpaRepository<DiagnosticHistory, String> {
    
    List<DiagnosticHistory> findByMachine(Machine machine);
    
    List<DiagnosticHistory> findByDiagnosticType(String diagnosticType);
    
    List<DiagnosticHistory> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT dh FROM DiagnosticHistory dh WHERE dh.machine = :machine AND dh.timestamp BETWEEN :startDate AND :endDate")
    List<DiagnosticHistory> findByMachineAndDateRange(
            @Param("machine") Machine machine,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT dh FROM DiagnosticHistory dh WHERE dh.machine = :machine AND dh.diagnosticType = :diagnosticType")
    List<DiagnosticHistory> findByMachineAndDiagnosticType(
            @Param("machine") Machine machine,
            @Param("diagnosticType") String diagnosticType);
}
