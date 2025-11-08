package com.bartofinance.controller;

import com.bartofinance.dto.request.AplicacaoRequest;
import com.bartofinance.dto.response.AplicacaoResponse;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.service.AplicacaoService;
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
 * Controller para gerenciamento de Aplicações Financeiras
 */
@RestController
@RequestMapping("/applications")
@Tag(name = "💰 Aplicações", description = "Endpoints para gerenciamento de aplicações financeiras")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class AplicacaoController {

    @Autowired
    private AplicacaoService aplicacaoService;

    @Autowired
    private AuthUtil authUtil;

    /**
     * Criar nova aplicação
     */
    @PostMapping
    @Operation(summary = "Criar nova aplicação", description = "Registra uma nova aplicação financeira em uma carteira")
    public ResponseEntity<ApiResponse<AplicacaoResponse>> criarAplicacao(
            @Valid @RequestBody AplicacaoRequest request,
            Authentication authentication) {
        
        log.info("POST /applications - Criando nova aplicação no portfolio {}", request.getPortfolioId());
        String assessorId = authUtil.getAssessorId(authentication);
        
        AplicacaoResponse response = aplicacaoService.criarAplicacao(request, assessorId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Aplicação criada com sucesso", response));
    }

    /**
     * Listar aplicações por portfolio
     */
    @GetMapping
    @Operation(
        summary = "📋 Listar aplicações",
        description = """
            ## 📋 Descrição
            
            Lista aplicações financeiras com filtros opcionais.
            
            ## 🔍 Filtros Disponíveis
            
            - **portfolioId** (opcional): Filtra por carteira específica
            - **status** (opcional): Filtra por status (ATIVA, VENDIDA, VENCIDA)
            - Filtros podem ser combinados
            
            ## 📊 Exemplos
            
            - `/applications` - Todas as aplicações do assessor
            - `/applications?portfolioId=xxx` - Aplicações de uma carteira
            - `/applications?status=ATIVA` - Apenas aplicações ativas
            - `/applications?portfolioId=xxx&status=ATIVA` - Filtros combinados
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "portfolioId",
                description = "ID da carteira para filtrar",
                example = "64f8a1b2c3d4e5f6a7b8c9d0"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "status",
                description = "Status da aplicação",
                example = "ATIVA",
                schema = @Schema(allowableValues = {"ATIVA", "VENDIDA", "VENCIDA"})
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Aplicações listadas com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<List<AplicacaoResponse>>> listarAplicacoes(
            @RequestParam(required = false) String portfolioId,
            @RequestParam(required = false) String status,
            Authentication authentication) {
        
        log.info("GET /applications - portfolioId={}, status={}", portfolioId, status);
        String assessorId = authUtil.getAssessorId(authentication);
        
        List<AplicacaoResponse> response;
        
        if (portfolioId != null && status != null) {
            response = aplicacaoService.listarPorPortfolioEStatus(portfolioId, status, assessorId);
        } else if (portfolioId != null) {
            response = aplicacaoService.listarPorPortfolio(portfolioId, assessorId);
        } else if (status != null) {
            response = aplicacaoService.listarPorStatus(status, assessorId);
        } else {
            response = aplicacaoService.listarTodas(assessorId);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Aplicações listadas com sucesso", response));
    }

    /**
     * Buscar aplicação por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aplicação", description = "Busca uma aplicação específica por ID")
    public ResponseEntity<ApiResponse<AplicacaoResponse>> buscarAplicacao(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("GET /applications/{} - Buscando aplicação", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        AplicacaoResponse response = aplicacaoService.buscarPorId(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Aplicação encontrada", response));
    }

    /**
     * Atualizar aplicação
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aplicação", description = "Atualiza os dados de uma aplicação")
    public ResponseEntity<ApiResponse<AplicacaoResponse>> atualizarAplicacao(
            @PathVariable String id,
            @Valid @RequestBody AplicacaoRequest request,
            Authentication authentication) {
        
        log.info("PUT /applications/{} - Atualizando aplicação", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        AplicacaoResponse response = aplicacaoService.atualizarAplicacao(id, request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Aplicação atualizada com sucesso", response));
    }

    /**
     * Deletar aplicação
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar aplicação", description = "Remove uma aplicação do sistema")
    public ResponseEntity<ApiResponse<Void>> deletarAplicacao(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("DELETE /applications/{} - Deletando aplicação", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        aplicacaoService.deletarAplicacao(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Aplicação deletada com sucesso"));
    }

    /**
     * Encerrar aplicação (registrar venda)
     */
    @PatchMapping("/{id}/encerrar")
    @Operation(
        summary = "🔚 Encerrar aplicação",
        description = """
            ## 📋 Descrição
            
            Registra a venda/encerramento de uma aplicação financeira.
            
            ## 🔄 Processo
            
            1. Atualiza status para `ENCERRADA`
            2. Registra data de venda
            3. Atualiza rentabilidade final
            4. Atualiza estatísticas da carteira
            
            ## ✅ Validações
            
            - Aplicação deve estar ATIVA
            - Data de venda deve ser posterior à data de compra
            - Data de venda não pode ser no futuro
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "id",
                description = "ID da aplicação a encerrar",
                required = true,
                example = "64f8a1b2c3d4e5f6a7b8c9d0"
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do encerramento",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Encerrar Aplicação",
                    value = """
                        {
                          "dataVenda": "2024-12-31T10:00:00",
                          "rentabilidadeAtual": 12.5
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Aplicação encerrada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou aplicação já encerrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "❌ Aplicação não encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<AplicacaoResponse>> encerrarAplicacao(
            @PathVariable String id,
            @RequestBody java.util.Map<String, Object> payload,
            Authentication authentication) {
        
        log.info("PATCH /applications/{}/encerrar - Encerrando aplicação", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        String dataVenda = (String) payload.get("dataVenda");
        Double rentabilidadeFinal = ((Number) payload.get("rentabilidadeAtual")).doubleValue();
        
        AplicacaoResponse response = aplicacaoService.encerrarAplicacao(id, dataVenda, rentabilidadeFinal, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Aplicação encerrada com sucesso", response));
    }
}

