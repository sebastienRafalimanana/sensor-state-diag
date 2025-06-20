package fr.sensorintegration.utils;

import fr.sensorintegration.business.service.DiagnosticHistoryService;
import fr.sensorintegration.business.service.MachineService;
import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    
    private final MachineService machineService;
    private final DiagnosticHistoryService diagnosticHistoryService;
    private static final Random RANDOM = new Random();
    
    @Override
    public void run(String... args) throws Exception {
        // Générer 5 machines de chaque type (10 machines au total)
        List<Machine> machines = new ArrayList<>();
        
        System.out.println("Creating machines...");
        for (int i = 0; i < 5; i++) {
            // Machine de moulage
            Machine moulage = DataGenerator.generateMachine();
            machineService.createMachine(moulage);
            machines.add(moulage);
            
            // Machine de soufflage
            Machine soufflage = DataGenerator.generateMachine();
            machineService.createMachine(soufflage);
            machines.add(soufflage);
        }
        
        // Pour chaque machine, générer des diagnostics réalistes
        System.out.println("Creating diagnostics...");
        for (Machine machine : machines) {
            // Le nombre de diagnostics dépend du type de machine
            int diagnosticCount;
            if (machine.getNom().startsWith("Moulage")) {
                diagnosticCount = 8 + RANDOM.nextInt(7); // 8 à 14 diagnostics
            } else {
                diagnosticCount = 6 + RANDOM.nextInt(9); // 6 à 14 diagnostics
            }
            
            List<DiagnosticHistory> diagnostics = DataGenerator.generateDiagnosticsForMachine(machine, diagnosticCount);
            
            for (DiagnosticHistory diagnostic : diagnostics) {
                diagnosticHistoryService.createDiagnosticHistory(diagnostic);
            }
        }
        
        System.out.println("Database initialization completed!");
        System.out.println("Created " + machines.size() + " machines");
        System.out.println("Created diagnostics for each machine");
    }
}
