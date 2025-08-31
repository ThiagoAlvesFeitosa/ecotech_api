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

/**
 * Regras de autenticação:
 * - register: cria usuário, salva senha com hash e já gera JWT.
 * - login   : valida senha e devolve JWT.
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
                .password(encoder.encode(req.password())) // nunca salvar senha crua
                .role(Role.USER)                          // default USER
                .build();

        userRepo.save(u);

        // Gero JWT com claims úteis (id/role) pro app não precisar reconsultar
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
