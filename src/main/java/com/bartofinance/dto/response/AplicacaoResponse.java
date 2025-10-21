package com.bartofinance.dto.response;

import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de Aplicação
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AplicacaoResponse {

    private String id;
    private String portfolioId;
    private TipoProduto tipoProduto;
    private String codigoAtivo;
    private BigDecimal valorAplicado;
    private BigDecimal quantidade;
    private LocalDateTime dataCompra;
    private LocalDateTime dataVenda;
    private BigDecimal rentabilidadeAtual;
    private StatusAplicacao status;
    private String notas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

