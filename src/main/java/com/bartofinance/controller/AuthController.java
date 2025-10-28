package com.bartofinance.controller;

import com.bartofinance.dto.request.LoginRequest;
import com.bartofinance.dto.request.RegisterRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.AuthResponse;
import com.bartofinance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de autenticação
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "🔐 Autenticação", description = "Endpoints para login e registro de assessores")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registro de novo assessor
     */
    @PostMapping("/register")
    @Operation(
        summary = "📝 Registrar novo assessor",
        description = """
            ## 📋 Descrição
            
            Registra um novo assessor financeiro no sistema BartoFinance.
            
            ## 🔐 Processo
            
            1. **Validação**: Verifica se o email já existe no sistema
            2. **Criptografia**: Senha é criptografada com BCrypt
            3. **Persistência**: Dados são salvos no MongoDB
            4. **Token**: Gera token JWT válido por 24 horas
            5. **Log**: Registra ação no sistema de auditoria
            
            ## ✅ Validações
            
            - Email deve ser único no sistema
            - Nome deve ter entre 3 e 100 caracteres
            - Senha deve ter no mínimo 6 caracteres
            - Email deve ter formato válido
            
            ## 🚀 Resposta
            
            Retorna token JWT e dados do assessor criado.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do novo assessor",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Registro",
                    value = """
                        {
                          "nome": "João Silva",
                          "email": "joao.silva@bartofinance.com",
                          "senha": "minhasenha123"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "✅ Assessor registrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Sucesso",
                    value = """
                        {
                          "sucesso": true,
                          "mensagem": "Assessor registrado com sucesso!",
                          "data": {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "tipo": "Bearer",
                            "assessorId": "64f8a1b2c3d4e5f6a7b8c9d0",
                            "nome": "João Silva",
                            "email": "joao.silva@bartofinance.com",
                            "mensagem": "Assessor registrado com sucesso!"
                          },
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou email já cadastrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de Validação",
                    value = """
                        {
                          "sucesso": false,
                          "mensagem": "Email já cadastrado no sistema",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "💥 Erro interno do servidor"
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        
        logger.info("POST /auth/register - Registro de novo assessor: {}", request.getEmail());
        
        String ip = getClientIp(httpRequest);
        AuthResponse response = authService.register(request, ip);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Assessor registrado com sucesso!", response));
    }

    /**
     * Endpoint para login de assessor
     */
    @PostMapping("/login")
    @Operation(
        summary = "🔑 Login de assessor",
        description = """
            ## 📋 Descrição
            
            Autentica um assessor no sistema e retorna token JWT para acesso aos endpoints protegidos.
            
            ## 🔐 Processo
            
            1. **Validação**: Verifica se o assessor existe no sistema
            2. **Senha**: Confere senha usando BCrypt
            3. **Status**: Verifica se o assessor está ativo
            4. **Token**: Gera novo token JWT válido por 24 horas
            5. **Log**: Atualiza último login e registra ação
            
            ## ✅ Validações
            
            - Email deve existir no sistema
            - Senha deve estar correta
            - Assessor deve estar ativo
            
            ## 🚀 Resposta
            
            Retorna token JWT e dados do assessor autenticado.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciais de login",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Login",
                    value = """
                        {
                          "email": "joao.silva@bartofinance.com",
                          "senha": "minhasenha123"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Sucesso",
                    value = """
                        {
                          "sucesso": true,
                          "mensagem": "Login realizado com sucesso!",
                          "data": {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "tipo": "Bearer",
                            "assessorId": "64f8a1b2c3d4e5f6a7b8c9d0",
                            "nome": "João Silva",
                            "email": "joao.silva@bartofinance.com",
                            "mensagem": "Login realizado com sucesso!"
                          },
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "❌ Credenciais inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de Autenticação",
                    value = """
                        {
                          "sucesso": false,
                          "mensagem": "Email ou senha inválidos",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de Validação",
                    value = """
                        {
                          "sucesso": false,
                          "mensagem": "Email é obrigatório",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "💥 Erro interno do servidor"
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        logger.info("POST /auth/login - Tentativa de login: {}", request.getEmail());
        
        String ip = getClientIp(httpRequest);
        AuthResponse response = authService.login(request, ip);
        
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso!", response));
    }

    /**
     * Endpoint de health check para verificar se a API está funcionando
     */
    @GetMapping("/health")
    @Operation(
        summary = "❤️ Health Check",
        description = """
            ## 📋 Descrição
            
            Verifica se a API está funcionando corretamente.
            
            ## 🎯 Uso
            
            - Monitoramento de saúde da aplicação
            - Verificação de conectividade
            - Teste básico de funcionamento
            
            ## ✅ Resposta
            
            Retorna status "OK" se a API estiver funcionando.
            """,
        tags = {"❤️ Health"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ API funcionando normalmente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Sucesso",
                    value = """
                        {
                          "sucesso": true,
                          "mensagem": "BartoFinance API está funcionando!",
                          "data": "OK",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("BartoFinance API está funcionando!", "OK"));
    }

    /**
     * Extrai o IP do cliente da requisição
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

