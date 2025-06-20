package fr.sensorintegration.data.repository;

import fr.sensorintegration.data.entity.Capteur;
import fr.sensorintegration.data.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapteurRepository extends JpaRepository<Capteur, Long> {
    
    List<Capteur> findByMachine(Machine machine);
    
    List<Capteur> findByType(String type);
    
    List<Capteur> findByMachineAndType(Machine machine, String type);
    
    List<Capteur> findBySeuilMinGreaterThanEqual(Float seuilMin);
    
    List<Capteur> findBySeuilMaxLessThanEqual(Float seuilMax);
    
    List<Capteur> findBySeuilMinGreaterThanEqualAndSeuilMaxLessThanEqual(Float minSeuil, Float maxSeuil);
    
    @Query("SELECT c FROM Capteur c WHERE c.seuilMin <= :valeur AND c.seuilMax >= :valeur")
    List<Capteur> findCapteursInRange(@Param("valeur") Float valeur);
    
    @Query("SELECT c FROM Capteur c WHERE c.machine = :machine AND c.seuilMin <= :valeur AND c.seuilMax >= :valeur")
    List<Capteur> findCapteursByMachineInRange(@Param("machine") Machine machine, @Param("valeur") Float valeur);
    
    long countByMachine(Machine machine);
    
    long countByType(String type);
    
    @Query("SELECT DISTINCT c.type FROM Capteur c")
    List<String> findDistinctTypes();
    
    boolean existsByMachineAndType(Machine machine, String type);
    
    @Query("SELECT c FROM Capteur c WHERE c.machine.id = :machineId")
    List<Capteur> findByMachineId(@Param("machineId") String machineId);
}