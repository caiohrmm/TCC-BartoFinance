package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.service.GeminiAIService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Análise de perfil de investidor com IA")
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
    @Operation(summary = "Análise de carteira de investimentos com IA")
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
    @Operation(summary = "Sugestão de diversificação de carteira")
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
    @Operation(summary = "Gera um insight genérico usando IA")
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

