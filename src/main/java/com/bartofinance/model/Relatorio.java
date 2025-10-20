package com.bartofinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade representando um Relatório de Investimentos
 * 
 * Consolida informações sobre aplicações e rendimentos de um investidor
 * em um período específico.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "relatorios")
public class Relatorio {

    @Id
    private String id;

    private String investidorId; // Referência ao Investidor

    private LocalDateTime periodoInicio;

    private LocalDateTime periodoFim;

    @Builder.Default
    private BigDecimal totalAplicado = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalRendimento = BigDecimal.ZERO;

    private String observacoes;

    private LocalDateTime geradoEm;

    @Builder.Default
    private Boolean insightsGerados = false;
}

