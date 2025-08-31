package br.com.fiap.ecotech.ecotech_api.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tabela de "recompensas" que podem ser resgatadas com pontos.
 *
 * Por que existir esta entidade?
 * - O ADMIN (no painel web) vai cadastrar/editar/remover recompensas.
 * - O APP (Flutter) lista e permite o usuário resgatar se tiver pontos.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // chave primária (gerada pelo banco)

    @Column(nullable = false)
    private String title;         // nome da recompensa (ex.: "Desconto X")

    @Column(nullable = false)
    private Integer costPoints;   // custo em pontos para resgatar 1 unidade

    @Column(nullable = false)
    private Integer stock;        // estoque disponível (quantas unidades)

    @Column(nullable = false)
    private Boolean active;       // se aparece no catálogo (true) ou não (false)
}
