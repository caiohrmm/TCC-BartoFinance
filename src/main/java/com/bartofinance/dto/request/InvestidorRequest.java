package com.bartofinance.dto.request;

import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.validation.ValidCpf;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação/atualização de Investidor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestidorRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @ValidCpf(message = "CPF inválido")
    private String cpf;

    @Email(message = "Email deve ser válido")
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    @NotNull(message = "Perfil de investidor é obrigatório")
    private PerfilInvestidor perfilInvestidor;

    @DecimalMin(value = "0.0", message = "Patrimônio deve ser maior ou igual a zero")
    private BigDecimal patrimonioAtual;

    @DecimalMin(value = "0.0", message = "Renda mensal deve ser maior ou igual a zero")
    private BigDecimal rendaMensal;

    @Size(max = 500, message = "Objetivos deve ter no máximo 500 caracteres")
    private String objetivos;
}

