package com.bartofinance.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração completa da documentação OpenAPI/Swagger para BartoFinance
 * 
 * Esta configuração define todas as informações da API, incluindo:
 * - Informações gerais da API
 * - Servidores disponíveis
 * - Esquemas de segurança
 * - Tags para organização dos endpoints
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "BartoFinance API",
        version = "1.0.0",
        description = """
            # 🏦 BartoFinance - Sistema de Assessoria de Investimentos
            
            Sistema completo para gestão de assessoria financeira com as seguintes funcionalidades:
            
            ## 🎯 Principais Recursos
            
            - **👥 Gestão de Assessores**: Cadastro e autenticação de assessores financeiros
            - **📊 Gestão de Investidores**: CRUD completo de clientes com perfis de risco
            - **💼 Carteiras de Investimento**: Criação de carteiras modelo e personalizadas
            - **💰 Aplicações Financeiras**: Registro de investimentos em diferentes produtos
            - **🤖 Inteligência Artificial**: Análises e insights gerados pelo Google Gemini AI
            - **📈 Relatórios**: Geração de relatórios consolidados
            - **🔍 Auditoria**: Sistema completo de logs para rastreamento
            
            ## 🔐 Autenticação
            
            A API utiliza autenticação JWT (JSON Web Token). Para acessar endpoints protegidos:
            1. Faça login através do endpoint `/auth/login`
            2. Use o token retornado no header `Authorization: Bearer <token>`
            
            ## 📚 Documentação
            
            - **Swagger UI**: `/swagger-ui.html`
            - **OpenAPI JSON**: `/api-docs`
            - **Health Check**: `/health`
            
            ## 🚀 Tecnologias
            
            - Spring Boot 3.2.0
            - Java 17
            - MongoDB
            - JWT Authentication
            - Google Gemini AI
            - Swagger/OpenAPI 3
            """,
        contact = @Contact(
            name = "BartoFinance Development Team",
            email = "dev@bartofinance.com",
            url = "https://bartofinance.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080", 
            description = "🖥️ Servidor de Desenvolvimento Local"
        ),
        @Server(
            url = "https://api-dev.bartofinance.com", 
            description = "🧪 Servidor de Desenvolvimento"
        ),
        @Server(
            url = "https://api.bartofinance.com", 
            description = "🚀 Servidor de Produção"
        )
    },
    tags = {
        @Tag(name = "🔐 Autenticação", description = "Endpoints para login e registro de assessores"),
        @Tag(name = "👥 Investidores", description = "Gestão completa de investidores/clientes"),
        @Tag(name = "💼 Carteiras", description = "Gestão de carteiras de investimento"),
        @Tag(name = "💰 Aplicações", description = "Registro de aplicações financeiras"),
        @Tag(name = "🤖 Inteligência Artificial", description = "Análises e insights com IA"),
        @Tag(name = "📊 Relatórios", description = "Geração de relatórios consolidados"),
        @Tag(name = "🔍 Logs", description = "Consulta de logs de auditoria"),
        @Tag(name = "❤️ Health", description = "Monitoramento da saúde da API")
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = """
        ## 🔑 Autenticação JWT
        
        Para acessar endpoints protegidos, você precisa:
        
        1. **Fazer Login**: Use o endpoint `POST /auth/login` com email e senha
        2. **Obter Token**: A resposta incluirá um token JWT válido por 24 horas
        3. **Incluir Token**: Adicione o header `Authorization: Bearer <token>` em todas as requisições
        
        ### Exemplo de Uso:
        ```
        Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        ```
        
        ### ⚠️ Importante:
        - Tokens expiram em 24 horas
        - Mantenha o token seguro e não o compartilhe
        - Use HTTPS em produção
        """
)
public class OpenApiConfig {
    // Configuração realizada através de anotações OpenAPI
}