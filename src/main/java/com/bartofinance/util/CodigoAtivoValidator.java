package com.bartofinance.util;

import com.bartofinance.model.enums.TipoProduto;

/**
 * Utilitário para validação de códigos de ativos financeiros
 */
public class CodigoAtivoValidator {

    // Validação será feita inline por simplicidade

    /**
     * Valida se um código de ativo é válido para o tipo de produto especificado
     * 
     * @param codigoAtivo Código do ativo
     * @param tipoProduto Tipo do produto financeiro
     * @return true se o código for válido para o tipo, false caso contrário
     */
    public static boolean isValid(String codigoAtivo, TipoProduto tipoProduto) {
        if (codigoAtivo == null || codigoAtivo.trim().isEmpty()) {
            return false;
        }

        codigoAtivo = codigoAtivo.trim().toUpperCase();

        return switch (tipoProduto) {
            case ACOES -> isValidAcao(codigoAtivo);
            case CDB -> isValidCDB(codigoAtivo);
            case TESOURO_DIRETO -> isValidTesouro(codigoAtivo);
            case FUNDOS -> isValidFundo(codigoAtivo);
            case FII -> isValidFII(codigoAtivo);
            case LCI -> isValidLCI(codigoAtivo);
            case LCA -> isValidLCA(codigoAtivo);
            case CRIPTOMOEDAS -> true; // Não há validação específica
            case OUTROS -> true; // Não há validação específica
        };
    }

    /**
     * Valida código de ação brasileira (ex: PETR4, VALE3, ITUB4)
     */
    private static boolean isValidAcao(String codigo) {
        return codigo.matches("^[A-Z]{4}\\d{1,2}$");
    }

    /**
     * Valida código de CDB (ex: CDB001, CDB123456)
     */
    private static boolean isValidCDB(String codigo) {
        return codigo.matches("^CDB\\d{3,6}$");
    }

    /**
     * Valida código de Tesouro Direto (ex: TESOURO01, TS01, TESOURO1234)
     */
    private static boolean isValidTesouro(String codigo) {
        return codigo.matches("^(TESOURO|TS)\\d{2,4}$");
    }

    /**
     * Valida código de Fundo de Investimento (ex: BB01, ITAU1234, XP123)
     */
    private static boolean isValidFundo(String codigo) {
        return codigo.matches("^[A-Z]{2,6}\\d{2,4}$");
    }

    /**
     * Valida código de Fundo Imobiliário (ex: HGLG11, XPML11, VISC11)
     */
    private static boolean isValidFII(String codigo) {
        return codigo.matches("^[A-Z]{4}\\d{2}$");
    }

    /**
     * Valida código de LCI (Letra de Crédito Imobiliário)
     */
    private static boolean isValidLCI(String codigo) {
        return codigo.matches("^LCI\\d{3,6}$");
    }

    /**
     * Valida código de LCA (Letra de Crédito do Agronegócio)
     */
    private static boolean isValidLCA(String codigo) {
        return codigo.matches("^LCA\\d{3,6}$");
    }

    /**
     * Retorna uma mensagem de erro específica para o tipo de produto
     */
    public static String getErrorMessage(TipoProduto tipoProduto) {
        return switch (tipoProduto) {
            case ACOES -> "Código de ação inválido. Use o formato: PETR4, VALE3, ITUB4";
            case CDB -> "Código de CDB inválido. Use o formato: CDB001, CDB123456";
            case TESOURO_DIRETO -> "Código de Tesouro inválido. Use o formato: TESOURO01, TS01";
            case FUNDOS -> "Código de Fundo inválido. Use o formato: BB01, ITAU1234";
            case FII -> "Código de FII inválido. Use o formato: HGLG11, XPML11, VISC11";
            case LCI -> "Código de LCI inválido. Use o formato: LCI001, LCI123456";
            case LCA -> "Código de LCA inválido. Use o formato: LCA001, LCA123456";
            case CRIPTOMOEDAS -> "Código de criptomoeda inválido";
            case OUTROS -> "Código inválido para o tipo de produto";
        };
    }
}
