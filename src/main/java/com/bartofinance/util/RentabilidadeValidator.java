package com.bartofinance.util;

import com.bartofinance.model.enums.TipoProduto;
import java.math.BigDecimal;

/**
 * Utilitário para validação de rentabilidade por tipo de produto financeiro
 */
public class RentabilidadeValidator {

    // Limites de rentabilidade anual por tipo de produto (em percentual)
    private static final BigDecimal ACAO_MAX_RENTABILIDADE = new BigDecimal("200.0"); // 200% ao ano
    private static final BigDecimal FII_MAX_RENTABILIDADE = new BigDecimal("50.0");   // 50% ao ano
    private static final BigDecimal CDB_MAX_RENTABILIDADE = new BigDecimal("25.0");  // 25% ao ano
    private static final BigDecimal LCI_MAX_RENTABILIDADE = new BigDecimal("20.0");  // 20% ao ano
    private static final BigDecimal LCA_MAX_RENTABILIDADE = new BigDecimal("20.0");   // 20% ao ano
    private static final BigDecimal TESOURO_MAX_RENTABILIDADE = new BigDecimal("15.0"); // 15% ao ano
    private static final BigDecimal FUNDO_MAX_RENTABILIDADE = new BigDecimal("100.0"); // 100% ao ano

    // Rentabilidade mínima esperada (para validação de valores muito baixos)
    private static final BigDecimal RENTABILIDADE_MINIMA = new BigDecimal("-50.0"); // -50% ao ano

    /**
     * Valida se uma rentabilidade é válida para o tipo de produto especificado
     * 
     * @param rentabilidade Rentabilidade em percentual anual
     * @param tipoProduto Tipo do produto financeiro
     * @return true se a rentabilidade for válida, false caso contrário
     */
    public static boolean isValid(BigDecimal rentabilidade, TipoProduto tipoProduto) {
        if (rentabilidade == null) {
            return true; // Rentabilidade é opcional
        }

        // Verifica se está dentro dos limites mínimos e máximos
        if (rentabilidade.compareTo(RENTABILIDADE_MINIMA) < 0) {
            return false;
        }

        BigDecimal limiteMaximo = getLimiteMaximo(tipoProduto);
        return rentabilidade.compareTo(limiteMaximo) <= 0;
    }

    /**
     * Retorna o limite máximo de rentabilidade para o tipo de produto
     */
    private static BigDecimal getLimiteMaximo(TipoProduto tipoProduto) {
        return switch (tipoProduto) {
            case ACOES -> ACAO_MAX_RENTABILIDADE;
            case FUNDOS -> FUNDO_MAX_RENTABILIDADE;
            case CDB -> CDB_MAX_RENTABILIDADE;
            case TESOURO_DIRETO -> TESOURO_MAX_RENTABILIDADE;
            case CRIPTOMOEDAS -> FUNDO_MAX_RENTABILIDADE; // Para criptomoedas, usa limite de fundos
            case FII -> FII_MAX_RENTABILIDADE;
            case LCI -> LCI_MAX_RENTABILIDADE;
            case LCA -> LCA_MAX_RENTABILIDADE;
            case OUTROS -> FUNDO_MAX_RENTABILIDADE; // Para outros tipos, usa limite padrão
        };
    }

    /**
     * Retorna uma mensagem de erro específica para o tipo de produto
     */
    public static String getErrorMessage(TipoProduto tipoProduto) {
        BigDecimal limiteMaximo = getLimiteMaximo(tipoProduto);
        return String.format("Rentabilidade inválida para %s. Deve estar entre %.1f%% e %.1f%% ao ano",
                tipoProduto.name(), RENTABILIDADE_MINIMA, limiteMaximo);
    }

    /**
     * Retorna o limite máximo de rentabilidade para exibição
     */
    public static BigDecimal getLimiteMaximoRentabilidade(TipoProduto tipoProduto) {
        return getLimiteMaximo(tipoProduto);
    }

    /**
     * Retorna o limite mínimo de rentabilidade para exibição
     */
    public static BigDecimal getLimiteMinimoRentabilidade() {
        return RENTABILIDADE_MINIMA;
    }
}
