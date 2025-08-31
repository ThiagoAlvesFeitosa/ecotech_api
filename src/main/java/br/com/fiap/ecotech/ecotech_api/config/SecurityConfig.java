package br.com.fiap.ecotech.ecotech_api.config;

import br.com.fiap.ecotech.ecotech_api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Autenticação/Provedores
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

// HTTP e Cadeia de Filtros
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

// Usuário/Senha
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Filtros e Ignore
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * @Configuration
 *  - Diz ao Spring que esta classe tem "beans" de configuração.
 *  - "Bean" = objeto gerenciado pelo Spring (ele cria/guarda/fornece quando alguém @Autowired).
 *
 * @RequiredArgsConstructor (Lombok)
 *  - Gera um construtor com os "final" que temos como atributos.
 *  - Assim eu não preciso escrever construtor manual para injetar dependências (ex.: JwtAuthFilter).
 *
 * O objetivo desta classe é definir:
 *  1) O que é público (ex.: /auth/**, Swagger, H2).
 *  2) O que exige token JWT.
 *  3) Regras por perfil (ex.: /admin/** só ADMIN).
 *  4) A cadeia de filtros para validar o token em cada request.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // Meu filtro que LÊ o header Authorization: Bearer <token>,
    // valida o JWT e coloca a autenticação no contexto do Spring.
    private final JwtAuthFilter jwtAuthFilter;

    // Serviço que o Spring usa para buscar usuário por username (no nosso caso, email)
    private final UserDetailsService userDetailsService;

    /**
     * WebSecurityCustomizer
     *  - “Ignorar” significa: essas rotas nem entram no filtro de segurança do Spring.
     *  - Uso isso para deixar /auth/** público de verdade, e liberar Swagger/H2 no ambiente dev.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/auth/**",
                "/swagger-ui.html", "/swagger-ui/**",
                "/v3/api-docs/**",
                "/h2-console/**"
        );
    }

    /**
     * PasswordEncoder
     *  - Define como vou transformar a senha antes de salvar no banco.
     *  - BCrypt gera um hash seguro. Nunca salvar senha em texto puro.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationProvider
     *  - Diz ao Spring: “para autenticar, use este UserDetailsService + este PasswordEncoder”.
     *  - O Spring usa isso internamente quando alguém faz login via /auth/login (no nosso caso, manual no service).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager
     *  - Fornece um "gerenciador" caso eu precise autenticar manualmente em algum fluxo.
     *  - Busca a configuração padrão do Spring (com nosso provider).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * SecurityFilterChain
     *  - Aqui eu configuro “como as requisições HTTP serão tratadas”.
     *  - Desligo sessão (stateless), CSRF, formulário HTML, Basic.
     *  - Defino quem pode o quê por rota (autorização).
     *  - E encaixo meu JwtAuthFilter antes do filtro padrão de login por formulário.
     *
     * Observação:
     *  - Apesar de ignorarmos /auth/** no WebSecurityCustomizer (nem passam aqui),
     *    deixo as permissões “permitAll” por clareza para quem lê.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Sem CSRF porque nossa API é stateless (cliente envia JWT a cada request)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS default (ajuste Allowed-Origin no seu CorsConfigurationSource ou via app.properties)
                .cors(Customizer.withDefaults())
                // Nada de sessão: cada request precisa de token
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Não usamos formLogin (aquele /login com tela HTML) nem HTTP Basic
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Autorização por rota
                .authorizeHttpRequests(auth -> auth
                        // públicos (redundante por causa do ignoring, mas didático)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        // Área ADMIN para o painel (Angular). IMPORTANTE: ROLE_ADMIN.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // todo o resto exige estar logado (ROLE USER ou ADMIN)
                        .anyRequest().authenticated()
                )
                // diz ao Spring como autenticar
                .authenticationProvider(authenticationProvider())
                // Meu JwtAuthFilter roda ANTES do UsernamePasswordAuthenticationFilter.
                // Assim, se o token for válido, a request já “entra” como autenticada no restante da cadeia.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Permite abrir o H2 console (usa frames)
        http.headers(h -> h.frameOptions(f -> f.disable()));

        return http.build();
    }
}
