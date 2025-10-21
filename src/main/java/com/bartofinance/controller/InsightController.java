package com.bartofinance.controller;

import com.bartofinance.dto.request.InsightRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InsightResponse;
import com.bartofinance.service.InsightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para geração e gerenciamento de Insights com IA
 */
@RestController
@RequestMapping("/insights")
@Tag(name = "Insights", description = "Endpoints para geração de insights com IA (Mock Gemini)")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class InsightController {

    @Autowired
    private InsightService insightService;

    /**
     * Gera um novo insight para um investidor
     */
    @PostMapping("/generate")
    @Operation(summary = "Gerar insight", description = "Gera um novo insight com IA para um investidor")
    public ResponseEntity<ApiResponse<InsightResponse>> gerarInsight(
            @Valid @RequestBody InsightRequest request) {
        
        log.info("POST /insights/generate - Gerando insight para investidor: {}", request.getInvestidorId());
        
        InsightResponse response = insightService.gerarInsight(request.getInvestidorId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Insight gerado com sucesso", response));
    }

    /**
     * Lista todos os insights de um investidor
     */
    @GetMapping
    @Operation(summary = "Listar insights", description = "Lista todos os insights gerados para um investidor")
    public ResponseEntity<ApiResponse<List<InsightResponse>>> listarInsights(
            @RequestParam String investorId) {
        
        log.info("GET /insights?investorId={} - Listando insights", investorId);
        
        List<InsightResponse> response = insightService.listarInsights(investorId);
        
        return ResponseEntity.ok(ApiResponse.success("Insights listados com sucesso", response));
    }
}

