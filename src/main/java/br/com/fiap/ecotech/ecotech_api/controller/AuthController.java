package br.com.fiap.ecotech.ecotech_api.controller;

import br.com.fiap.ecotech.ecotech_api.dto.AuthResponse;
import br.com.fiap.ecotech.ecotech_api.dto.LoginRequest;
import br.com.fiap.ecotech.ecotech_api.dto.RegisterRequest;
import br.com.fiap.ecotech.ecotech_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/*
 * Endpoints públicos de autenticação.
 * /auth/register -> cria usuário e já devolve token
 * /auth/login    -> valida credenciais e devolve token
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }
}
