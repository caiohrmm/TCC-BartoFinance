package com.bartofinance.validation;

import com.bartofinance.util.CpfValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador customizado para CPF usando algoritmo matemático
 */
public class CpfConstraintValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public void initialize(ValidCpf constraintAnnotation) {
        // Não precisa de inicialização
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return true; // Deixa a validação @NotBlank cuidar disso
        }

        return CpfValidator.isValid(cpf);
    }
}
