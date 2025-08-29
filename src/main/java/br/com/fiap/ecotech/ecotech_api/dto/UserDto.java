package br.com.fiap.ecotech.ecotech_api.dto;

import br.com.fiap.ecotech.ecotech_api.model.Role;

public record UserDto(
        Long id,
        String name,
        String email,
        Role role
) {}
