package com.bartofinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidade representando um Assessor de Investimentos
 * 
 * O assessor é o usuário principal do sistema, responsável por gerenciar
 * investidores, aplicações e relatórios.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assessores")
public class Assessor {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String email;

    private String senha; // Senha criptografada com BCrypt

    @CreatedDate
    private LocalDateTime dataCadastro;

    private LocalDateTime ultimoLogin;

    @Builder.Default
    private Boolean ativo = true;
}

