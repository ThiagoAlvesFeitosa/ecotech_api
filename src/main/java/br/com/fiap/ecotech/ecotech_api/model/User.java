package br.com.fiap.ecotech.ecotech_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * Representa a tabela de usuários.
 * Campos principais: id, name, email, password (hash), role, createdAt.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString(exclude = "password")
@Entity @Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)        // nome é obrigatório
    private String name;

    @Column(nullable = false, unique = true) // e-mail único (login)
    private String email;

    @Column(nullable = false)        // hash da senha (BCrypt)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)        // USER ou ADMIN
    private Role role;

    @Column(nullable = false)        // auditoria simples
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (role == null) role = Role.USER;
    }
}
