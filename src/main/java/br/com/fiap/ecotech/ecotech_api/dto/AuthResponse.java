package br.com.fiap.ecotech.ecotech_api.dto;

import br.com.fiap.ecotech.ecotech_api.model.Role;

/*
 * Resposta comum de auth: token + dados essenciais do usuário.
 */
public record AuthResponse(
        String token,
        Long id,
        String name,
        String email,
        Role role
) {}
