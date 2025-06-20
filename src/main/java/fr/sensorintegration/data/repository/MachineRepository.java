package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MachineRepository extends JpaRepository<Machine, String> {
    List<Machine> findByLocalisation(String localisation);
    List<Machine> findByNomContainingIgnoreCase(String nom);
    List<Machine> findByLocalisationAndNomContainingIgnoreCase(String localisation, String nom);
}
