package br.com.fiap.ecotech.ecotech_api.repository;

import br.com.fiap.ecotech.ecotech_api.model.PointEntry;
import br.com.fiap.ecotech.ecotech_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointEntryRepository extends JpaRepository<PointEntry, Long> {

    List<PointEntry> findByUserOrderByCreatedAtDesc(User user);

    // Soma total de pontos do usuário. COALESCE para evitar null.
    @Query("select coalesce(sum(p.points),0) from PointEntry p where p.user = :user")
    int sumPointsByUser(User user);
}
