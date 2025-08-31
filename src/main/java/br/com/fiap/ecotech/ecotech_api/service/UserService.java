package br.com.fiap.ecotech.ecotech_api.service;

import br.com.fiap.ecotech.ecotech_api.dto.UserDto;
import br.com.fiap.ecotech.ecotech_api.model.User;
import br.com.fiap.ecotech.ecotech_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Serviço responsável por manipular dados de usuários:
 *  - Obter o usuário logado no sistema.
 *  - Converter entidades User para UserDto.
 *  - Listar todos os usuários do banco de dados.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    // Injeta automaticamente o repositório para acessar o banco de dados
    private final UserRepository userRepo;

    /*
     * Busca o usuário atualmente autenticado no sistema.
     * - Pega o email armazenado no contexto de segurança (JWT/Spring Security)
     * - Busca no banco pelo email
     * - Se não encontrar, lança exceção
     */
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
    }

    /*
     * Converte uma entidade User em um DTO simplificado (UserDto)
     * - Usado para evitar expor a entidade completa para o cliente
     */
    public UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }

    /*
     * Busca todos os usuários do banco de dados e converte cada um para UserDto
     * - Utiliza stream + map para converter a lista de entidades em lista de DTOs
     */
    public List<UserDto> findAll() {
        return userRepo.findAll()
                .stream()
                .map(this::toDto) // converte cada User em UserDto
                .collect(Collectors.toList()); // retorna a lista final
    }
}
