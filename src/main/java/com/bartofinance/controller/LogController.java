package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.model.Log;
import com.bartofinance.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller para consulta de Logs de Auditoria
 */
@RestController
@RequestMapping("/logs")
@Tag(name = "🔍 Logs", description = "Endpoints para consulta de logs de auditoria do sistema")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * Listar todos os logs com filtros opcionais
     */
    @GetMapping
    @Operation(
        summary = "📋 Listar logs",
        description = """
            ## 📋 Descrição
            
            Lista logs do sistema de auditoria com filtros opcionais.
            
            ## 🔍 Filtros Disponíveis
            
            - **usuario**: Filtra por email do usuário
            - **endpoint**: Filtra por rota acessada
            - **metodo**: Filtra por método HTTP (GET, POST, PUT, DELETE)
            - **sucesso**: Filtra por sucesso/erro (true/false)
            
            ## 📊 Exemplos
            
            - `/logs` - Todos os logs
            - `/logs?usuario=joao@email.com` - Logs de um usuário
            - `/logs?endpoint=/investors` - Logs de um endpoint
            - `/logs?metodo=POST` - Logs de requisições POST
            - `/logs?sucesso=false` - Apenas erros
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "usuario",
                description = "Email do usuário para filtrar",
                example = "joao@email.com"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "endpoint",
                description = "Endpoint para filtrar",
                example = "/investors"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "metodo",
                description = "Método HTTP para filtrar",
                example = "POST",
                schema = @Schema(allowableValues = {"GET", "POST", "PUT", "DELETE", "PATCH"})
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "sucesso",
                description = "Filtrar por sucesso/erro",
                example = "true",
                schema = @Schema(type = "boolean")
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Logs listados com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<List<Log>>> listarLogs(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String endpoint,
            @RequestParam(required = false) String metodo,
            @RequestParam(required = false) Boolean sucesso) {
        
        log.info("GET /logs - usuario={}, endpoint={}, metodo={}, sucesso={}", usuario, endpoint, metodo, sucesso);
        
        List<Log> logs;
        
        if (usuario != null) {
            logs = logService.buscarLogsPorUsuario(usuario);
        } else if (endpoint != null) {
            logs = logService.buscarLogsPorEndpoint(endpoint);
        } else if (metodo != null) {
            logs = logService.buscarLogsPorMetodo(metodo);
        } else if (sucesso != null) {
            logs = sucesso ? logService.buscarTodosLogs()
                           .stream()
                           .filter(Log::getSucesso)
                           .toList()
                         : logService.buscarLogsComErro();
        } else {
            logs = logService.buscarTodosLogs();
        }
        
        return ResponseEntity.ok(ApiResponse.success("Logs listados com sucesso", logs));
    }

    /**
     * Listar logs por período
     */
    @GetMapping("/periodo")
    @Operation(
        summary = "📅 Listar logs por período",
        description = """
            ## 📋 Descrição
            
            Lista logs entre duas datas específicas.
            
            ## 📊 Formato de Data
            
            Use formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`
            
            Exemplo: `2024-01-15T10:30:00`
            """,
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "inicio",
                description = "Data/hora inicial (ISO 8601)",
                required = true,
                example = "2024-01-15T00:00:00"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "fim",
                description = "Data/hora final (ISO 8601)",
                required = true,
                example = "2024-01-15T23:59:59"
            )
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "✅ Logs do período listados com sucesso"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "❌ Formato de data inválido"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "🔒 Não autenticado"
        )
    })
    public ResponseEntity<ApiResponse<List<Log>>> listarLogsPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        log.info("GET /logs/periodo - inicio={}, fim={}", inicio, fim);
        
        List<Log> logs = logService.buscarLogsPorPeriodo(inicio, fim);
        
        return ResponseEntity.ok(ApiResponse.success("Logs do período listados com sucesso", logs));
    }

    /**
     * Listar apenas logs de erro
     */
    @GetMapping("/erros")
    @Operation(
        summary = "❌ Listar erros",
        description = """
            ## 📋 Descrição
            
            Lista apenas logs de requisições que falharam (sucesso = false).
            
            ## 🎯 Uso
            
            Útil para análise de erros e debugging do sistema.
            """,
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "✅ Logs de erro listados com sucesso"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "🔒 Não autenticado"
            )
        }
    )
    public ResponseEntity<ApiResponse<List<Log>>> listarErros() {
        
        log.info("GET /logs/erros - Listando apenas erros");
        
        List<Log> logs = logService.buscarLogsComErro();
        
        return ResponseEntity.ok(ApiResponse.success("Logs de erro listados com sucesso", logs));
    }
}

