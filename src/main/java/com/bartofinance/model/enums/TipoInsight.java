package com.bartofinance.model.enums;

/**
 * Enum representando os tipos de insights gerados pela IA
 */
public enum TipoInsight {
    RISCO("Análise de Risco"),
    OPORTUNIDADE("Oportunidade de Investimento"),
    RESUMO("Resumo da Carteira"),
    SUGESTAO("Sugestão de Ajuste");

    private final String descricao;

    TipoInsight(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

