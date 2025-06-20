package fr.sensorintegration.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DonneeCapteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float valeur;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "capteur_id")
    private Capteur capteur;


}
