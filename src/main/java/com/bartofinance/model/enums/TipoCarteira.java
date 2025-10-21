package com.bartofinance.model.enums;

/**
 * Enum representando o tipo de carteira de investimentos
 */
public enum TipoCarteira {
    MODELO("Carteira Modelo - Template para reutilização"),
    PERSONALIZADA("Carteira Personalizada - Específica para um investidor");

    private final String descricao;

    TipoCarteira(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

