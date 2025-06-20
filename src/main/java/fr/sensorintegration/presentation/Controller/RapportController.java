package fr.sensorintegration.presentation.Controller;

import fr.sensorintegration.data.entity.Rapport;
import fr.sensorintegration.business.service.RapportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rapports")
public class RapportController {
    @Autowired
    private RapportService rapportService;
    @PostMapping("/generer/{machineId}")
    public ResponseEntity<Object> genererRapport(@PathVariable Long machineId) {
        try {
            Rapport rapport = rapportService.genererRapport(machineId);
            return ResponseEntity.ok(rapport);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    @GetMapping("/exporter/{rapportId}")
    public ResponseEntity<byte[]> exporterPdf(@PathVariable Long rapportId) {
        try {
            byte[] pdf = rapportService.exporterPdf(rapportId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "rapport_" + rapportId + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ia/{machineId}")
    public ResponseEntity<List<Rapport>> rechercherParMachineEtGenerateurEtDate(
            @PathVariable Long machineId,
            @RequestParam String generePar,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Rapport> rapports = rapportService.rechercherParMachineEtGenerateurEtDate(machineId, generePar, debut, fin);
        return ResponseEntity.ok(rapports);
    }

    @GetMapping("/rechercher")
    public ResponseEntity<Page<Rapport>> rechercherParMotCle(
            @RequestParam String motCle,
            Pageable pageable) {
        Page<Rapport> rapports = rapportService.rechercherParMotCle(motCle, pageable);
        return ResponseEntity.ok(rapports);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<Object[]>> compterParMachineEtGenerateur() {
        List<Object[]> stats = rapportService.compterParMachineEtGenerateur();
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/alertes-non-resolues/{machineId}")
    public ResponseEntity<List<Rapport>> rechercherRapportsAvecAlertesNonResolues(
            @PathVariable Long machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime depuis) {
        List<Rapport> rapports = rapportService.rechercherRapportsAvecAlertesNonResolues(machineId, depuis);
        return ResponseEntity.ok(rapports);
    }
}
