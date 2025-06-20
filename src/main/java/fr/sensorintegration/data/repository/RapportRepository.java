package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.Rapport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface RapportRepository extends JpaRepository<Rapport,Long> {
    //Rapport du macine
    List<Rapport> findByMachineIdOrderByDateDesc(Long machineId);


    //Recherche des rapports par type de generateur dans une intervalle de temps
    @Query("SELECT r FROM Rapport r WHERE \n"+
            "r.machine.id = :machineId AND \n"+
            "r.generePar = :generePar AND r.date \n"+
            "BETWEEN :debut AND :fin")
    List<Rapport> findByMachineAndDateBetween(
            @Param("machineId") Long machineId,
            @Param("generePar") String generePar,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);

    // Compte les rapports par machine et type de generateur
    @Query("SELECT r.machine.id, r.generePar, COUNT(r) FROM Rapport r GROUP BY r.machine.id, r.generePar")
    List<Object[]> countByMachineAndGenerator();

    // Recupere les rapports recents associes a des alertes

    @Query("SELECT r FROM Rapport r JOIN Alerte a ON r.machine.id = a.machine.id " +
            "WHERE r.date >= :depuis AND a.resolue = false AND r.machine.id = :machineId")
    List<Rapport> findRecentWithUnresolvedAlerts(
            @Param("machineId") Long machineId,
            @Param("depuis") LocalDateTime depuis);



    @Query("SELECT r FROM Rapport r WHERE LOWER(r.recommandations) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    Page<Rapport> findByRecommandationsContaining(
            @Param("motCle") String motCle,
            Pageable pageable);
}
