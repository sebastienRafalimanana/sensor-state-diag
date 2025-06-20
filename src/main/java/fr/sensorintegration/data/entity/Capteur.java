package fr.sensorintegration.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Capteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Float seuilMin;
    private Float seuilMax;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
