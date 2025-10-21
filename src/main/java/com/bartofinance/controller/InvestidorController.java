package com.bartofinance.controller;

import com.bartofinance.dto.request.InvestidorRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InvestidorResponse;
import com.bartofinance.service.InvestidorService;
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
 * Controller para gerenciamento de Investidores
 */
@RestController
@RequestMapping("/investors")
@Tag(name = "Investidores", description = "Endpoints para gerenciamento de investidores")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class InvestidorController {

    @Autowired
    private InvestidorService investidorService;

    @Autowired
    private AuthUtil authUtil;

    /**
     * Criar novo investidor
     */
    @PostMapping
    @Operation(summary = "Criar novo investidor", description = "Cria um novo investidor para o assessor autenticado")
    public ResponseEntity<ApiResponse<InvestidorResponse>> criarInvestidor(
            @Valid @RequestBody InvestidorRequest request,
            Authentication authentication) {
        
        log.info("POST /investors - Criando novo investidor");
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.criarInvestidor(request, assessorId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Investidor criado com sucesso", response));
    }

    /**
     * Listar todos os investidores do assessor
     */
    @GetMapping
    @Operation(summary = "Listar investidores", description = "Lista todos os investidores do assessor autenticado")
    public ResponseEntity<ApiResponse<List<InvestidorResponse>>> listarInvestidores(
            Authentication authentication) {
        
        log.info("GET /investors - Listando investidores");
        String assessorId = authUtil.getAssessorId(authentication);
        
        List<InvestidorResponse> response = investidorService.listarInvestidores(assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidores listados com sucesso", response));
    }

    /**
     * Buscar investidor por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar investidor", description = "Busca um investidor específico por ID")
    public ResponseEntity<ApiResponse<InvestidorResponse>> buscarInvestidor(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("GET /investors/{} - Buscando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.buscarPorId(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor encontrado", response));
    }

    /**
     * Atualizar investidor
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar investidor", description = "Atualiza os dados de um investidor")
    public ResponseEntity<ApiResponse<InvestidorResponse>> atualizarInvestidor(
            @PathVariable String id,
            @Valid @RequestBody InvestidorRequest request,
            Authentication authentication) {
        
        log.info("PUT /investors/{} - Atualizando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.atualizarInvestidor(id, request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor atualizado com sucesso", response));
    }

    /**
     * Deletar investidor
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar investidor", description = "Remove um investidor do sistema")
    public ResponseEntity<ApiResponse<Void>> deletarInvestidor(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("DELETE /investors/{} - Deletando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        investidorService.deletarInvestidor(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor deletado com sucesso"));
    }
}

