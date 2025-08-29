package br.com.fiap.ecotech.ecotech_api.config;

import br.com.fiap.ecotech.ecotech_api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Auth base
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

// HTTP / cadeia de filtros
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

// User details + password encoder
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Filtro padrão para posicionar o nosso antes
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// *** IMPORT CERTO para ignorar rotas do filtro de segurança ***
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/*
 * Segurança da API em modo stateless:
 * - Ignora completamente /auth/**, Swagger e H2 (não passam pelo filtro de segurança)
 * - Demais rotas passam pela cadeia e exigem JWT
 * - Sem formulário de login e sem HTTP Basic (acabou a telinha HTML)
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Ignora TOTALMENTE essas rotas da cadeia de segurança.
     * Motivo: garantir que /auth/** (e Swagger/H2) fiquem realmente públicos
     * e evitar qualquer 403 teimoso antes do controller.
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

    /** Hash seguro de senha. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Diz ao Spring como autenticar (nosso UserDetailsService + BCrypt). */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /** Manager obtido da configuração (caso precise autenticar programaticamente). */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * Cadeia de filtros/autorizações para as rotas que NÃO foram ignoradas.
     * Aqui eu:
     * - deixo a app stateless (sem sessão),
     * - desligo CSRF, formLogin e httpBasic (nada de tela HTML),
     * - exijo autenticação no que sobrou,
     * - e coloco meu JwtAuthFilter antes do filtro padrão.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Estas permissões aqui são redundantes porque já ignoramos,
                        // mas deixo por clareza para quem ler.
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        // Qualquer outra rota exige token válido
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Necessário para abrir o console do H2 (usa frames)
        http.headers(h -> h.frameOptions(f -> f.disable()));

        return http.build();
    }
}
