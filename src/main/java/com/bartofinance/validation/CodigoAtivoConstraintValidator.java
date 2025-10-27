package com.bartofinance.validation;

import com.bartofinance.util.CodigoAtivoValidator;
import com.bartofinance.model.enums.TipoProduto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador customizado para código de ativo por tipo de produto
 */
public class CodigoAtivoConstraintValidator implements ConstraintValidator<ValidCodigoAtivo, String> {

    private TipoProduto tipoProduto;

    @Override
    public void initialize(ValidCodigoAtivo constraintAnnotation) {
        this.tipoProduto = constraintAnnotation.tipoProduto();
    }

    @Override
    public boolean isValid(String codigoAtivo, ConstraintValidatorContext context) {
        if (codigoAtivo == null || codigoAtivo.trim().isEmpty()) {
            return true; // Deixa a validação @NotBlank cuidar disso
        }

        return CodigoAtivoValidator.isValid(codigoAtivo, tipoProduto);
    }
}
