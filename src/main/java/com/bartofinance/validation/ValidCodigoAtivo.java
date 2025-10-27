package com.bartofinance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.bartofinance.model.enums.TipoProduto;
import java.lang.annotation.*;

/**
 * Anotação para validação de código de ativo por tipo de produto
 */
@Documented
@Constraint(validatedBy = CodigoAtivoConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCodigoAtivo {
    String message() default "Código de ativo inválido para o tipo de produto";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    TipoProduto tipoProduto();
}
