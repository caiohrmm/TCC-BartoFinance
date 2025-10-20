package com.bartofinance.model.enums;

/**
 * Enum representando o status de uma aplicação financeira
 */
public enum StatusAplicacao {
    ATIVA("Aplicação Ativa"),
    RESGATADA("Aplicação Resgatada"),
    ENCERRADA("Aplicação Encerrada");

    private final String descricao;

    StatusAplicacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

