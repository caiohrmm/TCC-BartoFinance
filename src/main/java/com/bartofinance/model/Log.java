package com.bartofinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidade representando um Log de Auditoria
 * 
 * Registra todas as ações executadas no sistema para fins de rastreamento
 * e auditoria.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    private String assessorId; // Referência ao Assessor (pode ser null para logs de sistema)

    private String acao;

    private String descricao;

    private String endpoint;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String ip;

    @Builder.Default
    private Boolean sucesso = true;
}

