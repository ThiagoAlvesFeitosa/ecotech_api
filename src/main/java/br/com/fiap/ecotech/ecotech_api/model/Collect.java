package br.com.fiap.ecotech.ecotech_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * Registro de uma coleta realizada por um usuário.
 * Mantemos referência ao user (ManyToOne), além de qrCode, tipo e peso.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity @Table(name = "collects")
public class Collect {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)              // quem fez a coleta
    private User user;

    @Column(nullable = false)                 // código lido pelo app (ponto de coleta)
    private String qrCode;

    @Column(nullable = false)                 // bateria, celular, cabo, etc.
    private String eWasteType;

    @Column(nullable = false)                 // peso em kg (p/ celular podemos enviar 1.0 = 1 un)
    private Double weightKg;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
