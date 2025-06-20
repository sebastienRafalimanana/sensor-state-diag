package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.DonneeCapteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonneeCapteurRepository extends JpaRepository<DonneeCapteur,Long> {

    @Query("SELECT d FROM DonneeCapteur d WHERE d.capteur.machine.id = :machineId AND d.timestamp = (SELECT MAX(d2.timestamp) FROM DonneeCapteur d2 WHERE d2.capteur.id = d.capteur.id)")
    List<DonneeCapteur> findLatestByMachine(@Param("machineId") String machineId);
    
    List<DonneeCapteur> findByCapteur(Capteur capteur);
    
    Page<DonneeCapteur> findByCapteur(Capteur capteur, Pageable pageable);
    
    List<DonneeCapteur> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<DonneeCapteur> findByCapteurAndTimestampBetween(Capteur capteur, LocalDateTime startDate, LocalDateTime endDate);
    
    List<DonneeCapteur> findByValeurBetween(Float minValeur, Float maxValeur);
    
    @Query("SELECT d FROM DonneeCapteur d WHERE d.capteur = :capteur ORDER BY d.timestamp DESC")
    List<DonneeCapteur> findTopByCapteurOrderByTimestampDesc(@Param("capteur") Capteur capteur, int limit);
    
    Optional<DonneeCapteur> findTopByCapteurOrderByTimestampDesc(Capteur capteur);
    
    @Query("SELECT AVG(d.valeur) FROM DonneeCapteur d WHERE d.capteur = :capteur")
    Double findAverageValeurByCapteur(@Param("capteur") Capteur capteur);
    
    @Query("SELECT AVG(d.valeur) FROM DonneeCapteur d WHERE d.capteur = :capteur AND d.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageValeurByCapteurAndDateRange(@Param("capteur") Capteur capteur, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d FROM DonneeCapteur d WHERE d.capteur = :capteur AND (d.valeur < d.capteur.seuilMin OR d.valeur > d.capteur.seuilMax)")
    List<DonneeCapteur> findDonneesOutOfThreshold(@Param("capteur") Capteur capteur);
    
    long countByCapteur(Capteur capteur);
    
    @Query("SELECT COUNT(d) FROM DonneeCapteur d WHERE d.capteur = :capteur AND (d.valeur < d.capteur.seuilMin OR d.valeur > d.capteur.seuilMax)")
    long countDonneesOutOfThreshold(@Param("capteur") Capteur capteur);
}
