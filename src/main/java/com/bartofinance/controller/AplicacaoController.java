package com.bartofinance.controller;

import com.bartofinance.dto.request.AplicacaoRequest;
import com.bartofinance.dto.response.AplicacaoResponse;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.service.AplicacaoService;
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
 * Controller para gerenciamento de Aplicações Financeiras
 */
@RestController
@RequestMapping("/applications")
@Tag(name = "Aplicações", description = "Endpoints para gerenciamento de aplicações financeiras")
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
    @Operation(summary = "Listar aplicações", description = "Lista aplicações de um portfolio, com filtros opcionais")
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
    @Operation(summary = "Encerrar aplicação", description = "Registra a venda/encerramento de uma aplicação")
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

