package br.com.fiap.ecotech.ecotech_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * Payload para registrar uma coleta.
 * eWasteType é livre (string), mas podemos validar pelo front por enquanto.
 */
public record CollectRequest(
        @NotBlank String qrCode,
        @NotBlank String eWasteType,
        @NotNull @Min(0) Double weightKg
) {}
