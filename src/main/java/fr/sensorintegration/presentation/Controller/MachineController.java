package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/machines")
@CrossOrigin(origins = "*")
public class MachineController {
    
    @Autowired
    private MachineService machineService;
    
    @PostMapping
    public ResponseEntity<Machine> createMachine(@RequestBody Machine machine) {
        try {
            Machine createdMachine = machineService.createMachine(machine);
            return new ResponseEntity<>(createdMachine, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines() {
        try {
            List<Machine> machines = machineService.getAllMachines();
            if (machines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(machines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(@PathVariable("id") String id) {
        try {
            Optional<Machine> machine = machineService.getMachineById(id);
            if (machine.isPresent()) {
                return new ResponseEntity<>(machine.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable("id") String id, @RequestBody Machine machine) {
        try {
            Machine updatedMachine = machineService.updateMachine(id, machine);
            return new ResponseEntity<>(updatedMachine, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMachine(@PathVariable("id") String id) {
        try {
            machineService.deleteMachine(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/by-location")
    public ResponseEntity<List<Machine>> getMachinesByLocation(@RequestParam("localisation") String localisation) {
        try {
            List<Machine> machines = machineService.getMachinesByLocation(localisation);
            if (machines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(machines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/by-name")
    public ResponseEntity<List<Machine>> getMachinesByNom(@RequestParam("nom") String nom) {
        try {
            List<Machine> machines = machineService.getMachinesByNom(nom);
            if (machines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(machines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> machineExists(@PathVariable("id") String id) {
        try {
            boolean exists = machineService.existsById(id);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getMachineCount() {
        try {
            long count = machineService.countMachines();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}