package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.repository.DiagnosticHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion de l'historique des diagnostics
 * Fournit toutes les opérations CRUD et de recherche pour les diagnostics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiagnosticHistoryService {

    private final DiagnosticHistoryRepository diagnosticHistoryRepository;
    
    /**
     * Crée un nouveau diagnostic dans l'historique
     */
    public DiagnosticHistory createDiagnosticHistory(DiagnosticHistory diagnosticHistory) {
        log.info("Création d'un nouveau diagnostic pour la machine: {}", 
                diagnosticHistory.getMachine().getNom());
        return diagnosticHistoryRepository.save(diagnosticHistory);
    }
    
    /**
     * Met à jour un diagnostic existant
     */
    public DiagnosticHistory updateDiagnosticHistory(DiagnosticHistory diagnosticHistory) {
        log.info("Mise à jour du diagnostic ID: {}", diagnosticHistory.getId());
        return diagnosticHistoryRepository.save(diagnosticHistory);
    }
    
    /**
     * Sauvegarde un diagnostic (create ou update)
     */
    public DiagnosticHistory save(DiagnosticHistory diagnosticHistory) {
        return diagnosticHistoryRepository.save(diagnosticHistory);
    }
    
    /**
     * Récupère un diagnostic par son ID
     */
    public Optional<DiagnosticHistory> findById(String id) {
        return diagnosticHistoryRepository.findById(id);
    }
    
    /**
     * Récupère tous les diagnostics
     */
    public List<DiagnosticHistory> findAll() {
        return diagnosticHistoryRepository.findAll();
    }
    
    /**
     * Récupère tous les diagnostics d'une machine
     */
    public List<DiagnosticHistory> findByMachine(Machine machine) {
        log.debug("Recherche des diagnostics pour la machine: {}", machine.getNom());
        return diagnosticHistoryRepository.findByMachine(machine);
    }
    
    /**
     * Récupère les diagnostics par type
     */
    public List<DiagnosticHistory> findByDiagnosticType(String diagnosticType) {
        log.debug("Recherche des diagnostics par type: {}", diagnosticType);
        return diagnosticHistoryRepository.findByDiagnosticType(diagnosticType);
    }
    
    /**
     * Récupère les diagnostics dans une plage de dates
     */
    public List<DiagnosticHistory> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Recherche des diagnostics entre {} et {}", startDate, endDate);
        return diagnosticHistoryRepository.findByTimestampBetween(startDate, endDate);
    }
    
    /**
     * Récupère les diagnostics d'une machine dans une plage de dates
     */
    public List<DiagnosticHistory> findByMachineAndDateRange(Machine machine, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Recherche des diagnostics pour la machine {} entre {} et {}", 
                machine.getNom(), startDate, endDate);
        return diagnosticHistoryRepository.findByMachineAndDateRange(machine, startDate, endDate);
    }
    
    /**
     * Récupère les diagnostics d'une machine par type
     */
    public List<DiagnosticHistory> findByMachineAndDiagnosticType(Machine machine, String diagnosticType) {
        log.debug("Recherche des diagnostics de type {} pour la machine {}", 
                diagnosticType, machine.getNom());
        return diagnosticHistoryRepository.findByMachineAndDiagnosticType(machine, diagnosticType);
    }
    
    /**
     * Récupère les diagnostics par statut
     */
    public List<DiagnosticHistory> findByStatus(String status) {
        log.debug("Recherche des diagnostics par statut: {}", status);
        return diagnosticHistoryRepository.findByStatus(status);
    }
    
    /**
     * Récupère les diagnostics par technicien
     */
    public List<DiagnosticHistory> findByTechnician(String technician) {
        log.debug("Recherche des diagnostics par technicien: {}", technician);
        return diagnosticHistoryRepository.findByTechnician(technician);
    }
    
    /**
     * Supprime un diagnostic par son ID
     */
    public void deleteDiagnosticHistory(String id) {
        log.info("Suppression du diagnostic ID: {}", id);
        diagnosticHistoryRepository.deleteById(id);
    }
    
    /**
     * Supprime un diagnostic par son ID (alias pour compatibilité)
     */
    public void deleteById(String id) {
        log.info("Suppression du diagnostic ID: {}", id);
        diagnosticHistoryRepository.deleteById(id);
    }
    
    /**
     * Vérifie si un diagnostic existe par son ID
     */
    public boolean existsById(String id) {
        return diagnosticHistoryRepository.existsById(id);
    }
    
    /**
     * Compte le nombre total de diagnostics
     */
    public long countDiagnostics() {
        return diagnosticHistoryRepository.count();
    }
    
    /**
     * Compte le nombre de diagnostics pour une machine
     */
    public long countByMachine(Machine machine) {
        return diagnosticHistoryRepository.countByMachine(machine);
    }
    
    /**
     * Récupère les diagnostics récents (dernières 24h)
     */
    public List<DiagnosticHistory> findRecentDiagnostics() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return findByDateRange(yesterday, LocalDateTime.now());
    }
    
    /**
     * Récupère les diagnostics critiques (statut critique ou urgent)
     */
    public List<DiagnosticHistory> findCriticalDiagnostics() {
        List<DiagnosticHistory> all = findAll();
        return all.stream()
                .filter(d -> d.getStatus() != null && 
                       (d.getStatus().toLowerCase().contains("critique") || 
                        d.getStatus().toLowerCase().contains("urgent")))
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les diagnostics en cours
     */
    public List<DiagnosticHistory> findActiveDiagnostics() {
        return findByStatus("En cours");
    }
    
    /**
     * Récupère les diagnostics terminés
     */
    public List<DiagnosticHistory> findCompletedDiagnostics() {
        List<DiagnosticHistory> completed = findByStatus("Terminé");
        completed.addAll(findByStatus("Résolu"));
        completed.addAll(findByStatus("Fermé"));
        return completed;
    }
    
    /**
     * Récupère les derniers diagnostics pour une machine
     */
    public List<DiagnosticHistory> findLatestByMachine(Machine machine, int limit) {
        List<DiagnosticHistory> machineHistory = findByMachine(machine);
        return machineHistory.stream()
                .sorted((d1, d2) -> d2.getTimestamp().compareTo(d1.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Met à jour le statut d'un diagnostic
     */
    public DiagnosticHistory updateStatus(String diagnosticId, String newStatus, String interventionDetails) {
        Optional<DiagnosticHistory> optionalDiagnostic = findById(diagnosticId);
        if (optionalDiagnostic.isPresent()) {
            DiagnosticHistory diagnostic = optionalDiagnostic.get();
            diagnostic.setStatus(newStatus);
            if (interventionDetails != null) {
                diagnostic.setInterventionDetails(interventionDetails);
            }
            diagnostic.setInterventionDate(LocalDateTime.now());
            log.info("Mise à jour du statut du diagnostic {} vers {}", diagnosticId, newStatus);
            return save(diagnostic);
        } else {
            throw new RuntimeException("Diagnostic not found with ID: " + diagnosticId);
        }
    }
    
    /**
     * Récupère des statistiques sur les diagnostics
     */
    public DiagnosticStats getDiagnosticStats() {
        long total = countDiagnostics();
        long active = findActiveDiagnostics().size();
        long completed = findCompletedDiagnostics().size();
        long critical = findCriticalDiagnostics().size();
        long recent = findRecentDiagnostics().size();
        
        return new DiagnosticStats(total, active, completed, critical, recent);
    }
    
    /**
     * Vérifie si une machine a des diagnostics en cours
     */
    public boolean hasPendingDiagnostics(Machine machine) {
        List<DiagnosticHistory> machineHistory = findByMachine(machine);
        return machineHistory.stream()
                .anyMatch(d -> d.getStatus() != null && 
                         d.getStatus().toLowerCase().contains("en cours"));
    }
    
    /**
     * Classe interne pour les statistiques des diagnostics
     */
    public static class DiagnosticStats {
        private final long totalDiagnostics;
        private final long activeDiagnostics;
        private final long completedDiagnostics;
        private final long criticalDiagnostics;
        private final long recentDiagnostics;
        
        public DiagnosticStats(long totalDiagnostics, long activeDiagnostics, 
                             long completedDiagnostics, long criticalDiagnostics, 
                             long recentDiagnostics) {
            this.totalDiagnostics = totalDiagnostics;
            this.activeDiagnostics = activeDiagnostics;
            this.completedDiagnostics = completedDiagnostics;
            this.criticalDiagnostics = criticalDiagnostics;
            this.recentDiagnostics = recentDiagnostics;
        }
        
        public long getTotalDiagnostics() { return totalDiagnostics; }
        public long getActiveDiagnostics() { return activeDiagnostics; }
        public long getCompletedDiagnostics() { return completedDiagnostics; }
        public long getCriticalDiagnostics() { return criticalDiagnostics; }
        public long getRecentDiagnostics() { return recentDiagnostics; }
        
        @Override
        public String toString() {
            return String.format("DiagnosticStats{total=%d, active=%d, completed=%d, critical=%d, recent=%d}", 
                    totalDiagnostics, activeDiagnostics, completedDiagnostics, criticalDiagnostics, recentDiagnostics);
        }
    }
}