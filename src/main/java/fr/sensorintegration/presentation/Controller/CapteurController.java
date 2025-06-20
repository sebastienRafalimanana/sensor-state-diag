package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.CapteurService;
import fr.sensorintegration.business.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/capteurs")
@CrossOrigin(origins = "*")
public class CapteurController {
    
    @Autowired
    private CapteurService capteurService;
    
    @Autowired
    private MachineService machineService;
    
    @PostMapping
    public ResponseEntity<Capteur> createCapteur(@RequestBody Capteur capteur) {
        try {
            Capteur createdCapteur = capteurService.createCapteur(capteur);
            return new ResponseEntity<>(createdCapteur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Capteur>> getAllCapteurs() {
        try {
            List<Capteur> capteurs = capteurService.getAllCapteurs();
            if (capteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(capteurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Capteur> getCapteurById(@PathVariable("id") Long id) {
        try {
            Optional<Capteur> capteur = capteurService.getCapteurById(id);
            if (capteur.isPresent()) {
                return new ResponseEntity<>(capteur.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Capteur> updateCapteur(@PathVariable("id") Long id, @RequestBody Capteur capteur) {
        try {
            Capteur updatedCapteur = capteurService.updateCapteur(id, capteur);
            return new ResponseEntity<>(updatedCapteur, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCapteur(@PathVariable("id") Long id) {
        try {
            capteurService.deleteCapteur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<Capteur>> getCapteursByMachine(@PathVariable("machineId") UUID machineId) {
        try {
            List<Capteur> capteurs = capteurService.getCapteursByMachineId(machineId);
            if (capteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(capteurs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/by-type")
    public ResponseEntity<List<Capteur>> getCapteursByType(@RequestParam("type") String type) {
        try {
            List<Capteur> capteurs = capteurService.getCapteursByType(type);
            if (capteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(capteurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/by-seuil-range")
    public ResponseEntity<List<Capteur>> getCapteursBySeuilRange(
            @RequestParam("minSeuil") Float minSeuil,
            @RequestParam("maxSeuil") Float maxSeuil) {
        try {
            List<Capteur> capteurs = capteurService.getCapteursBySeuilRange(minSeuil, maxSeuil);
            if (capteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(capteurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/machine/{machineId}/type/{type}")
    public ResponseEntity<List<Capteur>> getCapteursByMachineAndType(
            @PathVariable("machineId") String machineId,
            @PathVariable("type") String type) {
        try {
            Optional<Machine> machine = machineService.getMachineById(machineId);
            if (machine.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<Capteur> capteurs = capteurService.getCapteursByMachineAndType(machine.get(), type);
            if (capteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(capteurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> capteurExists(@PathVariable("id") Long id) {
        try {
            boolean exists = capteurService.existsById(id);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getCapteurCount() {
        try {
            long count = capteurService.countCapteurs();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/count/machine/{machineId}")
    public ResponseEntity<Long> getCapteurCountByMachine(@PathVariable("machineId") String machineId) {
        try {
            Optional<Machine> machine = machineService.getMachineById(machineId);
            if (machine.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            long count = capteurService.countCapteursByMachine(machine.get());
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}