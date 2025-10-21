package com.bartofinance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de geração de Insight
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightRequest {

    @NotBlank(message = "Investidor ID é obrigatório")
    private String investidorId;
}

