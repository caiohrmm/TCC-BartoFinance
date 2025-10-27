package com.bartofinance.model.enums;

/**
 * Enum representando os tipos de produtos de investimento disponíveis
 */
public enum TipoProduto {
    CDB("Certificado de Depósito Bancário"),
    TESOURO_DIRETO("Tesouro Direto"),
    ACOES("Ações"),
    FUNDOS("Fundos de Investimento"),
    CRIPTOMOEDAS("Criptomoedas"),
    FII("Fundos Imobiliários"),
    LCI("Letra de Crédito Imobiliário"),
    LCA("Letra de Crédito do Agronegócio"),
    OUTROS("Outros");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

