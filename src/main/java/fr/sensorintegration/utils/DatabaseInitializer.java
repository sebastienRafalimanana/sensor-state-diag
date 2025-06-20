package fr.sensorintegration.utils;

import fr.sensorintegration.business.service.DiagnosticHistoryService;
import fr.sensorintegration.business.service.MachineService;
import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
    
    private final MachineService machineService;
    private final DiagnosticHistoryService diagnosticHistoryService;
    private static final Random RANDOM = new Random();
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Vérifier si l'initialisation a déjà été effectuée
            if (isDatabaseAlreadyInitialized()) {
                log.info("Database already initialized, skipping initialization.");
                return;
            }
            
            log.info("Starting database initialization...");
            
            // Créer les machines
            List<Machine> machines = createMachines();
            
            // Créer les diagnostics
            createDiagnostics(machines);
            
            log.info("Database initialization completed!");
            log.info("Created {} machines with diagnostics", machines.size());
            
        } catch (Exception e) {
            log.error("Error during database initialization: {}", e.getMessage(), e);
            // Ne pas propager l'exception pour éviter l'arrêt de l'application
        }
    }
    
    private boolean isDatabaseAlreadyInitialized() {
        try {
            long machineCount = machineService.countMachines();
            return machineCount > 0;
        } catch (Exception e) {
            log.warn("Could not check machine count, proceeding with initialization: {}", e.getMessage());
            return false;
        }
    }
    
    private List<Machine> createMachines() {
        List<Machine> machines = new ArrayList<>();
        
        log.info("Creating machines...");
        
        try {
            // Générer 5 machines de chaque type (10 machines au total)
            for (int i = 1; i <= 5; i++) {
                // Machine de moulage
                Machine moulage = createMachineIfNotExists("Moulage par injection plastique " + i);
                if (moulage != null) {
                    machines.add(moulage);
                }
                
                // Machine de soufflage
                Machine soufflage = createMachineIfNotExists("Soufflage de bouteilles " + i);
                if (soufflage != null) {
                    machines.add(soufflage);
                }
            }
        } catch (Exception e) {
            log.error("Error creating machines: {}", e.getMessage(), e);
        }
        
        return machines;
    }
    
    private Machine createMachineIfNotExists(String machineName) {
        try {
            // Vérifier si la machine existe déjà par nom
//            Machine existingMachine = machineService.findByName(machineName);
//            if (existingMachine != null) {
//                log.info("Machine '{}' already exists, skipping creation", machineName);
//                return existingMachine;
//            }
            
            // Créer une nouvelle machine via le générateur
            Machine newMachine = DataGenerator.generateMachine();
            newMachine.setNom(machineName); // S'assurer que le nom est correct

            newMachine.setId(null);
            
            Machine savedMachine = machineService.createMachine(newMachine);
            log.info("Created machine: {}", machineName);
            return savedMachine;
            
        } catch (Exception e) {
            log.error("Error creating machine '{}': {}", machineName, e.getMessage(), e);
            return null;
        }
    }
    
    private void createDiagnostics(List<Machine> machines) {
        log.info("Creating diagnostics...");
        
        for (Machine machine : machines) {
            try {
                // Vérifier si des diagnostics existent déjà pour cette machine
                if (diagnosticHistoryService.countByMachine(machine) > 0) {
                    log.info("Diagnostics already exist for machine '{}', skipping", machine.getNom());
                    continue;
                }
                
                // Le nombre de diagnostics dépend du type de machine
                int diagnosticCount = calculateDiagnosticCount(machine);
                
                List<DiagnosticHistory> diagnostics = DataGenerator.generateDiagnosticsForMachine(machine, diagnosticCount);
                
                for (DiagnosticHistory diagnostic : diagnostics) {
                    try {
                        // Nettoyer l'ID pour éviter les conflits
                        diagnostic.setId(null);
                        diagnosticHistoryService.createDiagnosticHistory(diagnostic);
                    } catch (Exception e) {
                        log.error("Error creating diagnostic for machine '{}': {}", machine.getNom(), e.getMessage());
                    }
                }
                
                log.info("Created {} diagnostics for machine '{}'", diagnostics.size(), machine.getNom());
                
            } catch (Exception e) {
                log.error("Error creating diagnostics for machine '{}': {}", machine.getNom(), e.getMessage(), e);
            }
        }
    }
    
    private int calculateDiagnosticCount(Machine machine) {
        if (machine.getNom().startsWith("Moulage")) {
            return 8 + RANDOM.nextInt(7); // 8 à 14 diagnostics
        } else {
            return 6 + RANDOM.nextInt(9); // 6 à 14 diagnostics
        }
    }
}