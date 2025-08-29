package br.com.fiap.ecotech.ecotech_api.dto;

import jakarta.validation.constraints.NotNull;

/*
 * Payload para resgatar recompensa.
 * Apenas o id (na URL) e opcionalmente quantidade (default 1).
 */
public record RewardRedeemRequest(
        @NotNull Integer quantity
) {}
