package fr.sensorintegration.utils;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataGenerator {
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static final List<String> MACHINE_TYPES = Arrays.asList(
        "Moulage par injection plastique", "Soufflage"
    );
    
    private static final List<String> DIAGNOSTIC_TYPES = Arrays.asList(
        "Défaut de température", "Défaut de pression", "Vibration excessive", 
        "Réglage nécessaire", "Maintenance préventive", "Remplacement de pièce"
    );
    
    private static final Map<String, List<String>> MACHINE_SPECIFIC_ISSUES = Map.of(
        "Moulage par injection plastique", Arrays.asList(
            "Température trop élevée (>250°C)",
            "Pression insuffisante (<60 bars)",
            "Vibration excessive (>0.15)",
            "Usure des moules"
        ),
        "Soufflage", Arrays.asList(
            "Température trop basse (<90°C)",
            "Pression d'air trop élevée (>40 bars)",
            "Vibration excessive (>0.18)",
            "Problème de stabilisation"
        )
    );
    
    private static final List<String> TECHNICIANS = Arrays.asList(
        "Jean Dupont", "Marie Martin", "Pierre Dubois", "Sophie Bernard", "Thomas Lefevre"
    );
    
    private static final List<String> STATUSES = Arrays.asList(
        "En cours", "Terminé", "En attente", "Planifié", "Annulé"
    );
    
    public static Machine generateMachine() {
        String machineType = MACHINE_TYPES.get(RANDOM.nextInt(MACHINE_TYPES.size()));
        int machineNumber = RANDOM.nextInt(5) + 1; // 1 à 5 machines de chaque type
        
        Machine machine = new Machine();
        machine.setId(String.valueOf(UUID.randomUUID()));
        machine.setNom(machineType + " " + machineNumber);
        machine.setDescription("Machine de " + machineType.toLowerCase() + " de production de bouteilles en plastique");
        machine.setLocalisation("Atelier de production");
        
        return machine;
    }
    
    public static DiagnosticHistory generateDiagnostic(Machine machine) {
        String machineType = machine.getNom().split(" ")[0]; // Récupère le type de machine (Moulage ou Soufflage)
        
        DiagnosticHistory diagnostic = new DiagnosticHistory();
        diagnostic.setId(UUID.randomUUID().toString());
        diagnostic.setMachine(machine);
        diagnostic.setTimestamp(LocalDateTime.now().minusDays(RANDOM.nextInt(365)));
        diagnostic.setDiagnosticType(DIAGNOSTIC_TYPES.get(RANDOM.nextInt(DIAGNOSTIC_TYPES.size())));
        diagnostic.setDiagnosticDetails("Détection d'un " + generateRandomIssue(machineType));
        diagnostic.setStatus(STATUSES.get(RANDOM.nextInt(STATUSES.size())));
        diagnostic.setInterventionDetails(generateInterventionDetails(machineType));
        diagnostic.setTechnician(TECHNICIANS.get(RANDOM.nextInt(TECHNICIANS.size())));
        diagnostic.setInterventionDate(LocalDateTime.now().minusDays(RANDOM.nextInt(30)));
        
        return diagnostic;
    }
    
    private static String generateRandomIssue(String machineType) {
        List<String> issues = MACHINE_SPECIFIC_ISSUES.get(machineType);
        return issues.get(RANDOM.nextInt(issues.size()));
    }
    
    private static String generateInterventionDetails(String machineType) {
        List<String> details;
        if (machineType.equals("Moulage par injection plastique")) {
            details = Arrays.asList(
                "Ajustement des paramètres de température",
                "Réglage de la pression d'injection",
                "Remplacement des capteurs de température",
                "Nettoyage et lubrification des moules",
                "Calibration des capteurs de pression"
            );
        } else {
            details = Arrays.asList(
                "Ajustement des paramètres de température",
                "Réglage de la pression d'air",
                "Remplacement des capteurs de pression d'air",
                "Nettoyage des canaux d'air",
                "Calibration des capteurs de vibration"
            );
        }
        return details.get(RANDOM.nextInt(details.size()));
    }
    
    public static List<Machine> generateMachines(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> generateMachine())
            .collect(Collectors.toList());
    }
    
    public static List<DiagnosticHistory> generateDiagnosticsForMachine(Machine machine, int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> generateDiagnostic(machine))
            .collect(Collectors.toList());
    }
    
    public static DiagnosticHistory generateTemporalDiagnostic(Machine machine, String workflowId) {
        String machineType = machine.getNom().split(" ")[0];
        
        DiagnosticHistory diagnostic = new DiagnosticHistory();
        diagnostic.setId(workflowId);
        diagnostic.setMachine(machine);
        diagnostic.setTimestamp(LocalDateTime.now());
        diagnostic.setDiagnosticType("Diagnostic automatique via Temporal");
        diagnostic.setDiagnosticDetails("Workflow automatisé - " + generateRandomIssue(machineType));
        diagnostic.setStatus("En cours");
        diagnostic.setInterventionDetails("Diagnostic généré par workflow Temporal");
        diagnostic.setTechnician("Système automatique");
        diagnostic.setInterventionDate(null);
        
        return diagnostic;
    }
    
    public static List<Machine> generateMachinesForTesting(int count) {
        List<Machine> machines = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            String machineType = MACHINE_TYPES.get(i % MACHINE_TYPES.size());
            int machineNumber = (i / MACHINE_TYPES.size()) + 1;
            
            Machine machine = new Machine();
            machine.setId(String.valueOf(UUID.randomUUID()));
            machine.setNom(machineType + " " + machineNumber);
            machine.setDescription("Machine de test " + machineType.toLowerCase() + " - Générée automatiquement");
            machine.setLocalisation("Zone de test " + ((i % 3) + 1));
            
            machines.add(machine);
        }
        
        return machines;
    }
    
    public static List<String> generateDiagnosticScenarios() {
        return Arrays.asList(
            "Surveillance routine",
            "Diagnostic d'urgence",
            "Maintenance préventive",
            "Vérification post-réparation",
            "Calibration des capteurs",
            "Test de performance",
            "Diagnostic prédictif",
            "Inspection qualité"
        );
    }
}
