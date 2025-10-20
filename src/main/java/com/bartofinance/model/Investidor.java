package com.bartofinance.model;

import com.bartofinance.model.enums.PerfilInvestidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade representando um Investidor
 * 
 * Cliente do assessor que possui perfil de investimento, patrimônio
 * e aplicações financeiras.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "investidores")
public class Investidor {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String cpf;

    private String email;

    private String telefone;

    private PerfilInvestidor perfilInvestidor;

    @Builder.Default
    private BigDecimal patrimonio = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal rendaMensal = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime dataCadastro;

    private String assessorId; // Referência ao Assessor responsável
}

