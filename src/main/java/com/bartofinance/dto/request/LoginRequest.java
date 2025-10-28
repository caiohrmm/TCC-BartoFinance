package com.bartofinance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login de assessor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "LoginRequest",
    description = "Dados necessários para autenticação de assessor no sistema",
    example = """
        {
          "email": "joao.silva@bartofinance.com",
          "senha": "minhasenha123"
        }
        """
)
public class LoginRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(
        description = "Email do assessor cadastrado no sistema",
        example = "joao.silva@bartofinance.com",
        required = true,
        maxLength = 100
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(
        description = "Senha do assessor (será criptografada)",
        example = "minhasenha123",
        required = true,
        minLength = 6,
        maxLength = 50
    )
    private String senha;
}

