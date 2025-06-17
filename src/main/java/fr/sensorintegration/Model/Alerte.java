package fr.sensorintegration.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String criticite;
    private String description;
    private Boolean resolue;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
