package com.bartofinance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de registro de novo assessor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RegisterRequest",
    description = "Dados necessários para registro de novo assessor no sistema",
    example = """
        {
          "nome": "João Silva",
          "email": "joao.silva@bartofinance.com",
          "senha": "minhasenha123"
        }
        """
)
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Schema(
        description = "Nome completo do assessor",
        example = "João Silva",
        required = true,
        minLength = 3,
        maxLength = 100
    )
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(
        description = "Email único do assessor (será usado para login)",
        example = "joao.silva@bartofinance.com",
        required = true,
        maxLength = 100,
        format = "email"
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Schema(
        description = "Senha do assessor (será criptografada com BCrypt)",
        example = "minhasenha123",
        required = true,
        minLength = 6,
        maxLength = 50
    )
    private String senha;
}

