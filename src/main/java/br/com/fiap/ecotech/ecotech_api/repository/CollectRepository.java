package br.com.fiap.ecotech.ecotech_api.repository;

import br.com.fiap.ecotech.ecotech_api.model.Collect;
import br.com.fiap.ecotech.ecotech_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectRepository extends JpaRepository<Collect, Long> {
    List<Collect> findByUserOrderByCreatedAtDesc(User user);
}
