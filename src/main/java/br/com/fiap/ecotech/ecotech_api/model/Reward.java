package br.com.fiap.ecotech.ecotech_api.model;

import jakarta.persistence.*;
import lombok.*;

/*
 * Catálogo de recompensas (resgatáveis com pontos).
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table(name = "rewards")
public class Reward {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)     // nome da recompensa
    private String title;

    @Column(nullable = false)     // custo em pontos p/ resgatar
    private Integer costPoints;

    @Column(nullable = false)     // estoque disponível
    private Integer stock;

    @Column(nullable = false)     // ativa/inativa
    private Boolean active;
}
