package com.bartofinance.model.enums;

/**
 * Enum representando o nível de risco de uma carteira
 */
public enum RiscoCarteira {
    BAIXO("Risco Baixo - Investimentos conservadores"),
    MODERADO("Risco Moderado - Equilíbrio entre segurança e rentabilidade"),
    ALTO("Risco Alto - Busca maior rentabilidade com maior exposição");

    private final String descricao;

    RiscoCarteira(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

