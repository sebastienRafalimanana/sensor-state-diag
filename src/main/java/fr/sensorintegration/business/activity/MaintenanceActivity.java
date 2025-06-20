package fr.sensorintegration.business.activity;

import fr.sensorintegration.data.entity.Machine;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface MaintenanceActivity {
    
    @ActivityMethod
    void performMaintenance(Machine machine);
}
