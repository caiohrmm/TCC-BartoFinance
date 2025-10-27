package com.bartofinance.dto.response;

import com.bartofinance.model.enums.PerfilInvestidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para relatório completo do investidor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestidorRelatorioResponse {

    // Dados básicos do investidor
    private String id;
    private String nome;
    private String cpf;
    private String email;
    private PerfilInvestidor perfilInvestidor;
    private BigDecimal patrimonioAtual;
    private BigDecimal rendaMensal;
    private String objetivos;
    
    // Estatísticas gerais
    @Builder.Default
    private Integer totalCarteiras = 0;
    
    @Builder.Default
    private Integer totalAplicacoes = 0;
    
    @Builder.Default
    private BigDecimal valorTotalInvestido = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal rentabilidadeMedia = BigDecimal.ZERO;
    
    // Estatísticas por tipo de produto
    private BigDecimal valorEmAcoes;
    private BigDecimal valorEmFII;
    private BigDecimal valorEmRendaFixa;
    private BigDecimal valorEmFundos;
    private BigDecimal valorEmCripto;
    private BigDecimal valorEmOutros;
    
    // Estatísticas por status
    private Integer aplicacoesAtivas;
    private Integer aplicacoesEncerradas;
    private Integer aplicacoesResgatadas;
    
    // Alertas e recomendações
    private String nivelAlerta; // BAIXO, MEDIO, ALTO
    private String recomendacaoPrincipal;
    
    // Metadados
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

