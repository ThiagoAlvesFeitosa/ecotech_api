package br.com.fiap.ecotech.ecotech_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Configuração do Swagger (springdoc) com:
 * - título/descrição
 * - esquema de segurança "bearer-jwt" (Bearer Token no header Authorization)
 * - requisito de segurança global (aplica Bearer por padrão)
 *
 * Obs: nossas rotas /auth/** continuam públicas no servidor (ignoradas no WebSecurityCustomizer),
 * então dá pra chamá-las sem token mesmo com esse requisito declarado aqui.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        // Define o esquema Bearer JWT pro Swagger exibir o botão "Authorize"
        final String securitySchemeName = "bearer-jwt";

        SecurityScheme bearerScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        // Requisito de segurança global (pode ser omitido; se deixar, o Swagger manda o token por padrão)
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        return new OpenAPI()
                .info(new Info()
                        .title("EcoTech API")
                        .version("v1")
                        .description("API REST do projeto EcoTech/Smart HAS (FIAP)"))
                .components(new Components().addSecuritySchemes(securitySchemeName, bearerScheme))
                .addSecurityItem(securityRequirement);
    }
}
