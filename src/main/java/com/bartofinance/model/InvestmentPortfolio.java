package com.bartofinance.model;

import com.bartofinance.model.enums.RiscoCarteira;
import com.bartofinance.model.enums.TipoCarteira;
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
 * Entidade representando uma Carteira de Investimentos
 * 
 * Uma carteira pode ser modelo (template) ou personalizada para um investidor específico.
 * Contém múltiplas aplicações e acompanha o desempenho.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "portfolios")
public class InvestmentPortfolio {

    @Id
    private String id;

    private String nome;

    private String descricao;

    private TipoCarteira tipo; // MODELO ou PERSONALIZADA

    private RiscoCarteira risco; // BAIXO, MODERADO, ALTO

    @Builder.Default
    private BigDecimal metaRentabilidade = BigDecimal.ZERO; // Meta de rentabilidade anual (%)

    @Builder.Default
    private BigDecimal rentabilidadeAtual = BigDecimal.ZERO; // Rentabilidade atual calculada

    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO; // Valor total aplicado

    private String investidorId; // Nullable - se for carteira modelo, fica null

    private String assessorId; // Assessor responsável

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

