package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des machines industrielles
 * Fournit toutes les opérations CRUD et de recherche pour les machines
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MachineService {
    
    private final MachineRepository machineRepository;
    
    /**
     * Crée une nouvelle machine
     */
    public Machine createMachine(Machine machine) {
        log.info("Création d'une nouvelle machine: {}", machine.getNom());
        return machineRepository.save(machine);
    }
    
    /**
     * Sauvegarde une machine (create ou update)
     */
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }
    
    /**
     * Récupère toutes les machines
     */
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }
    
    /**
     * Récupère toutes les machines (alias pour compatibilité)
     */
    public List<Machine> findAll() {
        return machineRepository.findAll();
    }
    
    /**
     * Récupère une machine par son ID
     */
    public Optional<Machine> getMachineById(String id) {
        return machineRepository.findById(id);
    }
    
    /**
     * Récupère une machine par son ID (alias pour compatibilité)
     */
    public Optional<Machine> findById(String id) {
        return machineRepository.findById(id);
    }
    
    /**
     * Met à jour une machine existante
     */
    public Machine updateMachine(String id, Machine machineDetails) {
        log.info("Mise à jour de la machine avec ID: {}", id);
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found with id: " + id));
        
        machine.setNom(machineDetails.getNom());
        machine.setDescription(machineDetails.getDescription());
        machine.setLocalisation(machineDetails.getLocalisation());
        
        return machineRepository.save(machine);
    }
    
    /**
     * Supprime une machine par son ID
     */
    public void deleteMachine(String id) {
        log.info("Suppression de la machine avec ID: {}", id);
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found with id: " + id));
        machineRepository.delete(machine);
    }
    
    /**
     * Supprime une machine par son ID (alias pour compatibilité)
     */
    public void deleteById(String id) {
        log.info("Suppression de la machine avec ID: {}", id);
        machineRepository.deleteById(id);
    }
    
    /**
     * Recherche des machines par localisation
     */
    public List<Machine> getMachinesByLocation(String localisation) {
        log.debug("Recherche de machines par localisation: {}", localisation);
        return machineRepository.findByLocalisation(localisation);
    }
    
    /**
     * Recherche des machines par nom (recherche partielle insensible à la casse)
     */
    public List<Machine> getMachinesByNom(String nom) {
        log.debug("Recherche de machines par nom: {}", nom);
        return machineRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Vérifie si une machine existe par son ID
     */
    public boolean existsById(String id) {
        return machineRepository.existsById(id);
    }
    
    /**
     * Compte le nombre total de machines
     */
    public long countMachines() {
        return machineRepository.count();
    }
    
    /**
     * Recherche des machines par type (si le champ type existe dans l'entité)
     */
    public List<Machine> getMachinesByType(String type) {
        log.debug("Recherche de machines par type: {}", type);
        // Cette méthode nécessiterait l'ajout d'une méthode dans le repository
        // Pour l'instant, on fait une recherche par nom qui contient le type
        return machineRepository.findByNomContainingIgnoreCase(type);
    }
    
    /**
     * Récupère les machines actives (si un champ status existe)
     */
    public List<Machine> getActiveMachines() {
        log.debug("Récupération des machines actives");
        // Pour l'instant, on retourne toutes les machines
        // Cette méthode pourrait être améliorée avec un champ status dans l'entité
        return machineRepository.findAll();
    }
    
    /**
     * Vérifie si une machine est disponible pour diagnostic
     */
    public boolean isMachineAvailableForDiagnostic(String machineId) {
        Optional<Machine> machine = findById(machineId);
        if (machine.isEmpty()) {
            log.warn("Machine non trouvée pour diagnostic: {}", machineId);
            return false;
        }
        
        // Logique métier pour déterminer si la machine est disponible
        // Pour l'instant, on considère que toutes les machines existantes sont disponibles
        log.debug("Machine {} disponible pour diagnostic", machine.get().getNom());
        return true;
    }
    
    /**
     * Récupère des statistiques basiques sur les machines
     */
    public MachineStats getMachineStats() {
        long totalMachines = countMachines();
        List<Machine> allMachines = findAll();
        
        // Compter par localisation
        long uniqueLocations = allMachines.stream()
                .map(Machine::getLocalisation)
                .distinct()
                .count();
        
        return new MachineStats(totalMachines, uniqueLocations);
    }
    
    /**
     * Classe interne pour les statistiques des machines
     */
    public static class MachineStats {
        private final long totalMachines;
        private final long uniqueLocations;
        
        public MachineStats(long totalMachines, long uniqueLocations) {
            this.totalMachines = totalMachines;
            this.uniqueLocations = uniqueLocations;
        }
        
        public long getTotalMachines() {
            return totalMachines;
        }
        
        public long getUniqueLocations() {
            return uniqueLocations;
        }
        
        @Override
        public String toString() {
            return String.format("MachineStats{totalMachines=%d, uniqueLocations=%d}", 
                    totalMachines, uniqueLocations);
        }
    }
}