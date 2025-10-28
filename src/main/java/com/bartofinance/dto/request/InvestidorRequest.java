package com.bartofinance.dto.request;

import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.validation.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
    name = "InvestidorRequest",
    description = "Dados necessários para criação ou atualização de um investidor",
    example = """
        {
          "nome": "Maria Santos",
          "cpf": "12345678901",
          "email": "maria.santos@email.com",
          "telefone": "11999999999",
          "perfilInvestidor": "MODERADO",
          "patrimonioAtual": 50000.00,
          "rendaMensal": 8000.00,
          "objetivos": "Aposentadoria e reserva de emergência"
        }
        """
)
public class InvestidorRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Schema(
        description = "Nome completo do investidor",
        example = "Maria Santos",
        required = true,
        minLength = 3,
        maxLength = 100
    )
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @ValidCpf(message = "CPF inválido")
    @Schema(
        description = "CPF do investidor (apenas números, 11 dígitos)",
        example = "12345678901",
        required = true,
        pattern = "^\\d{11}$",
        minLength = 11,
        maxLength = 11
    )
    private String cpf;

    @Email(message = "Email deve ser válido")
    @Schema(
        description = "Email de contato do investidor",
        example = "maria.santos@email.com",
        required = false,
        maxLength = 100,
        format = "email"
    )
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    @Schema(
        description = "Telefone de contato (apenas números, 10 ou 11 dígitos)",
        example = "11999999999",
        required = false,
        pattern = "^\\d{10,11}$",
        minLength = 10,
        maxLength = 11
    )
    private String telefone;

    @NotNull(message = "Perfil de investidor é obrigatório")
    @Schema(
        description = "Perfil de risco do investidor",
        example = "MODERADO",
        required = true,
        allowableValues = {"CONSERVADOR", "MODERADO", "AGRESSIVO"}
    )
    private PerfilInvestidor perfilInvestidor;

    @DecimalMin(value = "0.0", message = "Patrimônio deve ser maior ou igual a zero")
    @Schema(
        description = "Patrimônio atual do investidor em reais",
        example = "50000.00",
        required = false,
        minimum = "0.0",
        type = "number",
        format = "decimal"
    )
    private BigDecimal patrimonioAtual;

    @DecimalMin(value = "0.0", message = "Renda mensal deve ser maior ou igual a zero")
    @Schema(
        description = "Renda mensal do investidor em reais",
        example = "8000.00",
        required = false,
        minimum = "0.0",
        type = "number",
        format = "decimal"
    )
    private BigDecimal rendaMensal;

    @Size(max = 500, message = "Objetivos deve ter no máximo 500 caracteres")
    @Schema(
        description = "Objetivos financeiros e de investimento do cliente",
        example = "Aposentadoria e reserva de emergência",
        required = false,
        maxLength = 500
    )
    private String objetivos;
}

