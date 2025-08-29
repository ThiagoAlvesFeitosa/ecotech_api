package br.com.fiap.ecotech.ecotech_api.controller;

import br.com.fiap.ecotech.ecotech_api.dto.RewardRedeemRequest;
import br.com.fiap.ecotech.ecotech_api.model.Reward;
import br.com.fiap.ecotech.ecotech_api.service.RewardService;
import br.com.fiap.ecotech.ecotech_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * /rewards (GET)                 -> lista catálogo ativo
 * /rewards/{id}/redeem (POST)    -> debita pontos e reduz estoque
 */
@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardsController {

    private final RewardService rewards;
    private final UserService users;

    @GetMapping
    public ResponseEntity<List<Reward>> list() {
        return ResponseEntity.ok(rewards.listActive());
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<Void> redeem(
            @PathVariable Long id,
            @Valid @RequestBody RewardRedeemRequest req
    ) {
        rewards.redeem(users.getCurrentUser(), id, req.quantity());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
