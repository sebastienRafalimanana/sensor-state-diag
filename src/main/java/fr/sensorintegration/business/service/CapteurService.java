package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.repository.CapteurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des capteurs
 * Fournit toutes les opérations CRUD et de recherche pour les capteurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CapteurService {
    
    private final CapteurRepository capteurRepository;
    private final MachineService machineService;
    
    /**
     * Crée un nouveau capteur
     */
    public Capteur createCapteur(Capteur capteur) {
        log.info("Création d'un nouveau capteur de type: {}", capteur.getType());
        return capteurRepository.save(capteur);
    }
    
    /**
     * Sauvegarde un capteur (create ou update)
     */
    public Capteur save(Capteur capteur) {
        return capteurRepository.save(capteur);
    }
    
    /**
     * Récupère tous les capteurs
     */
    public List<Capteur> getAllCapteurs() {
        return capteurRepository.findAll();
    }
    
    /**
     * Récupère tous les capteurs (alias pour compatibilité)
     */
    public List<Capteur> findAll() {
        return capteurRepository.findAll();
    }
    
    /**
     * Récupère un capteur par son ID
     */
    public Optional<Capteur> getCapteurById(Long id) {
        return capteurRepository.findById(id);
    }
    
    /**
     * Récupère un capteur par son ID (alias pour compatibilité)
     */
    public Optional<Capteur> findById(Long id) {
        return capteurRepository.findById(id);
    }
    
    /**
     * Met à jour un capteur existant
     */
    public Capteur updateCapteur(Long id, Capteur capteurDetails) {
        log.info("Mise à jour du capteur avec ID: {}", id);
        Capteur capteur = capteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur not found with id: " + id));
        
        capteur.setType(capteurDetails.getType());
        capteur.setSeuilMin(capteurDetails.getSeuilMin());
        capteur.setSeuilMax(capteurDetails.getSeuilMax());
        capteur.setMachine(capteurDetails.getMachine());
        
        return capteurRepository.save(capteur);
    }
    
    /**
     * Supprime un capteur par son ID
     */
    public void deleteCapteur(Long id) {
        log.info("Suppression du capteur avec ID: {}", id);
        Capteur capteur = capteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur not found with id: " + id));
        capteurRepository.delete(capteur);
    }
    
    /**
     * Supprime un capteur par son ID (alias pour compatibilité)
     */
    public void deleteById(Long id) {
        log.info("Suppression du capteur avec ID: {}", id);
        capteurRepository.deleteById(id);
    }
    
    /**
     * Récupère les capteurs d'une machine
     */
    public List<Capteur> getCapteursByMachine(Machine machine) {
        log.debug("Recherche des capteurs pour la machine: {}", machine.getNom());
        return capteurRepository.findByMachine(machine);
    }
    
    /**
     * Récupère les capteurs d'une machine par son ID
     */
    public List<Capteur> getCapteursByMachineId(String machineId) {
        log.debug("Recherche des capteurs pour la machine ID: {}", machineId);
        Machine machine = machineService.getMachineById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found with id: " + machineId));
        return capteurRepository.findByMachine(machine);
    }
    
    /**
     * Récupère les capteurs par type
     */
    public List<Capteur> getCapteursByType(String type) {
        log.debug("Recherche des capteurs par type: {}", type);
        return capteurRepository.findByType(type);
    }
    
    /**
     * Récupère les capteurs dans une plage de seuils
     */
    public List<Capteur> getCapteursBySeuilRange(Float minSeuil, Float maxSeuil) {
        log.debug("Recherche des capteurs avec seuils entre {} et {}", minSeuil, maxSeuil);
        return capteurRepository.findBySeuilMinGreaterThanEqualAndSeuilMaxLessThanEqual(minSeuil, maxSeuil);
    }
    
    /**
     * Vérifie si un capteur existe par son ID
     */
    public boolean existsById(Long id) {
        return capteurRepository.existsById(id);
    }
    
    /**
     * Compte le nombre total de capteurs
     */
    public long countCapteurs() {
        return capteurRepository.count();
    }
    
    /**
     * Compte le nombre de capteurs pour une machine
     */
    public long countCapteursByMachine(Machine machine) {
        return capteurRepository.countByMachine(machine);
    }
    
    /**
     * Récupère les capteurs d'une machine par type
     */
    public List<Capteur> getCapteursByMachineAndType(Machine machine, String type) {
        log.debug("Recherche des capteurs de type {} pour la machine {}", type, machine.getNom());
        return capteurRepository.findByMachineAndType(machine, type);
    }
    
    /**
     * Vérifie si un capteur a des valeurs hors seuils
     */
    public boolean isValueOutOfThreshold(Capteur capteur, Float value) {
        if (capteur.getSeuilMin() != null && value < capteur.getSeuilMin()) {
            log.warn("Valeur {} en dessous du seuil minimum {} pour le capteur {}", 
                    value, capteur.getSeuilMin(), capteur.getId());
            return true;
        }
        if (capteur.getSeuilMax() != null && value > capteur.getSeuilMax()) {
            log.warn("Valeur {} au-dessus du seuil maximum {} pour le capteur {}", 
                    value, capteur.getSeuilMax(), capteur.getId());
            return true;
        }
        return false;
    }
    
    /**
     * Récupère les capteurs actifs (ceux qui ont des données récentes)
     */
    public List<Capteur> getActiveCapteurs() {
        log.debug("Récupération des capteurs actifs");
        // Pour l'instant, on retourne tous les capteurs
        // Cette méthode pourrait être améliorée en vérifiant les dernières données
        return capteurRepository.findAll();
    }
    
    /**
     * Récupère des statistiques basiques sur les capteurs
     */
    public CapteurStats getCapteurStats() {
        long totalCapteurs = countCapteurs();
        List<Capteur> allCapteurs = findAll();
        
        // Compter par type
        long uniqueTypes = allCapteurs.stream()
                .map(Capteur::getType)
                .distinct()
                .count();
        
        return new CapteurStats(totalCapteurs, uniqueTypes);
    }
    
    /**
     * Classe interne pour les statistiques des capteurs
     */
    public static class CapteurStats {
        private final long totalCapteurs;
        private final long uniqueTypes;
        
        public CapteurStats(long totalCapteurs, long uniqueTypes) {
            this.totalCapteurs = totalCapteurs;
            this.uniqueTypes = uniqueTypes;
        }
        
        public long getTotalCapteurs() {
            return totalCapteurs;
        }
        
        public long getUniqueTypes() {
            return uniqueTypes;
        }
        
        @Override
        public String toString() {
            return String.format("CapteurStats{totalCapteurs=%d, uniqueTypes=%d}", 
                    totalCapteurs, uniqueTypes);
        }
    }
}