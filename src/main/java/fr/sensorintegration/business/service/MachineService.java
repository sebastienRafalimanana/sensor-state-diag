package fr.sensorintegration.business.service;

import fr.sensorintegration.data.entity.Machine;
import fr.sensorintegration.data.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MachineService {
    
    private final MachineRepository machineRepository;
    
    public Optional<Machine> findById(UUID id) {
        return machineRepository.findById(id);
    }
    
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }
    
    public void deleteById(UUID id) {
        machineRepository.deleteById(id);
    }
    
    public List<Machine> findAll() {
        return machineRepository.findAll();
    }
}
