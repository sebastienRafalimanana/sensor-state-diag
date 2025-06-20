package fr.sensorintegration.business.activity;

import fr.sensorintegration.data.entity.DiagnosticHistory;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.business.service.DiagnosticHistoryService;
import fr.sensorintegration.business.service.MachineService;
import io.temporal.activity.ActivityInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class DiagnosticActivityImpl implements DiagnosticActivity {
    
    @Autowired
    private DiagnosticHistoryService diagnosticHistoryService;
    
    @Autowired
    private MachineService machineService;
    
    @Override
    public DiagnosticHistory createDiagnostic(String diagnosticId, Machine machine, String diagnosticType, String issue, String technician) {
        DiagnosticHistory diagnostic = new DiagnosticHistory();
        diagnostic.setId(diagnosticId);
        diagnostic.setMachine(machine);
        diagnostic.setTimestamp(LocalDateTime.now());
        diagnostic.setDiagnosticType(diagnosticType);
        diagnostic.setDiagnosticDetails(issue);
        diagnostic.setStatus("En cours");
        diagnostic.setTechnician(technician);
        diagnostic.setInterventionDetails("Diagnostic créé automatiquement");
        
        return diagnosticHistoryService.createDiagnosticHistory(diagnostic);
    }
    
    @Override
    public String analyzeDiagnostic(DiagnosticHistory diagnostic) {
        // Logique d'analyse du diagnostic
        String diagnosticType = diagnostic.getDiagnosticType();
        String details = diagnostic.getDiagnosticDetails();
        
        // Analyse basée sur le type de diagnostic
        if (diagnosticType.contains("température")) {
            if (details.contains("trop élevée")) {
                return "Critique - Arrêt nécessaire";
            } else if (details.contains("trop basse")) {
                return "Attention - Réglage requis";
            }
        } else if (diagnosticType.contains("pression")) {
            if (details.contains("insuffisante") || details.contains("trop élevée")) {
                return "Urgent - Intervention requise";
            }
        } else if (diagnosticType.contains("vibration")) {
            if (details.contains("excessive")) {
                return "Maintenance - Vérification nécessaire";
            }
        } else if (diagnosticType.contains("préventive")) {
            return "Programmé - Maintenance de routine";
        }
        
        return "Normal - Surveillance continue";
    }
    
    @Override
    public DiagnosticHistory updateDiagnosticStatus(String diagnosticId, String status, String interventionDetails) {
        // Rechercher le diagnostic par ID
        Optional<DiagnosticHistory> optionalDiagnostic = diagnosticHistoryService.findById(diagnosticId);
        
        if (optionalDiagnostic.isPresent()) {
            DiagnosticHistory diagnostic = optionalDiagnostic.get();
            diagnostic.setStatus(status);
            diagnostic.setInterventionDetails(interventionDetails);
            diagnostic.setInterventionDate(LocalDateTime.now());
            
            return diagnosticHistoryService.updateDiagnosticHistory(diagnostic);
        } else {
            throw new RuntimeException("Diagnostic not found with ID: " + diagnosticId);
        }
    }
    
    @Override
    public void sendAlertNotification(Machine machine, String diagnosticType, String severity) {
        // Simulation d'envoi de notification
        System.out.println("=== ALERTE DIAGNOSTIC ===");
        System.out.println("Machine: " + machine.getNom());
        System.out.println("Localisation: " + machine.getLocalisation());
        System.out.println("Type de diagnostic: " + diagnosticType);
        System.out.println("Sévérité: " + severity);
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("========================");
        
        // Ici, on pourrait intégrer avec un système de notification réel :
        // - Email
        // - SMS
        // - Slack/Teams
        // - Système de ticketing
        
        // Logique conditionnelle basée sur la sévérité
        if ("Critique".equals(severity)) {
            // Notification immédiate aux superviseurs
            System.out.println("Notification CRITIQUE envoyée aux superviseurs");
        } else if ("Urgent".equals(severity)) {
            // Notification aux techniciens de garde
            System.out.println("Notification URGENTE envoyée aux techniciens");
        } else {
            // Notification standard
            System.out.println("Notification standard envoyée");
        }
    }
    
    @Override
    public boolean checkMachineStatus(Machine machine) {
        // Vérifier si la machine existe et est active
        if (machine == null || machine.getId() == null) {
            return false;
        }
        
        boolean exists = machineService.existsById(String.valueOf(UUID.fromString(machine.getId())));
        if (!exists) {
            return false;
        }
        
        // Simulation de vérification du statut de la machine
        // Dans un environnement réel, cela pourrait inclure :
        // - Vérification de la connectivité réseau
        // - Ping des capteurs
        // - Vérification des dernières données reçues
        // - Statut des systèmes de contrôle
        
        try {
            // Simulation d'une vérification réseau
            Thread.sleep(100); // Simule une latence de vérification
            
            // Logique de vérification basée sur le nom de la machine
            String nom = machine.getNom().toLowerCase();
            
            // Simulation : les machines de "Moulage" sont plus susceptibles d'être en ligne
            if (nom.contains("moulage")) {
                return Math.random() > 0.1; // 90% de chance d'être en ligne
            } else if (nom.contains("soufflage")) {
                return Math.random() > 0.2; // 80% de chance d'être en ligne
            }
            
            return Math.random() > 0.15; // 85% de chance par défaut
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    @Override
    public void generateDiagnosticReport(String diagnosticId) {
        try {
            Optional<DiagnosticHistory> optionalDiagnostic = diagnosticHistoryService.findById(diagnosticId);
            
            if (optionalDiagnostic.isPresent()) {
                DiagnosticHistory diagnostic = optionalDiagnostic.get();
                
                System.out.println("=== RAPPORT DE DIAGNOSTIC ===");
                System.out.println("ID: " + diagnostic.getId());
                System.out.println("Machine: " + diagnostic.getMachine().getNom());
                System.out.println("Type: " + diagnostic.getDiagnosticType());
                System.out.println("Détails: " + diagnostic.getDiagnosticDetails());
                System.out.println("Statut: " + diagnostic.getStatus());
                System.out.println("Technicien: " + diagnostic.getTechnician());
                System.out.println("Date de création: " + diagnostic.getTimestamp());
                System.out.println("Date d'intervention: " + diagnostic.getInterventionDate());
                System.out.println("Détails intervention: " + diagnostic.getInterventionDetails());
                System.out.println("============================");
                
                // Dans un environnement réel, ici on pourrait :
                // - Générer un PDF avec iTextPDF
                // - Envoyer le rapport par email
                // - Stocker le rapport dans un système de fichiers
                // - Intégrer avec un système de reporting
                
            } else {
                System.err.println("Impossible de générer le rapport : Diagnostic non trouvé avec l'ID " + diagnosticId);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération du rapport pour le diagnostic " + diagnosticId, e);
        }
    }
}