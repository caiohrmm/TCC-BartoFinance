package com.bartofinance.model;

import com.bartofinance.model.enums.TipoInsight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidade representando um Insight gerado por IA
 * 
 * Armazena análises e sugestões automáticas geradas pela IA Gemini
 * com base nos dados dos investidores.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "insights")
public class Insight {

    @Id
    private String id;

    private String investidorId; // Referência ao Investidor

    private String textoInsight;

    @Builder.Default
    private String geradoPor = "Gemini AI";

    private LocalDateTime dataGeracao;

    private TipoInsight tipoInsight;
}

