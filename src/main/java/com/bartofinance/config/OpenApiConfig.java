package com.bartofinance.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação OpenAPI/Swagger
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "BartoFinance API",
        version = "1.0.0",
        description = "Sistema completo de assessoria de investimentos com gestão de investidores, aplicações, relatórios e insights gerados por IA",
        contact = @Contact(
            name = "BartoFinance Team",
            email = "contato@bartofinance.com",
            url = "https://bartofinance.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor Local"),
        @Server(url = "https://api.bartofinance.com", description = "Servidor de Produção")
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Token JWT obtido através do endpoint de login"
)
public class OpenApiConfig {
    // Configuração realizada através de anotações
}

