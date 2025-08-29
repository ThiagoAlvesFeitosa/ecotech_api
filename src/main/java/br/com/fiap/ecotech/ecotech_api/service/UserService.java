package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.dto.UserDto;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
 * Utilitários para lidar com o usuário logado e conversões para DTO.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
    }

    public UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}
