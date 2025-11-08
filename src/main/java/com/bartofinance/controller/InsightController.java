package com.bartofinance.controller;

import com.bartofinance.dto.request.InsightRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InsightResponse;
import com.bartofinance.service.InsightService;
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
 * Controller para geração e gerenciamento de Insights com IA
 */
@RestController
@RequestMapping("/insights")
@Tag(name = "🤖 Insights", description = "Endpoints para geração de insights com IA (Mock Gemini)")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class InsightController {

    @Autowired
    private InsightService insightService;
    
    @Autowired
    private AuthUtil authUtil;

    /**
     * Gera um novo insight para um investidor
     */
    @PostMapping("/generate")
    @Operation(
        summary = "✨ Gerar insight para investidor",
        description = """
            ## 📋 Descrição
            
            Gera um novo insight personalizado para um investidor usando IA (Mock Gemini).
            
            ## 🎯 Tipos de Insight
            
            - **RECOMENDACAO_PORTFOLIO**: Sugestões de alocação de ativos
            - **ANALISE_RISCO**: Análise de exposição a risco
            - **ALERTA_MERCADO**: Alertas sobre cenário econômico
            - **OPORTUNIDADE**: Oportunidades de investimento
            
            ## 📊 Processo
            
            1. Analisa o perfil do investidor
            2. Gera texto personalizado (mock simula IA Gemini)
            3. Salva no banco com timestamp
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para geração do insight",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InsightRequest.class),
                examples = @ExampleObject(
                    name = "Gerar Insight",
                    value = """
                        {
                          "investidorId": "64f8a1b2c3d4e5f6a7b8c9d0",
                          "tipo": "RECOMENDACAO_PORTFOLIO"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "✅ Insight gerado com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou investidor não encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<InsightResponse>> gerarInsight(
            @Valid @RequestBody InsightRequest request,
            Authentication authentication) {
        
        log.info("POST /insights/generate - Gerando insight para investidor: {}", request.getInvestidorId());
        String assessorId = authUtil.getAssessorId(authentication);
        
        InsightResponse response = insightService.gerarInsight(request.getInvestidorId(), assessorId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Insight gerado com sucesso", response));
    }

    /**
     * Lista todos os insights de um investidor
     */
    @GetMapping
    @Operation(
        summary = "📋 Listar insights",
        description = """
            ## 📋 Descrição
            
            Lista todos os insights gerados para um investidor específico.
            
            ## 🔐 Segurança
            
            Valida se o investidor pertence ao assessor autenticado.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "investorId",
                description = "ID do investidor",
                required = true,
                example = "64f8a1b2c3d4e5f6a7b8c9d0"
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Insights listados com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Investidor não pertence a este assessor"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<List<InsightResponse>>> listarInsights(
            @RequestParam String investorId,
            Authentication authentication) {
        
        log.info("GET /insights?investorId={} - Listando insights", investorId);
        String assessorId = authUtil.getAssessorId(authentication);
        
        List<InsightResponse> response = insightService.listarInsights(investorId, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Insights listados com sucesso", response));
    }
}

