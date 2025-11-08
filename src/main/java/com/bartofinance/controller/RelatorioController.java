package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InvestidorRelatorioResponse;
import com.bartofinance.service.RelatorioService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reports")
@Tag(name = "Relatórios", description = "Endpoints para relatórios e estatísticas")
@SecurityRequirement(name = "Bearer Authentication")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;
    
    @Autowired
    private AuthUtil authUtil;

    /**
     * Gera relatório completo do investidor
     * GET /reports/investor/{investorId}
     */
    @GetMapping("/investor/{investorId}")
    @Operation(
        summary = "📊 Gerar relatório do investidor",
        description = """
            ## 📋 Descrição
            
            Gera relatório completo com estatísticas, alertas e recomendações para um investidor.
            
            ## 📈 Conteúdo do Relatório
            
            - Estatísticas gerais (patrimônio, renda, perfil)
            - Resumo de carteiras e aplicações
            - Análise de rentabilidade
            - Alertas e recomendações
            
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
            description = "✅ Relatório gerado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Investidor não pertence a este assessor"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "❌ Investidor não encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<InvestidorRelatorioResponse>> 
            gerarRelatorioInvestidor(
                @PathVariable String investorId,
                Authentication authentication
            ) {
        String assessorId = authUtil.getAssessorId(authentication);
        
        log.info("Gerando relatório para investidor: {}, assessor: {}", investorId, assessorId);
        
        InvestidorRelatorioResponse relatorio = relatorioService.gerarRelatorioInvestidor(investorId, assessorId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Relatório gerado com sucesso", relatorio)
        );
    }
}

