package br.com.fiap.ecotech.ecotech_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * Payload do cadastro.
 * @NotBlank: campo não pode ser vazio/nulo.
 */
public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
