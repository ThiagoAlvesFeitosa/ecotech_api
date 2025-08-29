package br.com.fiap.ecotech.ecotech_api.controller;

import br.com.fiap.ecotech.ecotech_api.model.PointEntry;
import br.com.fiap.ecotech.ecotech_api.repository.PointEntryRepository;
import br.com.fiap.ecotech.ecotech_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*
 * /points        -> histórico do usuário
 * /points/total  -> soma total (para o "quadro verde")
 */
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointsController {

    private final PointEntryRepository repo;
    private final UserService users;

    @GetMapping
    public ResponseEntity<List<PointEntry>> history() {
        return ResponseEntity.ok(repo.findByUserOrderByCreatedAtDesc(users.getCurrentUser()));
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Integer>> total() {
        int total = repo.sumPointsByUser(users.getCurrentUser());
        return ResponseEntity.ok(Map.of("total", total));
    }
}
