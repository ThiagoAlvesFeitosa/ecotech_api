package br.com.fiap.ecotech.ecotech_api.controller;

import br.com.fiap.ecotech.ecotech_api.dto.CollectRequest;
import br.com.fiap.ecotech.ecotech_api.model.Collect;
import br.com.fiap.ecotech.ecotech_api.service.CollectService;
import br.com.fiap.ecotech.ecotech_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * /collects (GET)  -> lista coletas do usuário logado
 * /collects (POST) -> registra nova coleta e gera pontos
 */
@RestController
@RequestMapping("/collects")
@RequiredArgsConstructor
public class CollectsController {

    private final CollectService collects;
    private final UserService users;

    @GetMapping
    public ResponseEntity<List<Collect>> list() {
        return ResponseEntity.ok(collects.listUserCollects(users.getCurrentUser()));
    }

    @PostMapping
    public ResponseEntity<Collect> create(@Valid @RequestBody CollectRequest req) {
        var c = collects.registerCollect(users.getCurrentUser(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
}
