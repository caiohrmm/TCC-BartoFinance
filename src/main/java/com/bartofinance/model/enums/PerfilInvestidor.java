package com.bartofinance.model.enums;

/**
 * Enum representando o perfil de risco de um investidor
 */
public enum PerfilInvestidor {
    CONSERVADOR("Conservador - Prioriza segurança e baixo risco"),
    MODERADO("Moderado - Busca equilíbrio entre segurança e rentabilidade"),
    AGRESSIVO("Agressivo - Aceita alto risco em busca de maior rentabilidade");

    private final String descricao;

    PerfilInvestidor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

