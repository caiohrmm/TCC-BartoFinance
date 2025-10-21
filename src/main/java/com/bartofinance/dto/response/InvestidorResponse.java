package com.bartofinance.dto.response;

import com.bartofinance.model.enums.PerfilInvestidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de Investidor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestidorResponse {

    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private PerfilInvestidor perfilInvestidor;
    private BigDecimal patrimonioAtual;
    private BigDecimal rendaMensal;
    private String objetivos;
    private String assessorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

