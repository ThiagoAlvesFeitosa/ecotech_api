package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.model.PointEntry;
import br.com.fiap.ecotech.ecotech_api.model.Reward;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.PointEntryRepository;
import br.com.fiap.ecotech.ecotech_api.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio das recompensas:
 * - listagem para o app (somente ativas);
 * - CRUD para o admin (criar/editar/excluir);
 * - resgate (debitar pontos e baixar estoque).
 */
@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepo;
    private final PointEntryRepository pointRepo;

    // ================== CATÁLOGO (APP / USUÁRIO) ==================

    /** Lista somente as recompensas ativas (para o app). */
    public List<Reward> listActive() {
        return rewardRepo.findByActiveTrueOrderByTitleAsc();
    }

    // ================== ADMIN (CRUD) ==================

    /** Lista todas (ativas e inativas) para o painel admin. */
    public List<Reward> listAll() {
        return rewardRepo.findAll();
    }

    /** Cria uma nova recompensa. */
    public Reward create(Reward r) {
        // validações simples (poderia usar @Valid no controller)
        if (r.getTitle() == null || r.getTitle().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (r.getCostPoints() == null || r.getCostPoints() < 1) {
            throw new IllegalArgumentException("Custo em pontos inválido.");
        }
        if (r.getStock() == null || r.getStock() < 0) {
            throw new IllegalArgumentException("Estoque inválido.");
        }
        if (r.getActive() == null) {
            r.setActive(Boolean.TRUE);
        }
        return rewardRepo.save(r);
    }

    /** Atualiza uma recompensa existente. */
    public Reward update(Long id, Reward data) {
        Reward r = rewardRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa não encontrada"));

        // atualiza campos permitidos
        if (data.getTitle() != null) r.setTitle(data.getTitle());
        if (data.getCostPoints() != null) r.setCostPoints(data.getCostPoints());
        if (data.getStock() != null) r.setStock(data.getStock());
        if (data.getActive() != null) r.setActive(data.getActive());

        return rewardRepo.save(r);
    }

    /** Exclui uma recompensa (somente admin). */
    public void delete(Long id) {
        if (!rewardRepo.existsById(id)) {
            throw new IllegalArgumentException("Recompensa não encontrada");
        }
        rewardRepo.deleteById(id);
    }

    // ================== RESGATE (APP / USUÁRIO) ==================

    /**
     * Tenta resgatar uma recompensa:
     * - valida ativo/estoque;
     * - verifica se o usuário tem pontos suficientes;
     * - lança uma entrada de pontos NEGATIVA (debita);
     * - reduz o estoque.
     */
    public void redeem(User user, Long rewardId, int quantity) {
        Reward r = rewardRepo.findById(rewardId)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa não encontrada"));
        if (!Boolean.TRUE.equals(r.getActive())) {
            throw new IllegalArgumentException("Recompensa inativa");
        }
        if (quantity < 1) quantity = 1;
        if (r.getStock() < quantity) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        int totalCost = r.getCostPoints() * quantity;
        int userTotal = pointRepo.sumPointsByUser(user);
        if (userTotal < totalCost) {
            throw new IllegalArgumentException("Pontos insuficientes");
        }

        // débito de pontos (entrada negativa)
        pointRepo.save(PointEntry.builder()
                .user(user)
                .title("Resgate: " + r.getTitle() + " x" + quantity)
                .points(-totalCost)
                .build());

        // baixa estoque
        r.setStock(r.getStock() - quantity);
        rewardRepo.save(r);
    }
}
