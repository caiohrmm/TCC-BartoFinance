package com.bartofinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidade representando um Log de Auditoria
 * 
 * Registra todas as ações executadas no sistema para fins de rastreamento
 * e auditoria via AOP.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    private String usuario; // Email ou ID do assessor

    private String endpoint;

    private String metodo; // GET, POST, PUT, DELETE

    private String ip;

    private Boolean sucesso;

    private String mensagem;

    @CreatedDate
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

