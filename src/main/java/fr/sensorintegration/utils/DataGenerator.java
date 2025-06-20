package fr.sensorintegration.utils;

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
        
        return new Machine(
            UUID.randomUUID(),
            machineType + " " + machineNumber,
            "Machine de " + machineType.toLowerCase() + " de production de bouteilles en plastique",
            "Atelier de production"
        );
    }
    
    public static DiagnosticHistory generateDiagnostic(Machine machine) {
        String machineType = machine.getNom().split(" ")[0]; // Récupère le type de machine (Moulage ou Soufflage)
        
        return new DiagnosticHistory(
            UUID.randomUUID(),
            machine,
            LocalDateTime.now().minusDays(RANDOM.nextInt(365)),
            DIAGNOSTIC_TYPES.get(RANDOM.nextInt(DIAGNOSTIC_TYPES.size())),
            "Détection d'un " + generateRandomIssue(machineType),
            STATUSES.get(RANDOM.nextInt(STATUSES.size())),
            generateInterventionDetails(machineType),
            TECHNICIANS.get(RANDOM.nextInt(TECHNICIANS.size())),
            null
        );
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
}
