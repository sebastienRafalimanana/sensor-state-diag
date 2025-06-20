package fr.sensorintegration.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "diagnostic_history")
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String diagnosticType;

    @Column(columnDefinition = "TEXT")
    private String diagnosticDetails;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String interventionDetails;

    @Column(nullable = false)
    private String technician;

    @Column
    private LocalDateTime interventionDate;
}
