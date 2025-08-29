package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.dto.AuthResponse;
import br.com.fiap.ecotech.ecotech_api.dto.LoginRequest;
import br.com.fiap.ecotech.ecotech_api.dto.RegisterRequest;
import br.com.fiap.ecotech.ecotech_api.model.Role;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.UserRepository;
import br.com.fiap.ecotech.ecotech_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/*
 * Regras de registro e login:
 * - cadastro: impede e-mail duplicado e salva senha com hash
 * - login: valida senha e retorna JWT com claims de id/role
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        User u = User.builder()
                .name(req.name())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(Role.USER)
                .build();

        userRepo.save(u);

        String token = jwt.generateToken(u.getEmail(), Map.of(
                "id", u.getId(),
                "role", u.getRole().name()
        ));

        return new AuthResponse(token, u.getId(), u.getName(), u.getEmail(), u.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        String token = jwt.generateToken(u.getEmail(), Map.of(
                "id", u.getId(),
                "role", u.getRole().name()
        ));

        return new AuthResponse(token, u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}
