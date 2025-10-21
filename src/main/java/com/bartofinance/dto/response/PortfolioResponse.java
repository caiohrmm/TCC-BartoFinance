package com.bartofinance.dto.response;

import com.bartofinance.model.enums.RiscoCarteira;
import com.bartofinance.model.enums.TipoCarteira;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de Carteira de Investimentos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {

    private String id;
    private String nome;
    private String descricao;
    private TipoCarteira tipo;
    private RiscoCarteira risco;
    private BigDecimal metaRentabilidade;
    private BigDecimal rentabilidadeAtual;
    private BigDecimal valorTotal;
    private String investidorId;
    private String assessorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

