package br.com.fiap.ecotech.ecotech_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Corpo do POST /rewards/{id}/redeem
 *
 * Por que criar DTO?
 * - Documenta o que o endpoint espera.
 * - Aplica validações (@Min, @NotNull) antes de chegar na regra de negócio.
 */
public record RewardRedeemRequest(
        @NotNull @Min(1) Integer quantity // quantidade a resgatar (>= 1)
) {}
