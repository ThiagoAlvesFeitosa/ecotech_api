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

/**
 * Endpoints de Recompensas
 *
 * Público (autenticado):
 *  - GET  /rewards                 -> lista catálogo ativo (para o app)
 *  - POST /rewards/{id}/redeem     -> resgata (debita pontos)
 *
 * Admin (painel):
 *  - GET    /admin/rewards         -> lista todas (ativas/inativas)
 *  - POST   /admin/rewards         -> cria recompensa
 *  - PUT    /admin/rewards/{id}    -> atualiza recompensa
 *  - DELETE /admin/rewards/{id}    -> remove recompensa
 *
 * IMPORTANTE (segurança):
 *  - Garanta no SecurityConfig que /admin/** exija ROLE_ADMIN.
 *    (ou use @PreAuthorize com @EnableMethodSecurity)
 */
@RestController
@RequiredArgsConstructor
public class RewardsController {

    private final RewardService rewards;
    private final UserService users;

    // ================== CATÁLOGO (APP) ==================

    @GetMapping("/rewards")
    public ResponseEntity<List<Reward>> listActive() {
        return ResponseEntity.ok(rewards.listActive());
    }

    @PostMapping("/rewards/{id}/redeem")
    public ResponseEntity<Void> redeem(
            @PathVariable Long id,
            @Valid @RequestBody RewardRedeemRequest req
    ) {
        rewards.redeem(users.getCurrentUser(), id, req.quantity());
        return ResponseEntity.noContent().build(); // 204 sem body
    }

    // ================== ADMIN (CRUD) ==================
    // DICA: Proteja esses endpoints com ROLE_ADMIN no SecurityConfig
    // (ex.: .requestMatchers("/admin/**").hasRole("ADMIN"))

    @GetMapping("/admin/rewards")
    public ResponseEntity<List<Reward>> adminList() {
        return ResponseEntity.ok(rewards.listAll());
    }

    @PostMapping("/admin/rewards")
    public ResponseEntity<Reward> create(@RequestBody Reward body) {
        Reward saved = rewards.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/admin/rewards/{id}")
    public ResponseEntity<Reward> update(@PathVariable Long id, @RequestBody Reward body) {
        Reward updated = rewards.update(id, body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/admin/rewards/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rewards.delete(id);
        return ResponseEntity.noContent().build();
    }
}
