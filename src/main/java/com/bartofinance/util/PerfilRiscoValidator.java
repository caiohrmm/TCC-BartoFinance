package com.bartofinance.util;

import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.model.enums.RiscoCarteira;

/**
 * Utilitário para validação de compatibilidade entre perfil do investidor e risco da carteira
 */
public class PerfilRiscoValidator {

    /**
     * Valida se o perfil do investidor é compatível com o risco da carteira
     * 
     * @param perfilInvestidor Perfil do investidor
     * @param riscoCarteira Risco da carteira
     * @return true se compatível, false caso contrário
     */
    public static boolean isCompatible(PerfilInvestidor perfilInvestidor, RiscoCarteira riscoCarteira) {
        if (perfilInvestidor == null || riscoCarteira == null) {
            return false;
        }

        return switch (perfilInvestidor) {
            case CONSERVADOR -> riscoCarteira == RiscoCarteira.BAIXO;
            case MODERADO -> riscoCarteira == RiscoCarteira.BAIXO || riscoCarteira == RiscoCarteira.MODERADO;
            case AGRESSIVO -> true; // Agressivo aceita qualquer nível de risco
        };
    }

    /**
     * Retorna uma mensagem de erro específica para incompatibilidade
     */
    public static String getErrorMessage(PerfilInvestidor perfilInvestidor, RiscoCarteira riscoCarteira) {
        return switch (perfilInvestidor) {
            case CONSERVADOR -> "Investidor conservador só pode ter carteiras de risco BAIXO";
            case MODERADO -> "Investidor moderado pode ter carteiras de risco BAIXO ou MODERADO";
            case AGRESSIVO -> "Investidor agressivo pode ter carteiras de qualquer nível de risco";
        };
    }

    /**
     * Retorna os níveis de risco permitidos para o perfil do investidor
     */
    public static RiscoCarteira[] getRiscosPermitidos(PerfilInvestidor perfilInvestidor) {
        return switch (perfilInvestidor) {
            case CONSERVADOR -> new RiscoCarteira[]{RiscoCarteira.BAIXO};
            case MODERADO -> new RiscoCarteira[]{RiscoCarteira.BAIXO, RiscoCarteira.MODERADO};
            case AGRESSIVO -> RiscoCarteira.values();
        };
    }

    /**
     * Retorna uma descrição da compatibilidade para exibição
     */
    public static String getCompatibilityDescription(PerfilInvestidor perfilInvestidor) {
        return switch (perfilInvestidor) {
            case CONSERVADOR -> "Recomendado: Carteiras de risco BAIXO";
            case MODERADO -> "Recomendado: Carteiras de risco BAIXO ou MODERADO";
            case AGRESSIVO -> "Pode usar carteiras de qualquer nível de risco";
        };
    }
}
