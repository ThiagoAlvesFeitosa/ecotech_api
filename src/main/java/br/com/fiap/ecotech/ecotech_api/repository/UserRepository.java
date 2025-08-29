package br.com.fiap.ecotech.ecotech_api.repository;

import br.com.fiap.ecotech.ecotech_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
