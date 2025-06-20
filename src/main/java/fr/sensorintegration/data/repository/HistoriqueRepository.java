package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long> {

}
