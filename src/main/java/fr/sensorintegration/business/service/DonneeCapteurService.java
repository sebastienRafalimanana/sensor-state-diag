package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.DonneeCapteur;
import fr.sensorintegration.data.repository.DonneeCapteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonneeCapteurService {
    
    @Autowired
    private DonneeCapteurRepository donneeCapteurRepository;
    
    @Autowired
    private CapteurService capteurService;
    
    public DonneeCapteur createDonneeCapteur(DonneeCapteur donneeCapteur) {
        // Vérifier si la valeur est dans les seuils du capteur
        if (donneeCapteur.getCapteur() != null) {
            checkThresholds(donneeCapteur);
        }
        return donneeCapteurRepository.save(donneeCapteur);
    }
    
    public List<DonneeCapteur> getAllDonneesCapteurs() {
        return donneeCapteurRepository.findAll();
    }
    
    public Optional<DonneeCapteur> getDonneeCapteurById(Long id) {
        return donneeCapteurRepository.findById(id);
    }
    
    public DonneeCapteur updateDonneeCapteur(Long id, DonneeCapteur donneeDetails) {
        DonneeCapteur donnee = donneeCapteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DonneeCapteur not found with id: " + id));
        
        donnee.setValeur(donneeDetails.getValeur());
        donnee.setTimestamp(donneeDetails.getTimestamp());
        donnee.setCapteur(donneeDetails.getCapteur());
        
        // Vérifier les seuils après mise à jour
        checkThresholds(donnee);
        
        return donneeCapteurRepository.save(donnee);
    }
    
    public void deleteDonneeCapteur(Long id) {
        DonneeCapteur donnee = donneeCapteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DonneeCapteur not found with id: " + id));
        donneeCapteurRepository.delete(donnee);
    }
    
    public List<DonneeCapteur> getDonneesByCapteur(Capteur capteur) {
        return donneeCapteurRepository.findByCapteur(capteur);
    }
    
    public List<DonneeCapteur> getDonneesByCapteurId(Long capteurId) {
        Capteur capteur = capteurService.getCapteurById(capteurId)
                .orElseThrow(() -> new RuntimeException("Capteur not found with id: " + capteurId));
        return donneeCapteurRepository.findByCapteur(capteur);
    }
    
    public List<DonneeCapteur> getDonneesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return donneeCapteurRepository.findByTimestampBetween(startDate, endDate);
    }
    
    public List<DonneeCapteur> getDonneesByCapteurAndDateRange(Capteur capteur, LocalDateTime startDate, LocalDateTime endDate) {
        return donneeCapteurRepository.findByCapteurAndTimestampBetween(capteur, startDate, endDate);
    }
    
    public List<DonneeCapteur> getDonneesByValeurRange(Float minValeur, Float maxValeur) {
        return donneeCapteurRepository.findByValeurBetween(minValeur, maxValeur);
    }
    
    public List<DonneeCapteur> getLatestDonneesByCapteur(Capteur capteur, int limit) {
        return donneeCapteurRepository.findTopByCapteurOrderByTimestampDesc(capteur, limit);
    }
    
    public Page<DonneeCapteur> getDonneesByCapteurPaginated(Capteur capteur, Pageable pageable) {
        return donneeCapteurRepository.findByCapteur(capteur, pageable);
    }
    
    public Double getAverageValeurByCapteur(Capteur capteur) {
        return donneeCapteurRepository.findAverageValeurByCapteur(capteur);
    }
    
    public Double getAverageValeurByCapteurAndDateRange(Capteur capteur, LocalDateTime startDate, LocalDateTime endDate) {
        return donneeCapteurRepository.findAverageValeurByCapteurAndDateRange(capteur, startDate, endDate);
    }
    
    public Optional<DonneeCapteur> getLatestDonneeByCapteur(Capteur capteur) {
        return donneeCapteurRepository.findTopByCapteurOrderByTimestampDesc(capteur);
    }
    
    public List<DonneeCapteur> getDonneesOutOfThreshold(Capteur capteur) {
        return donneeCapteurRepository.findDonneesOutOfThreshold(capteur);
    }
    
    public long countDonneesByCapteur(Capteur capteur) {
        return donneeCapteurRepository.countByCapteur(capteur);
    }
    
    public long countDonneesOutOfThreshold(Capteur capteur) {
        return donneeCapteurRepository.countDonneesOutOfThreshold(capteur);
    }
    
    private void checkThresholds(DonneeCapteur donnee) {
        Capteur capteur = donnee.getCapteur();
        Float valeur = donnee.getValeur();
        
        if (capteur.getSeuilMin() != null && valeur < capteur.getSeuilMin()) {
            // Log ou déclencher une alerte pour valeur trop basse
            System.out.println("ALERTE: Valeur " + valeur + " en dessous du seuil minimum " + capteur.getSeuilMin() + " pour le capteur " + capteur.getId());
        }
        
        if (capteur.getSeuilMax() != null && valeur > capteur.getSeuilMax()) {
            // Log ou déclencher une alerte pour valeur trop haute
            System.out.println("ALERTE: Valeur " + valeur + " au dessus du seuil maximum " + capteur.getSeuilMax() + " pour le capteur " + capteur.getId());
        }
    }
    
    public boolean isValeurInThreshold(DonneeCapteur donnee) {
        Capteur capteur = donnee.getCapteur();
        Float valeur = donnee.getValeur();
        
        boolean inRange = true;
        if (capteur.getSeuilMin() != null && valeur < capteur.getSeuilMin()) {
            inRange = false;
        }
        if (capteur.getSeuilMax() != null && valeur > capteur.getSeuilMax()) {
            inRange = false;
        }
        
        return inRange;
    }
}