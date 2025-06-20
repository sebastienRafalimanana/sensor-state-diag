package fr.sensorintegration.core.config;

import fr.sensorintegration.business.activity.DiagnosticActivityImpl;
import fr.sensorintegration.business.activity.MaintenanceActivityImpl;
import fr.sensorintegration.business.workflow.DiagnosticWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import jakarta.annotation.PostConstruct;

@Configuration
@ConditionalOnProperty(name = "temporal.enabled", havingValue = "true", matchIfMissing = false)
public class TemporalConfiguration {
    
    @Value("${temporal.server.address:localhost:7233}")
    private String temporalServerAddress;
    
    @Value("${temporal.namespace:sensor-integration}")
    private String temporalNamespace;
    
    @Value("${temporal.task-queue:sensor-queue}")
    private String taskQueue;
    
    @Autowired
    private DiagnosticActivityImpl diagnosticActivity;
    
    @Autowired
    private MaintenanceActivityImpl maintenanceActivity;
    
    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServerAddress)
                .build();
        return WorkflowServiceStubs.newServiceStubs(options);
    }
    
    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        WorkflowClientOptions options = WorkflowClientOptions.newBuilder()
                .setNamespace(temporalNamespace)
                .build();
        return WorkflowClient.newInstance(workflowServiceStubs, options);
    }
    
    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }
    
    @Bean
    @DependsOn({"diagnosticActivityImpl", "maintenanceActivityImpl"})
    public Worker sensorWorker(WorkerFactory workerFactory) {
        Worker worker = workerFactory.newWorker(taskQueue);
        
        // Enregistrement des workflows
        worker.registerWorkflowImplementationTypes(DiagnosticWorkflowImpl.class);
        
        // Enregistrement des activities
        worker.registerActivitiesImplementations(diagnosticActivity);
        worker.registerActivitiesImplementations(maintenanceActivity);
        
        return worker;
    }
    
    @PostConstruct
    public void startWorkers() {
        try {
            System.out.println("=== TEMPORAL CONFIGURATION ===");
            System.out.println("Server: " + temporalServerAddress);
            System.out.println("Namespace: " + temporalNamespace);
            System.out.println("Task Queue: " + taskQueue);
            System.out.println("Workers started successfully!");
            System.out.println("===============================");
        } catch (Exception e) {
            System.err.println("Warning: Could not connect to Temporal server: " + e.getMessage());
            System.out.println("Application will continue without Temporal workflows");
            throw new RuntimeException("Temporal configuration failed", e);
        }
    }
    
    public String getTaskQueue() {
        return taskQueue;
    }
    
    public String getTemporalNamespace() {
        return temporalNamespace;
    }
}