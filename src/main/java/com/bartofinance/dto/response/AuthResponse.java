package com.bartofinance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de autenticação (login/registro)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "AuthResponse",
    description = "Resposta de autenticação contendo token JWT e dados do assessor",
    example = """
        {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
          "tipo": "Bearer",
          "assessorId": "64f8a1b2c3d4e5f6a7b8c9d0",
          "nome": "João Silva",
          "email": "joao.silva@bartofinance.com",
          "mensagem": "Login realizado com sucesso!"
        }
        """
)
public class AuthResponse {

    @Schema(
        description = "Token JWT para autenticação em endpoints protegidos",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    private String token;
    
    @Schema(
        description = "Tipo do token (sempre 'Bearer')",
        example = "Bearer",
        required = true,
        allowableValues = {"Bearer"}
    )
    private String tipo;
    
    @Schema(
        description = "ID único do assessor no sistema",
        example = "64f8a1b2c3d4e5f6a7b8c9d0",
        required = true
    )
    private String assessorId;
    
    @Schema(
        description = "Nome completo do assessor",
        example = "João Silva",
        required = true
    )
    private String nome;
    
    @Schema(
        description = "Email do assessor",
        example = "joao.silva@bartofinance.com",
        required = true,
        format = "email"
    )
    private String email;
    
    @Schema(
        description = "Mensagem de confirmação da operação",
        example = "Login realizado com sucesso!",
        required = true
    )
    private String mensagem;
}

