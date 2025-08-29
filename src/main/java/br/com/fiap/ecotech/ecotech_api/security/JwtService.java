package br.com.fiap.ecotech.ecotech_api.security;

import io.jsonwebtoken.Claims;                       // payload do JWT
import io.jsonwebtoken.Jws;                          // JWT assinado e validado
import io.jsonwebtoken.Jwts;                         // utilitário principal
import io.jsonwebtoken.security.Keys;                // cria chaves HMAC a partir do segredo
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;                       // usar SecretKey para HMAC
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/*
 * Serviço responsável por GERAR e VALIDAR JWT com jjwt 0.12.x.
 * Mudanças vs 0.11.x:
 *  - parserBuilder() -> parser().verifyWith(key).build()
 *  - parseClaimsJws() -> parseSignedClaims()
 *  - HMAC requer SecretKey (javax.crypto.SecretKey)
 */
@Service
public class JwtService {

    @Value("${ecotech.security.jwt-secret}")
    private String jwtSecret;

    @Value("${ecotech.security.jwt-expiration-minutes}")
    private long expirationMinutes;

    // Converte a string do segredo em SecretKey HMAC (>= 32 bytes para HS256)
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Gera token com sub=email, claims extras e HS256
    public String generateToken(String subjectEmail, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .subject(subjectEmail)                    // "sub"
                .claims(extraClaims)                      // claims adicionais
                .issuedAt(Date.from(now))                 // "iat"
                .expiration(Date.from(exp))               // "exp"
                // Pode omitir o algoritmo (é inferido pela key) ou declarar explicitamente:
                // .signWith(signingKey())
                .signWith(signingKey(), Jwts.SIG.HS256)   // assina com HS256 explicitamente
                .compact();
    }

    // Extrai o subject validando assinatura/expiração
    public String extractSubject(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(signingKey())                 // verifica com SecretKey
                .build()
                .parseSignedClaims(token);                // valida e retorna (header + claims + assinatura)

        return jws.getPayload().getSubject();
    }

    // Validação simples: se não lançar exceção, é válido
    public boolean isValid(String token) {
        Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token);                // lança se inválido/expirado
        return true;
    }
}
