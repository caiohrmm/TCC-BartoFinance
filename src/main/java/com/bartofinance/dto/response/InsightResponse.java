package com.bartofinance.dto.response;

import com.bartofinance.model.enums.TipoInsight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de Insight
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponse {

    private String id;
    private String investidorId;
    private String texto;
    private String geradoPor;
    private TipoInsight tipo;
    private LocalDateTime dataGeracao;
}

