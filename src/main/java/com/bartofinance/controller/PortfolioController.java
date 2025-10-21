package com.bartofinance.controller;

import com.bartofinance.dto.request.PortfolioRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.service.PortfolioService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Portfolios", description = "Endpoints para gerenciamento de carteiras de investimento")
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
    @Operation(summary = "Criar nova carteira", description = "Cria uma nova carteira de investimentos")
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
    @Operation(summary = "Simular carteira", description = "Simula o desempenho de uma carteira hipotética")
    public ResponseEntity<ApiResponse<PortfolioResponse>> simularPortfolio(
            @Valid @RequestBody PortfolioRequest request,
            Authentication authentication) {
        
        log.info("POST /portfolios/simulate - Simulando carteira");
        String assessorId = authUtil.getAssessorId(authentication);
        
        PortfolioResponse response = portfolioService.simularPortfolio(request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Simulação realizada com sucesso", response));
    }
}

