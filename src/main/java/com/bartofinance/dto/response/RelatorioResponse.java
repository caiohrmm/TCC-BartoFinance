package com.bartofinance.dto.response;

import com.bartofinance.model.enums.TipoRelatorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para resposta de Relatório
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioResponse {

    private String id;
    private TipoRelatorio tipo;
    private String referenciaId;
    private Map<String, Object> dadosResumo;
    private BigDecimal totalAplicado;
    private BigDecimal rendimento;
    private String criadoPor;
    private LocalDateTime createdAt;
}

