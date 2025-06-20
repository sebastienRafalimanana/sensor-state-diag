package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.utils.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// @Service - Temporairement désactivé pour tests
public class TemporalInitializationService implements CommandLineRunner {
    
    // @Autowired - Temporairement désactivé
    private MachineService machineService;
    
    // @Autowired - Temporairement désactivé
    private DiagnosticHistoryService diagnosticHistoryService;
    
    // @Autowired - Temporairement désactivé
    private TemporalWorkflowService temporalWorkflowService;
    
    private static final boolean ENABLE_AUTO_INIT = true;
    private static final int MACHINES_TO_CREATE = 6;
    private static final int DIAGNOSTICS_PER_MACHINE = 3;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== TEMPORAL INITIALIZATION TEMPORAIREMENT DÉSACTIVÉ ===");
        System.out.println("L'application démarre sans les workflows Temporal pour l'instant");
        System.out.println("Vous pouvez utiliser les APIs REST normalement");
        System.out.println("========================================================");
    }
    
    public void initializeTestData() {
        try {
            // 1. Créer des machines de test
            List<Machine> machines = createTestMachines();
            
            // 2. Créer des diagnostics classiques
            createClassicDiagnostics(machines);
            
            // 3. Démarrer des workflows Temporal
            startTemporalWorkflows(machines);
            
            // 4. Programmer des maintenances futures
            scheduleMaintenances(machines);
            
            System.out.println("Données de test initialisées avec succès !");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des données de test : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private List<Machine> createTestMachines() {
        System.out.println("Création de " + MACHINES_TO_CREATE + " machines de test...");
        
        List<Machine> testMachines = DataGenerator.generateMachinesForTesting(MACHINES_TO_CREATE);
        
        for (Machine machine : testMachines) {
            try {
                Machine savedMachine = machineService.createMachine(machine);
                System.out.println("Machine créée : " + savedMachine.getNom() + " (ID: " + savedMachine.getId() + ")");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création de la machine " + machine.getNom() + " : " + e.getMessage());
            }
        }
        
        return testMachines;
    }
    
    private void createClassicDiagnostics(List<Machine> machines) {
        System.out.println("Création de diagnostics classiques...");
        
        for (Machine machine : machines) {
            List<DiagnosticHistory> diagnostics = DataGenerator.generateDiagnosticsForMachine(machine, DIAGNOSTICS_PER_MACHINE);
            
            for (DiagnosticHistory diagnostic : diagnostics) {
                try {
                    DiagnosticHistory savedDiagnostic = diagnosticHistoryService.createDiagnosticHistory(diagnostic);
                    System.out.println("Diagnostic créé pour " + machine.getNom() + " : " + savedDiagnostic.getDiagnosticType());
                } catch (Exception e) {
                    System.err.println("Erreur lors de la création du diagnostic pour " + machine.getNom() + " : " + e.getMessage());
                }
            }
        }
    }
    
    private void startTemporalWorkflows(List<Machine> machines) {
        System.out.println("Démarrage de workflows Temporal...");
        
        List<String> scenarios = DataGenerator.generateDiagnosticScenarios();
        
        for (int i = 0; i < machines.size(); i++) {
            Machine machine = machines.get(i);
            String scenario = scenarios.get(i % scenarios.size());
            
            // Démarrage asynchrone des workflows
            CompletableFuture.runAsync(() -> {
                try {
                    String workflowId = temporalWorkflowService.startDiagnosticWorkflow(
                        machine,
                        scenario,
                        "Diagnostic automatique initialisé au démarrage de l'application",
                        "Système d'initialisation"
                    );
                    
                    System.out.println("Workflow Temporal démarré pour " + machine.getNom() + 
                                     " (Scénario: " + scenario + ", ID: " + workflowId + ")");
                    
                } catch (Exception e) {
                    System.err.println("Erreur lors du démarrage du workflow pour " + machine.getNom() + " : " + e.getMessage());
                }
            });
            
            // Petit délai pour éviter de surcharger le système
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void scheduleMaintenances(List<Machine> machines) {
        System.out.println("Programmation de maintenances futures...");
        
        for (int i = 0; i < machines.size(); i++) {
            Machine machine = machines.get(i);
            
            // Programmer une maintenance dans 1 à 7 jours
            LocalDateTime maintenanceDate = LocalDateTime.now().plusDays(i + 1);
            
            CompletableFuture.runAsync(() -> {
                try {
                    String workflowId = temporalWorkflowService.scheduleMaintenanceWorkflow(machine, maintenanceDate);
                    
                    System.out.println("Maintenance programmée pour " + machine.getNom() + 
                                     " le " + maintenanceDate + " (ID: " + workflowId + ")");
                    
                } catch (Exception e) {
                    System.err.println("Erreur lors de la programmation de maintenance pour " + machine.getNom() + " : " + e.getMessage());
                }
            });
            
            // Petit délai pour éviter de surcharger le système
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public void createBulkDiagnostics(List<String> machineIds, String diagnosticType) {
        System.out.println("Création de diagnostics en lot via Temporal...");
        
        try {
            String bulkWorkflowId = temporalWorkflowService.startBulkDiagnosticWorkflow(
                machineIds.stream()
                    .map(id -> {
                        try {
                            return machineService.getMachineById(String.valueOf(java.util.UUID.fromString(id)));
                        } catch (Exception e) {
                            return java.util.Optional.<Machine>empty();
                        }
                    })
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .toList(),
                diagnosticType,
                "Système de traitement en lot"
            );
            
            System.out.println("Traitement en lot démarré avec l'ID : " + bulkWorkflowId);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement en lot : " + e.getMessage());
        }
    }
    
    public void demonstrateWorkflowCapabilities() {
        System.out.println("=== DÉMONSTRATION DES CAPACITÉS TEMPORAL ===");
        
        try {
            // Récupérer quelques machines existantes
            List<Machine> machines = machineService.getAllMachines();
            
            if (machines.isEmpty()) {
                System.out.println("Aucune machine disponible pour la démonstration");
                return;
            }
            
            Machine firstMachine = machines.get(0);
            
            // Démonstration 1 : Workflow synchrone
            System.out.println("1. Exécution d'un workflow synchrone...");
            DiagnosticHistory syncResult = temporalWorkflowService.executeDiagnosticWorkflowSync(
                firstMachine,
                "Démonstration synchrone",
                "Test des capacités Temporal en mode synchrone",
                "Système de démonstration"
            );
            System.out.println("Résultat synchrone : " + syncResult.getId());
            
            // Démonstration 2 : Workflow asynchrone
            System.out.println("2. Démarrage d'un workflow asynchrone...");
            String asyncWorkflowId = temporalWorkflowService.startDiagnosticWorkflow(
                firstMachine,
                "Démonstration asynchrone",
                "Test des capacités Temporal en mode asynchrone",
                "Système de démonstration"
            );
            System.out.println("Workflow asynchrone démarré : " + asyncWorkflowId);
            
            // Démonstration 3 : Vérification du statut
            Thread.sleep(2000); // Attendre un peu
            System.out.println("3. Vérification du statut du workflow...");
            String status = temporalWorkflowService.getWorkflowStatus(asyncWorkflowId);
            System.out.println("Statut du workflow " + asyncWorkflowId + " : " + status);
            
            // Démonstration 4 : Programmation de maintenance
            System.out.println("4. Programmation d'une maintenance de démonstration...");
            LocalDateTime demoMaintenanceDate = LocalDateTime.now().plusMinutes(5);
            String maintenanceWorkflowId = temporalWorkflowService.scheduleMaintenanceWorkflow(
                firstMachine,
                demoMaintenanceDate
            );
            System.out.println("Maintenance de démonstration programmée : " + maintenanceWorkflowId);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la démonstration : " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FIN DE LA DÉMONSTRATION ===");
    }
    
    public void cleanupTestData() {
        System.out.println("Nettoyage des données de test...");
        // Ici on pourrait ajouter la logique de nettoyage si nécessaire
        // Pour l'instant, on laisse les données pour les tests
    }
}