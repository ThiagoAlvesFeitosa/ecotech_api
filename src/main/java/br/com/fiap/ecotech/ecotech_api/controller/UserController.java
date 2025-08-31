package br.com.fiap.ecotech.ecotech_api.controller;

import br.com.fiap.ecotech.ecotech_api.dto.UserDto;
import br.com.fiap.ecotech.ecotech_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Rotas do usuário:
 * /users/me  -> retorna dados do usuário autenticado
 * /users     -> retorna todos os usuários cadastrados
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // Injeção automática do serviço que lida com a lógica de usuários
    private final UserService users;

    // Retorna o usuário autenticado
    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(users.toDto(users.getCurrentUser()));
    }

    // Retorna a lista de todos os usuários cadastrados
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(users.findAll());
    }
}
