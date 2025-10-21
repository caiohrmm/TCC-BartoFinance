package com.bartofinance.dto.request;

import com.bartofinance.model.enums.RiscoCarteira;
import com.bartofinance.model.enums.TipoCarteira;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação/atualização de Carteira de Investimentos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest {

    @NotBlank(message = "Nome da carteira é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "Tipo de carteira é obrigatório")
    private TipoCarteira tipo;

    @NotNull(message = "Nível de risco é obrigatório")
    private RiscoCarteira risco;

    @DecimalMin(value = "0.0", message = "Meta de rentabilidade deve ser maior ou igual a zero")
    @DecimalMax(value = "100.0", message = "Meta de rentabilidade deve ser menor ou igual a 100")
    private BigDecimal metaRentabilidade;

    private String investidorId; // Obrigatório apenas se tipo = PERSONALIZADA
}

