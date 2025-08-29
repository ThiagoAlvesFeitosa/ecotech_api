package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.dto.CollectRequest;
import br.com.fiap.ecotech.ecotech_api.model.Collect;
import br.com.fiap.ecotech.ecotech_api.model.PointEntry;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.CollectRepository;
import br.com.fiap.ecotech.ecotech_api.repository.PointEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Fluxo de coleta:
 * - cria Collect
 * - com base no tipo/peso, calcula pontos e cria PointEntry
 * - devolve a lista atualizada (ou poderíamos devolver só a Collect criada)
 */
@Service
@RequiredArgsConstructor
public class CollectService {

    private final CollectRepository collectRepo;
    private final PointEntryRepository pointRepo;

    public Collect registerCollect(User user, CollectRequest req) {
        Collect c = Collect.builder()
                .user(user)
                .qrCode(req.qrCode())
                .eWasteType(req.eWasteType())
                .weightKg(req.weightKg())
                .build();

        collectRepo.save(c);

        // Regrinha simples de pontos (ajuste conforme seu app):
        int pts = calculatePoints(req.eWasteType(), req.weightKg());

        PointEntry p = PointEntry.builder()
                .user(user)
                .title("Coleta: " + req.eWasteType() + " (" + req.weightKg() + "kg)")
                .points(pts)
                .build();

        pointRepo.save(p);
        return c;
    }

    public List<Collect> listUserCollects(User user) {
        return collectRepo.findByUserOrderByCreatedAtDesc(user);
    }

    private int calculatePoints(String type, Double weightKg) {
        // Exemplos:
        // baterias/pilhas: 15 pts/kg
        // celular: 25 pts/unidade (vamos tratar 1.0 kg como 1 unidade)
        // cabos/periféricos: 8 pts/kg
        String t = type.toLowerCase();
        if (t.contains("bateria") || t.contains("pilha")) {
            return (int) Math.round(15 * weightKg);
        } else if (t.contains("celular") || t.contains("telefone")) {
            return 25 * weightKg.intValue();
        } else if (t.contains("cabo") || t.contains("perif")) {
            return (int) Math.round(8 * weightKg);
        }
        // default simples
        return (int) Math.round(10 * weightKg);
    }
}
