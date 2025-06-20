package fr.sensorintegration.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resume;
    private String recommandations;
    private LocalDateTime date;
    private String generePar;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
