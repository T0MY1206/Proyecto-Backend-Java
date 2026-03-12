package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para el monolito.
 * Separa la documentación por contexto de negocio:
 * - supermercado: APIs actuales (ventas, productos, sucursales)
 * - otros: futuras APIs de otros proyectos o mejoras
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Monolito - Prueba Técnica")
                        .version("1.0")
                        .description("Un solo despliegue con APIs separadas por contexto. " +
                                "Usa el selector de grupo en Swagger UI o la URL para elegir el contexto."));
    }

    /** Grupo Swagger: APIs del dominio Supermercado (actual) */
    @Bean
    public GroupedOpenApi supermercadoApi() {
        return GroupedOpenApi.builder()
                .group("supermercado")
                .displayName("Supermercado (actual)")
                .pathsToMatch("/api/supermercado/**")
                .build();
    }

    /** Grupo Swagger: futuras APIs de otros proyectos o mejoras */
    @Bean
    public GroupedOpenApi otrosApi() {
        return GroupedOpenApi.builder()
                .group("otros")
                .displayName("Otros / Futuras APIs")
                .pathsToMatch("/api/otro/**")
                .build();
    }
}
