package br.com.fiap.ecotech.ecotech_api.repository;

import br.com.fiap.ecotech.ecotech_api.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByActiveTrueOrderByTitleAsc();
}
