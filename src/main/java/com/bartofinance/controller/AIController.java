package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.service.GeminiAIService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller para endpoints de IA (Gemini)
 */
@RestController
@RequestMapping("/ai")
@Tag(name = "Inteligência Artificial", description = "Endpoints para análises e insights com IA")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class AIController {

    @Autowired
    private GeminiAIService geminiAIService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/analisar-perfil")
    @Operation(
        summary = "🤖 Análise de perfil de investidor com IA",
        description = """
            ## 📋 Descrição
            
            Gera análise personalizada do perfil de investidor usando Google Gemini AI.
            
            ## 🎯 Uso
            
            Analisa características do investidor e fornece recomendações baseadas em:
            - Perfil de risco (CONSERVADOR, MODERADO, AGRESSIVO)
            - Renda mensal
            - Patrimônio atual
            
            ## 📊 Resposta
            
            Retorna análise textual gerada pela IA.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do investidor para análise",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Análise de Perfil",
                    value = """
                        {
                          "nome": "Maria Santos",
                          "perfil": "MODERADO",
                          "rendaMensal": 8000.00,
                          "patrimonioAtual": 50000.00
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Análise gerada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Erro ao gerar análise"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> analisarPerfil(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        try {
            String nome = (String) request.get("nome");
            String perfil = (String) request.get("perfil");
            Double rendaMensal = ((Number) request.get("rendaMensal")).doubleValue();
            Double patrimonioAtual = ((Number) request.get("patrimonioAtual")).doubleValue();

            String analise = geminiAIService.analisarPerfilInvestidor(nome, perfil, rendaMensal, patrimonioAtual);

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                    .sucesso(true)
                    .mensagem("Análise gerada com sucesso")
                    .data(Map.of("analise", analise))
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Erro ao analisar perfil", e);
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, String>>builder()
                    .sucesso(false)
                    .mensagem("Erro ao gerar análise: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/analisar-carteira")
    @Operation(
        summary = "📊 Análise de carteira com IA",
        description = """
            ## 📋 Descrição
            
            Gera análise completa de uma carteira de investimentos usando Google Gemini AI.
            
            ## 🎯 Uso
            
            Analisa desempenho e fornece recomendações baseadas em:
            - Nome e tipo da carteira
            - Nível de risco
            - Valor total investido
            - Rentabilidade atual vs. meta
            
            ## 📊 Resposta
            
            Retorna análise textual com recomendações de ajustes.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados da carteira para análise",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Análise de Carteira",
                    value = """
                        {
                          "nomeCarteira": "Carteira Maria 2025",
                          "tipoCarteira": "PERSONALIZADA",
                          "riscoCarteira": "MEDIO",
                          "valorTotal": 50000.00,
                          "rentabilidadeAtual": 12.5,
                          "metaRentabilidade": 15.0
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Análise gerada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Erro ao gerar análise"
        )
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> analisarCarteira(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        try {
            String nomeCarteira = (String) request.get("nomeCarteira");
            String tipoCarteira = (String) request.get("tipoCarteira");
            String riscoCarteira = (String) request.get("riscoCarteira");
            Double valorTotal = ((Number) request.get("valorTotal")).doubleValue();
            Double rentabilidadeAtual = ((Number) request.get("rentabilidadeAtual")).doubleValue();
            Double metaRentabilidade = ((Number) request.get("metaRentabilidade")).doubleValue();

            String analise = geminiAIService.analisarCarteira(
                nomeCarteira, tipoCarteira, riscoCarteira, 
                valorTotal, rentabilidadeAtual, metaRentabilidade
            );

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                    .sucesso(true)
                    .mensagem("Análise gerada com sucesso")
                    .data(Map.of("analise", analise))
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Erro ao analisar carteira", e);
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, String>>builder()
                    .sucesso(false)
                    .mensagem("Erro ao gerar análise: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/sugerir-diversificacao")
    @Operation(
        summary = "💡 Sugestão de diversificação",
        description = """
            ## 📋 Descrição
            
            Gera sugestões de diversificação de carteira usando Google Gemini AI.
            
            ## 🎯 Uso
            
            Fornece recomendações de alocação baseadas em:
            - Perfil do investidor
            - Valor disponível para investimento
            
            ## 📊 Resposta
            
            Retorna sugestões de distribuição de ativos.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para sugestão de diversificação",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Sugestão de Diversificação",
                    value = """
                        {
                          "perfilInvestidor": "MODERADO",
                          "valorDisponivel": 10000.00
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Sugestão gerada com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Erro ao gerar sugestão"
        )
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> sugerirDiversificacao(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        try {
            String perfilInvestidor = (String) request.get("perfilInvestidor");
            Double valorDisponivel = ((Number) request.get("valorDisponivel")).doubleValue();

            String sugestao = geminiAIService.sugerirDiversificacao(perfilInvestidor, valorDisponivel);

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                    .sucesso(true)
                    .mensagem("Sugestão gerada com sucesso")
                    .data(Map.of("sugestao", sugestao))
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Erro ao gerar sugestão", e);
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, String>>builder()
                    .sucesso(false)
                    .mensagem("Erro ao gerar sugestão: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/gerar-insight")
    @Operation(
        summary = "💭 Gerar insight genérico com IA",
        description = """
            ## 📋 Descrição
            
            Gera um insight genérico usando Google Gemini AI baseado em um prompt personalizado.
            
            ## 🎯 Uso
            
            Permite criar insights customizados sobre qualquer tópico relacionado a investimentos.
            
            ## 📊 Resposta
            
            Retorna insight textual gerado pela IA.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Prompt para geração do insight",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Gerar Insight",
                    value = """
                        {
                          "prompt": "Analise as tendências do mercado de ações brasileiro em 2024"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Insight gerado com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Erro ao gerar insight"
        )
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> gerarInsight(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        try {
            String prompt = (String) request.get("prompt");
            String insight = geminiAIService.generateContent(prompt);

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                    .sucesso(true)
                    .mensagem("Insight gerado com sucesso")
                    .data(Map.of("insight", insight))
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Erro ao gerar insight", e);
            return ResponseEntity.badRequest().body(ApiResponse.<Map<String, String>>builder()
                    .sucesso(false)
                    .mensagem("Erro ao gerar insight: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }
}

