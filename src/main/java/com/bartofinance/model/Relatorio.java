package com.bartofinance.model;

import com.bartofinance.model.enums.TipoRelatorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entidade representando um Relatório de Investimentos
 * 
 * Consolida informações sobre investidores ou carteiras.
 * Armazena dados resumidos para consultas rápidas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "relatorios")
public class Relatorio {

    @Id
    private String id;

    private TipoRelatorio tipo; // INVESTIDOR ou CARTEIRA

    private String referenciaId; // ID do investidor ou carteira

    private Map<String, Object> dadosResumo; // Dados flexíveis em formato JSON

    @Builder.Default
    private BigDecimal totalAplicado = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal rendimento = BigDecimal.ZERO;

    private String criadoPor; // ID do assessor

    @CreatedDate
    private LocalDateTime createdAt;
}

