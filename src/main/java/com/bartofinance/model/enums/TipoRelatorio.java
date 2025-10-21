package com.bartofinance.model.enums;

/**
 * Enum representando o tipo de relatório
 */
public enum TipoRelatorio {
    INVESTIDOR("Relatório de Investidor"),
    CARTEIRA("Relatório de Carteira"),
    CONSOLIDADO("Relatório Consolidado");

    private final String descricao;

    TipoRelatorio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

