package fr.sensorintegration.business.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.DonneeCapteur;
import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.entity.Rapport;
import fr.sensorintegration.data.repository.DonneeCapteurRepository;
import fr.sensorintegration.data.repository.MachineRepository;
import fr.sensorintegration.data.repository.RapportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.time.LocalDateTime;

@Service
public class RapportService {
    private static final Logger logger = LoggerFactory.getLogger(RapportService.class);
    @Autowired
    private RapportRepository rapportRepository;
    @Autowired
    private DonneeCapteurRepository donneeCapteurRepository;
    @Autowired
    private MachineRepository machineRepository;

    public Rapport genererRapport(String machineId){
        logger.info("Generation d'un rapport pour la machine ID {}", machineId);

        // Vérifier l'existence de la machine
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new EntityNotFoundException("Machine avec ID " + machineId + " non trouvee"));

        // Recuperer les dernieres données des capteurs
        List<DonneeCapteur> dernieresDonnees = donneeCapteurRepository.findLatestByMachine(machineId);
        List<Rapport> rapportsPrecedents = rapportRepository.findByMachineIdOrderByDateDesc(machineId);

        StringBuilder resume = new StringBuilder();
        StringBuilder recommandations = new StringBuilder();

        // Analyse des donnees capteurs
        if (!dernieresDonnees.isEmpty()) {
            resume.append("Etat actuel de la machine ").append(machine.getNom()).append(":\n");
            for (DonneeCapteur donnee : dernieresDonnees) {
                Capteur capteur = donnee.getCapteur();
                resume.append(String.format("- %s : %.2f (min: %.2f, max: %.2f)\n",
                        capteur.getType(), donnee.getValeur(), capteur.getSeuilMin(), capteur.getSeuilMax()));

                // Vérification des seuils
                if (donnee.getValeur() > capteur.getSeuilMax()) {
                    recommandations.append(String.format("Alerte : %s depasse le seuil maximum (%.2f). Action recommandee : Vérifier immediatement.\n",
                            capteur.getType(), capteur.getSeuilMax()));
                } else if (donnee.getValeur() < capteur.getSeuilMin()) {
                    recommandations.append(String.format("Alerte : %s sous le seuil minimum (%.2f). Action recommandee : Inspecter la cause.\n",
                            capteur.getType(), capteur.getSeuilMin()));
                }
            }
        } else {
            resume.append("Aucune donnee capteur disponible pour la machine.\n");
            recommandations.append("Verifier la configuration des capteurs.\n");
        }

        if (!rapportsPrecedents.isEmpty()) {
            resume.append("\nComparaison avec le rapport precedent du ").append(rapportsPrecedents.get(0).getDate()).append(":\n");
            resume.append("- Analyse des ecarts en cours (e implementer selon les besoins).\n");
        } else {
            resume.append("Premier rapport pour cette machine.\n");
        }

        Rapport rapport = new Rapport();
        rapport.setMachine(machine);
        rapport.setResume(resume.toString());
        rapport.setRecommandations(recommandations.toString());
        rapport.setDate(LocalDateTime.now());
        rapport.setGenerePar("IA");

        return rapportRepository.save(rapport);
    }
    public byte[] exporterPdf(Long rapportId) throws Exception {
        logger.info("Exportation du rapport ID {} en PDF", rapportId);

        Rapport rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new EntityNotFoundException("Rapport avec ID " + rapportId + " non trouvé"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Rapport de Maintenance")
                .setBold()
                .setFontSize(16));

        document.add(new Paragraph("Machine : " + rapport.getMachine().getNom()));
        document.add(new Paragraph("Date : " + rapport.getDate().toString()));
        document.add(new Paragraph("Généré par : " + rapport.getGenerePar()));

        document.add(new Paragraph("\nRésumé :"));
        Table table = new Table(1).useAllAvailableWidth();
        table.addCell("Contenu du résumé :\n" + rapport.getResume());
        document.add(table);

        document.add(new Paragraph("\nRecommandations :"));
        document.add(new Paragraph(rapport.getRecommandations()).setItalic());

        document.close();
        return baos.toByteArray();
    }

    public List<Rapport> rechercherParMachineEtGenerateurEtDate(
            java.util.UUID machineId, String generePar, LocalDateTime debut, LocalDateTime fin) {
        return rapportRepository.findByMachineAndDateBetween(machineId, generePar, debut, fin);
    }

    public Page<Rapport> rechercherParMotCle(String motCle, Pageable pageable) {
        return rapportRepository.findByRecommandationsContaining(motCle, pageable);
    }
    public List<Object[]> compterParMachineEtGenerateur() {
        return rapportRepository.countByMachineAndGenerator();
    }

    public List<Rapport> rechercherRapportsAvecAlertesNonResolues(java.util.UUID machineId, LocalDateTime depuis) {
        return rapportRepository.findRecentWithUnresolvedAlerts(machineId, depuis);
    }
}
