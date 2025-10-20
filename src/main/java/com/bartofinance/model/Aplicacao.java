package com.bartofinance.model;

import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade representando uma Aplicação Financeira
 * 
 * Registra os investimentos realizados por um investidor em diferentes
 * produtos financeiros.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aplicacoes")
public class Aplicacao {

    @Id
    private String id;

    private String investidorId; // Referência ao Investidor

    private TipoProduto tipoProduto;

    @Builder.Default
    private BigDecimal valorAplicado = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal rentabilidadeEsperada = BigDecimal.ZERO;

    private LocalDateTime dataAplicacao;

    private LocalDateTime dataResgate; // Opcional - pode ser null se ainda não resgatado

    @Builder.Default
    private StatusAplicacao status = StatusAplicacao.ATIVA;

    private String notas;
}

