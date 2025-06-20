package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.DonneeCapteur;
import fr.sensorintegration.business.service.CapteurService;
import fr.sensorintegration.business.service.DonneeCapteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donnees-capteurs")
@CrossOrigin(origins = "*")
public class DonneeCapteurController {
    
    @Autowired
    private DonneeCapteurService donneeCapteurService;
    
    @Autowired
    private CapteurService capteurService;
    
    @PostMapping
    public ResponseEntity<DonneeCapteur> createDonneeCapteur(@RequestBody DonneeCapteur donneeCapteur) {
        try {
            DonneeCapteur createdDonnee = donneeCapteurService.createDonneeCapteur(donneeCapteur);
            return new ResponseEntity<>(createdDonnee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<DonneeCapteur>> getAllDonneesCapteurs() {
        try {
            List<DonneeCapteur> donnees = donneeCapteurService.getAllDonneesCapteurs();
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DonneeCapteur> getDonneeCapteurById(@PathVariable("id") Long id) {
        try {
            Optional<DonneeCapteur> donnee = donneeCapteurService.getDonneeCapteurById(id);
            if (donnee.isPresent()) {
                return new ResponseEntity<>(donnee.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DonneeCapteur> updateDonneeCapteur(@PathVariable("id") Long id, @RequestBody DonneeCapteur donneeCapteur) {
        try {
            DonneeCapteur updatedDonnee = donneeCapteurService.updateDonneeCapteur(id, donneeCapteur);
            return new ResponseEntity<>(updatedDonnee, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDonneeCapteur(@PathVariable("id") Long id) {
        try {
            donneeCapteurService.deleteDonneeCapteur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}")
    public ResponseEntity<List<DonneeCapteur>> getDonneesByCapteur(@PathVariable("capteurId") Long capteurId) {
        try {
            List<DonneeCapteur> donnees = donneeCapteurService.getDonneesByCapteurId(capteurId);
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/paginated")
    public ResponseEntity<Page<DonneeCapteur>> getDonneesByCapteurPaginated(
            @PathVariable("capteurId") Long capteurId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Pageable pageable = PageRequest.of(page, size);
            Page<DonneeCapteur> donnees = donneeCapteurService.getDonneesByCapteurPaginated(capteur.get(), pageable);
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<DonneeCapteur>> getDonneesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<DonneeCapteur> donnees = donneeCapteurService.getDonneesByDateRange(startDate, endDate);
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/date-range")
    public ResponseEntity<List<DonneeCapteur>> getDonneesByCapteurAndDateRange(
            @PathVariable("capteurId") Long capteurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<DonneeCapteur> donnees = donneeCapteurService.getDonneesByCapteurAndDateRange(capteur.get(), startDate, endDate);
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/valeur-range")
    public ResponseEntity<List<DonneeCapteur>> getDonneesByValeurRange(
            @RequestParam Float minValeur,
            @RequestParam Float maxValeur) {
        try {
            List<DonneeCapteur> donnees = donneeCapteurService.getDonneesByValeurRange(minValeur, maxValeur);
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/latest")
    public ResponseEntity<List<DonneeCapteur>> getLatestDonneesByCapteur(
            @PathVariable("capteurId") Long capteurId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<DonneeCapteur> donnees = donneeCapteurService.getLatestDonneesByCapteur(capteur.get(), limit);
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/latest-single")
    public ResponseEntity<DonneeCapteur> getLatestDonneeByCapteur(@PathVariable("capteurId") Long capteurId) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Optional<DonneeCapteur> donnee = donneeCapteurService.getLatestDonneeByCapteur(capteur.get());
            if (donnee.isPresent()) {
                return new ResponseEntity<>(donnee.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/average")
    public ResponseEntity<Double> getAverageValeurByCapteur(@PathVariable("capteurId") Long capteurId) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Double average = donneeCapteurService.getAverageValeurByCapteur(capteur.get());
            return new ResponseEntity<>(average, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/average/date-range")
    public ResponseEntity<Double> getAverageValeurByCapteurAndDateRange(
            @PathVariable("capteurId") Long capteurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Double average = donneeCapteurService.getAverageValeurByCapteurAndDateRange(capteur.get(), startDate, endDate);
            return new ResponseEntity<>(average, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/out-of-threshold")
    public ResponseEntity<List<DonneeCapteur>> getDonneesOutOfThreshold(@PathVariable("capteurId") Long capteurId) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<DonneeCapteur> donnees = donneeCapteurService.getDonneesOutOfThreshold(capteur.get());
            if (donnees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(donnees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/count")
    public ResponseEntity<Long> countDonneesByCapteur(@PathVariable("capteurId") Long capteurId) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            long count = donneeCapteurService.countDonneesByCapteur(capteur.get());
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/capteur/{capteurId}/count-out-of-threshold")
    public ResponseEntity<Long> countDonneesOutOfThreshold(@PathVariable("capteurId") Long capteurId) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(capteurId);
            if (capteur.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            long count = donneeCapteurService.countDonneesOutOfThreshold(capteur.get());
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}/in-threshold")
    public ResponseEntity<Boolean> isValeurInThreshold(@PathVariable("id") Long id) {
        try {
            Optional<DonneeCapteur> donnee = donneeCapteurService.getDonneeCapteurById(id);
            if (donnee.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            boolean inThreshold = donneeCapteurService.isValeurInThreshold(donnee.get());
            return new ResponseEntity<>(inThreshold, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}