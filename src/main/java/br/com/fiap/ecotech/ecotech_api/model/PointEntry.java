package br.com.fiap.ecotech.ecotech_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * Histórico de pontos do usuário.
 * Cada ação (coleta, resgate de recompensa) gera uma entrada (+ ou -).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table(name = "point_entries")
public class PointEntry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)  // dono dos pontos
    private User user;

    @Column(nullable = false)     // rótulo amigável (ex: "Coleta: Bateria (1.2kg)")
    private String title;

    @Column(nullable = false)     // pode ser negativo no resgate
    private Integer points;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
