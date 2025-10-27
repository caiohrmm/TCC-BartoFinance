package com.bartofinance.util;

import java.util.regex.Pattern;

/**
 * Utilitário para validação de CPF usando algoritmo matemático
 */
public class CpfValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private static final String[] CPFS_INVALIDOS = {
        "00000000000", "11111111111", "22222222222", "33333333333",
        "44444444444", "55555555555", "66666666666", "77777777777",
        "88888888888", "99999999999"
    };

    /**
     * Valida se um CPF é válido usando o algoritmo matemático oficial
     * 
     * @param cpf CPF a ser validado (apenas números)
     * @return true se o CPF for válido, false caso contrário
     */
    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Verifica se não é uma sequência de números iguais
        for (String cpfInvalido : CPFS_INVALIDOS) {
            if (cpf.equals(cpfInvalido)) {
                return false;
            }
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        // Verifica o primeiro dígito
        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        // Verifica o segundo dígito
        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    /**
     * Formata um CPF válido para exibição (XXX.XXX.XXX-XX)
     * 
     * @param cpf CPF apenas com números
     * @return CPF formatado ou null se inválido
     */
    public static String format(String cpf) {
        if (!isValid(cpf)) {
            return null;
        }
        
        cpf = cpf.replaceAll("\\D", "");
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9, 11);
    }

    /**
     * Remove formatação de um CPF (mantém apenas números)
     * 
     * @param cpf CPF com ou sem formatação
     * @return CPF apenas com números
     */
    public static String clean(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("\\D", "");
    }
}
