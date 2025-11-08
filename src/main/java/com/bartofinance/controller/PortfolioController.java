package com.bartofinance.controller;

import com.bartofinance.dto.request.PortfolioRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.service.PortfolioService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciamento de Carteiras de Investimento
 */
@RestController
@RequestMapping("/portfolios")
@Tag(name = "💼 Carteiras", description = "Endpoints para gerenciamento de carteiras de investimento")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private AuthUtil authUtil;

    /**
     * Criar nova carteira
     */
    @PostMapping
    @Operation(
        summary = "📝 Criar nova carteira",
        description = """
            ## 📋 Descrição
            
            Cria uma nova carteira de investimentos vinculada ao assessor autenticado.
            
            ## 📊 Tipos de Carteira
            
            - **MODELO**: Template genérico, sem investidor vinculado (reutilizável)
            - **PERSONALIZADA**: Vinculada a um investidor específico (obrigatório informar `investidorId`)
            
            ## ✅ Validações
            
            - Nome: obrigatório, entre 3 e 100 caracteres, único por assessor
            - Tipo: obrigatório (MODELO ou PERSONALIZADA)
            - Risco: obrigatório (BAIXO, MEDIO, ALTO)
            - Meta de Rentabilidade: opcional, entre 0 e 100%
            - Investidor ID: obrigatório apenas se tipo = PERSONALIZADA
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados da nova carteira",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PortfolioRequest.class),
                examples = @ExampleObject(
                    name = "Carteira Personalizada",
                    value = """
                        {
                          "nome": "Carteira Maria 2025",
                          "descricao": "Carteira de investimentos para Maria Santos",
                          "tipo": "PERSONALIZADA",
                          "risco": "MEDIO",
                          "metaRentabilidade": 15.5,
                          "investidorId": "64f8a1b2c3d4e5f6a7b8c9d0"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "✅ Carteira criada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou nome já existe"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<PortfolioResponse>> criarPortfolio(
            @Valid @RequestBody PortfolioRequest request,
            Authentication authentication) {
        
        log.info("POST /portfolios - Criando nova carteira");
        String assessorId = authUtil.getAssessorId(authentication);
        
        PortfolioResponse response = portfolioService.criarPortfolio(request, assessorId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Carteira criada com sucesso", response));
    }

    /**
     * Listar todas as carteiras
     */
    @GetMapping
    @Operation(summary = "Listar carteiras", description = "Lista todas as carteiras do assessor")
    public ResponseEntity<ApiResponse<List<PortfolioResponse>>> listarPortfolios(
            Authentication authentication) {
        
        log.info("GET /portfolios - Listando carteiras");
        String assessorId = authUtil.getAssessorId(authentication);
        
        List<PortfolioResponse> response = portfolioService.listarPortfolios(assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Carteiras listadas com sucesso", response));
    }

    /**
     * Listar carteiras modelo (templates)
     */
    @GetMapping("/models")
    @Operation(summary = "Listar carteiras modelo", description = "Lista todas as carteiras modelo disponíveis")
    public ResponseEntity<ApiResponse<List<PortfolioResponse>>> listarPortfoliosModelo() {
        
        log.info("GET /portfolios/models - Listando carteiras modelo");
        
        List<PortfolioResponse> response = portfolioService.listarPortfoliosModelo();
        
        return ResponseEntity.ok(ApiResponse.success("Carteiras modelo listadas", response));
    }

    /**
     * Buscar carteira por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar carteira", description = "Busca uma carteira específica por ID")
    public ResponseEntity<ApiResponse<PortfolioResponse>> buscarPortfolio(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("GET /portfolios/{} - Buscando carteira", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        PortfolioResponse response = portfolioService.buscarPorId(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Carteira encontrada", response));
    }

    /**
     * Atualizar carteira
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar carteira", description = "Atualiza os dados de uma carteira")
    public ResponseEntity<ApiResponse<PortfolioResponse>> atualizarPortfolio(
            @PathVariable String id,
            @Valid @RequestBody PortfolioRequest request,
            Authentication authentication) {
        
        log.info("PUT /portfolios/{} - Atualizando carteira", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        PortfolioResponse response = portfolioService.atualizarPortfolio(id, request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Carteira atualizada com sucesso", response));
    }

    /**
     * Deletar carteira
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar carteira", description = "Remove uma carteira do sistema")
    public ResponseEntity<ApiResponse<Void>> deletarPortfolio(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("DELETE /portfolios/{} - Deletando carteira", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        portfolioService.deletarPortfolio(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Carteira deletada com sucesso"));
    }

    /**
     * Simular desempenho de carteira
     */
    @PostMapping("/simulate")
    @Operation(
        summary = "🎯 Simular carteira",
        description = """
            ## 📋 Descrição
            
            Simula o desempenho de uma carteira hipotética **sem salvar** no banco de dados.
            
            ## 🎯 Uso
            
            - Testar configurações antes de criar definitivamente
            - Avaliar diferentes cenários de investimento
            - Calcular rentabilidade estimada
            
            ## 📊 Resposta
            
            Retorna carteira simulada com rentabilidade estimada (mock: 80% da meta).
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados da carteira para simulação",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PortfolioRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Simulação realizada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<PortfolioResponse>> simularPortfolio(
            @Valid @RequestBody PortfolioRequest request,
            Authentication authentication) {
        
        log.info("POST /portfolios/simulate - Simulando carteira");
        String assessorId = authUtil.getAssessorId(authentication);
        
        PortfolioResponse response = portfolioService.simularPortfolio(request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Simulação realizada com sucesso", response));
    }
}

