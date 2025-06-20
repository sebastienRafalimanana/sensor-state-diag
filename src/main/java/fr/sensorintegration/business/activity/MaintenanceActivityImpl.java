package fr.sensorintegration.business.activity;

import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.MachineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class MaintenanceActivityImpl implements MaintenanceActivity {
    
    @Autowired
    private MachineService machineService;
    
    private static final Random RANDOM = new Random();
    
    private static final List<String> MAINTENANCE_TASKS = Arrays.asList(
        "Nettoyage des filtres",
        "Lubrification des composants mobiles",
        "Vérification des connexions électriques",
        "Calibration des capteurs",
        "Inspection visuelle des composants",
        "Remplacement des pièces d'usure",
        "Test des systèmes de sécurité",
        "Mise à jour des paramètres de fonctionnement",
        "Vérification des niveaux de fluides",
        "Test de performance générale"
    );
    
    @Override
    public void performMaintenance(Machine machine) {
        try {
            System.out.println("=== DÉBUT DE LA MAINTENANCE ===");
            System.out.println("Machine: " + machine.getNom());
            System.out.println("Localisation: " + machine.getLocalisation());
            System.out.println("Heure de début: " + LocalDateTime.now());
            System.out.println("================================");
            
            // Vérifier que la machine existe
            if (!machineService.existsById(UUID.fromString(machine.getId()))) {
                throw new RuntimeException("Machine introuvable avec l'ID: " + machine.getId());
            }
            
            // Simulation des étapes de maintenance
            performMaintenanceSteps(machine);
            
            // Vérification post-maintenance
            boolean maintenanceSuccess = performPostMaintenanceCheck(machine);
            
            if (maintenanceSuccess) {
                System.out.println("=== MAINTENANCE TERMINÉE AVEC SUCCÈS ===");
                System.out.println("Machine: " + machine.getNom());
                System.out.println("Heure de fin: " + LocalDateTime.now());
                System.out.println("Statut: OPÉRATIONNELLE");
                System.out.println("=======================================");
            } else {
                System.err.println("=== MAINTENANCE TERMINÉE AVEC PROBLÈMES ===");
                System.err.println("Machine: " + machine.getNom());
                System.err.println("Statut: NÉCESSITE UNE INTERVENTION SUPPLÉMENTAIRE");
                System.err.println("===========================================");
                throw new RuntimeException("La maintenance a échoué pour la machine: " + machine.getNom());
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la maintenance de la machine " + machine.getNom() + ": " + e.getMessage());
            throw new RuntimeException("Échec de la maintenance", e);
        }
    }
    
    private void performMaintenanceSteps(Machine machine) throws InterruptedException {
        String machineType = extractMachineType(machine.getNom());
        
        // Sélectionner les tâches de maintenance appropriées
        List<String> selectedTasks = selectMaintenanceTasks(machineType);
        
        System.out.println("Exécution de " + selectedTasks.size() + " tâches de maintenance:");
        
        for (int i = 0; i < selectedTasks.size(); i++) {
            String task = selectedTasks.get(i);
            System.out.println("Étape " + (i + 1) + "/" + selectedTasks.size() + ": " + task);
            
            // Simulation du temps d'exécution de chaque tâche
            int taskDuration = RANDOM.nextInt(3000) + 1000; // Entre 1 et 4 secondes
            Thread.sleep(taskDuration);
            
            // Simulation de succès/échec de tâche (95% de succès)
            if (RANDOM.nextDouble() < 0.05) {
                System.out.println("  ⚠️  Attention: Problème mineur détecté lors de: " + task);
            } else {
                System.out.println("  ✅ Terminé: " + task);
            }
        }
    }
    
    private List<String> selectMaintenanceTasks(String machineType) {
        // Sélectionner des tâches spécifiques au type de machine
        if ("moulage".equals(machineType)) {
            return Arrays.asList(
                "Nettoyage des moules",
                "Vérification de la température de chauffe",
                "Calibration des capteurs de pression",
                "Lubrification des systèmes d'injection",
                "Test des cycles de moulage",
                "Inspection des buses d'injection"
            );
        } else if ("soufflage".equals(machineType)) {
            return Arrays.asList(
                "Nettoyage des circuits d'air",
                "Vérification des compresseurs",
                "Calibration des capteurs de pression d'air",
                "Test des systèmes de soufflage",
                "Inspection des moules de soufflage",
                "Vérification des systèmes de refroidissement"
            );
        } else {
            // Tâches génériques
            return MAINTENANCE_TASKS.subList(0, Math.min(6, MAINTENANCE_TASKS.size()));
        }
    }
    
    private String extractMachineType(String machineName) {
        String lowerName = machineName.toLowerCase();
        if (lowerName.contains("moulage")) {
            return "moulage";
        } else if (lowerName.contains("soufflage")) {
            return "soufflage";
        }
        return "general";
    }
    
    private boolean performPostMaintenanceCheck(Machine machine) throws InterruptedException {
        System.out.println("Exécution des vérifications post-maintenance...");
        
        // Simulation des tests post-maintenance
        String[] checks = {
            "Test de démarrage",
            "Vérification des paramètres opérationnels",
            "Test des systèmes de sécurité",
            "Contrôle qualité des premières productions",
            "Validation des capteurs"
        };
        
        boolean allChecksPass = true;
        
        for (String check : checks) {
            System.out.println("Vérification: " + check);
            Thread.sleep(500); // Simulation du temps de vérification
            
            // 90% de chance de succès pour chaque vérification
            if (RANDOM.nextDouble() < 0.9) {
                System.out.println("  ✅ Réussi: " + check);
            } else {
                System.out.println("  ❌ Échec: " + check);
                allChecksPass = false;
            }
        }
        
        return allChecksPass;
    }
}