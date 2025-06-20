package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.DonneeCapteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DonneeCapteurRepository extends JpaRepository<DonneeCapteur,Long> {

    @Query("SELECT d FROM DonneeCapteur d WHERE d.capteur.machine.id = :machineId AND d.timestamp = (SELECT MAX(d2.timestamp) FROM DonneeCapteur d2 WHERE d2.capteur.id = d.capteur.id)")
    List<DonneeCapteur> findLatestByMachine(@Param("machineId") Long machineId);
}
