package com.bartofinance.model;

import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade representando uma Aplicação Financeira
 * 
 * Registra os investimentos realizados dentro de uma carteira.
 * Cada aplicação está vinculada a um portfolio específico.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aplicacoes")
public class Aplicacao {

    @Id
    private String id;

    private String portfolioId; // Referência à Carteira

    private TipoProduto tipoProduto;

    private String codigoAtivo; // Ex: PETR4, VALE3, LCI123

    @Builder.Default
    private BigDecimal valorAplicado = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal quantidade = BigDecimal.ZERO; // Quantidade de cotas/ações

    private LocalDateTime dataCompra;

    private LocalDateTime dataVenda; // Opcional - null se ainda não vendido

    @Builder.Default
    private BigDecimal rentabilidadeAtual = BigDecimal.ZERO; // Percentual ou valor

    @Builder.Default
    private StatusAplicacao status = StatusAplicacao.ATIVA;

    private String notas;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

