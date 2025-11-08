package com.bartofinance.controller;

import com.bartofinance.dto.request.InvestidorRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.InvestidorResponse;
import com.bartofinance.service.InvestidorService;
import com.bartofinance.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciamento de Investidores
 */
@RestController
@RequestMapping("/investors")
@Tag(name = "👥 Investidores", description = "Gestão completa de investidores/clientes")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class InvestidorController {

    @Autowired
    private InvestidorService investidorService;

    @Autowired
    private AuthUtil authUtil;

    /**
     * Criar novo investidor
     */
    @PostMapping
    @Operation(
        summary = "📝 Criar novo investidor",
        description = """
            ## 📋 Descrição
            
            Cria um novo investidor (cliente) vinculado ao assessor autenticado.
            
            ## ✅ Validações
            
            - Nome: obrigatório, entre 3 e 100 caracteres
            - CPF: obrigatório, 11 dígitos, válido e único por assessor
            - Email: opcional, formato válido
            - Telefone: opcional, 10 ou 11 dígitos
            - Perfil de Investidor: obrigatório (CONSERVADOR, MODERADO, AGRESSIVO)
            - Patrimônio e Renda: opcionais, valores >= 0
            
            ## 🔐 Segurança
            
            O investidor é automaticamente vinculado ao assessor autenticado.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do novo investidor",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InvestidorRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo Completo",
                    value = """
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
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "✅ Investidor criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Sucesso",
                    value = """
                        {
                          "sucesso": true,
                          "mensagem": "Investidor criado com sucesso",
                          "data": {
                            "id": "64f8a1b2c3d4e5f6a7b8c9d0",
                            "nome": "Maria Santos",
                            "cpf": "12345678901",
                            "email": "maria.santos@email.com",
                            "telefone": "11999999999",
                            "perfilInvestidor": "MODERADO",
                            "patrimonioAtual": 50000.00,
                            "rendaMensal": 8000.00,
                            "objetivos": "Aposentadoria e reserva de emergência",
                            "assessorId": "64f8a1b2c3d4e5f6a7b8c9d1",
                            "createdAt": "2024-01-15T10:30:00",
                            "updatedAt": "2024-01-15T10:30:00"
                          },
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou CPF já cadastrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de Validação",
                    value = """
                        {
                          "sucesso": false,
                          "mensagem": "CPF já cadastrado para este assessor",
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado - Token JWT inválido ou ausente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "💥 Erro interno do servidor"
        )
    })
    public ResponseEntity<ApiResponse<InvestidorResponse>> criarInvestidor(
            @Valid @RequestBody InvestidorRequest request,
            Authentication authentication) {
        
        log.info("POST /investors - Criando novo investidor");
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.criarInvestidor(request, assessorId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Investidor criado com sucesso", response));
    }

    /**
     * Listar todos os investidores do assessor
     */
    @GetMapping
    @Operation(
        summary = "📋 Listar investidores",
        description = """
            ## 📋 Descrição
            
            Lista todos os investidores vinculados ao assessor autenticado.
            
            ## 🔍 Filtros Disponíveis
            
            - **perfilInvestidor** (opcional): Filtra por perfil de risco
              - Valores aceitos: `CONSERVADOR`, `MODERADO`, `AGRESSIVO`
              - Exemplo: `/investors?perfilInvestidor=MODERADO`
            
            ## 📊 Resposta
            
            Retorna lista de investidores com todos os dados cadastrados.
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "perfilInvestidor",
                description = "Filtro opcional por perfil de investidor",
                example = "MODERADO",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"CONSERVADOR", "MODERADO", "AGRESSIVO"}
                )
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Lista de investidores retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Sucesso",
                    value = """
                        {
                          "sucesso": true,
                          "mensagem": "Investidores listados com sucesso",
                          "data": [
                            {
                              "id": "64f8a1b2c3d4e5f6a7b8c9d0",
                              "nome": "Maria Santos",
                              "cpf": "12345678901",
                              "perfilInvestidor": "MODERADO",
                              "patrimonioAtual": 50000.00
                            }
                          ],
                          "timestamp": "2024-01-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado - Token JWT inválido ou ausente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "💥 Erro interno do servidor"
        )
    })
    public ResponseEntity<ApiResponse<List<InvestidorResponse>>> listarInvestidores(
            @RequestParam(required = false) String perfilInvestidor,
            Authentication authentication) {
        
        log.info("GET /investors - Listando investidores, perfil={}", perfilInvestidor);
        String assessorId = authUtil.getAssessorId(authentication);
        
        List<InvestidorResponse> response;
        
        if (perfilInvestidor != null && !perfilInvestidor.isBlank()) {
            response = investidorService.listarPorPerfil(assessorId, perfilInvestidor);
        } else {
            response = investidorService.listarInvestidores(assessorId);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Investidores listados com sucesso", response));
    }

    /**
     * Buscar investidor por ID
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "🔍 Buscar investidor por ID",
        description = """
            ## 📋 Descrição
            
            Busca um investidor específico pelo ID.
            
            ## 🔐 Segurança
            
            - Valida se o investidor pertence ao assessor autenticado
            - Retorna erro 404 se não encontrado
            - Retorna erro 400 se não pertence ao assessor
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "id",
                description = "ID único do investidor",
                required = true,
                example = "64f8a1b2c3d4e5f6a7b8c9d0",
                schema = @Schema(type = "string")
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Investidor encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Investidor não pertence a este assessor"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "❌ Investidor não encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado - Token JWT inválido ou ausente"
        )
    })
    public ResponseEntity<ApiResponse<InvestidorResponse>> buscarInvestidor(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("GET /investors/{} - Buscando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.buscarPorId(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor encontrado", response));
    }

    /**
     * Atualizar investidor
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "✏️ Atualizar investidor",
        description = """
            ## 📋 Descrição
            
            Atualiza os dados de um investidor existente.
            
            ## ✅ Validações
            
            - Mesmas validações da criação
            - CPF pode ser alterado, mas deve ser único por assessor
            - Valida se o investidor pertence ao assessor autenticado
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "id",
                description = "ID único do investidor",
                required = true,
                example = "64f8a1b2c3d4e5f6a7b8c9d0"
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Investidor atualizado com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Dados inválidos ou investidor não pertence a este assessor"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "❌ Investidor não encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<InvestidorResponse>> atualizarInvestidor(
            @PathVariable String id,
            @Valid @RequestBody InvestidorRequest request,
            Authentication authentication) {
        
        log.info("PUT /investors/{} - Atualizando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        InvestidorResponse response = investidorService.atualizarInvestidor(id, request, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor atualizado com sucesso", response));
    }

    /**
     * Deletar investidor
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "🗑️ Deletar investidor",
        description = """
            ## 📋 Descrição
            
            Remove um investidor do sistema.
            
            ## ⚠️ Atenção
            
            - Esta operação é irreversível
            - Considere o impacto nas carteiras e aplicações vinculadas
            - Valida se o investidor pertence ao assessor autenticado
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "id",
                description = "ID único do investidor",
                required = true,
                example = "64f8a1b2c3d4e5f6a7b8c9d0"
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Investidor deletado com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Investidor não pertence a este assessor"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "❌ Investidor não encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deletarInvestidor(
            @PathVariable String id,
            Authentication authentication) {
        
        log.info("DELETE /investors/{} - Deletando investidor", id);
        String assessorId = authUtil.getAssessorId(authentication);
        
        investidorService.deletarInvestidor(id, assessorId);
        
        return ResponseEntity.ok(ApiResponse.success("Investidor deletado com sucesso"));
    }
}

