package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InvestidorRelatorioResponse;
import com.bartofinance.service.RelatorioService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Gerar relatório do investidor", description = "Gera relatório completo com estatísticas, alertas e recomendações")
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

