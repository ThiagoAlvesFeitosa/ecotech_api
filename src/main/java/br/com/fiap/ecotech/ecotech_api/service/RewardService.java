package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.model.PointEntry;
import br.com.fiap.ecotech.ecotech_api.model.Reward;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.PointEntryRepository;
import br.com.fiap.ecotech.ecotech_api.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Operações com o catálogo de recompensas e resgate.
 */
@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepo;
    private final PointEntryRepository pointRepo;

    public List<Reward> listActive() {
        return rewardRepo.findByActiveTrueOrderByTitleAsc();
    }

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

        // Lança entrada negativa de pontos (resgate)
        pointRepo.save(PointEntry.builder()
                .user(user)
                .title("Resgate: " + r.getTitle() + " x" + quantity)
                .points(-totalCost)
                .build());

        // Atualiza estoque
        r.setStock(r.getStock() - quantity);
        rewardRepo.save(r);
    }
}
