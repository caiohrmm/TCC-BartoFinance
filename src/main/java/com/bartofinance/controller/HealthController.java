package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para health checks e status da aplicação
 */
@RestController
@RequestMapping("/health")
@Tag(name = "❤️ Health", description = "Endpoints para verificação de status da aplicação")
public class HealthController {

    @GetMapping
    @Operation(
        summary = "❤️ Health Check Principal",
        description = """
            ## 📋 Descrição
            
            Verifica o status geral da aplicação BartoFinance.
            
            ## 🎯 Uso
            
            - Monitoramento de saúde da aplicação
            - Verificação de conectividade
            - Teste básico de funcionamento
            
            ## ✅ Resposta
            
            Retorna status "UP" se a aplicação estiver funcionando normalmente.
            
            ## 🔓 Acesso Público
            
            Este endpoint não requer autenticação.
            """,
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "✅ Sistema operacional",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "Sucesso",
                        value = """
                            {
                              "sucesso": true,
                              "mensagem": "Sistema operacional",
                              "data": {
                                "status": "UP",
                                "application": "BartoFinance Backend",
                                "version": "1.0.0",
                                "timestamp": "2024-01-15T10:30:00"
                              },
                              "timestamp": "2024-01-15T10:30:00"
                            }
                            """
                    )
                )
            )
        }
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "BartoFinance Backend");
        status.put("version", "1.0.0");
        status.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(ApiResponse.success("Sistema operacional", status));
    }

    @GetMapping("/ping")
    @Operation(
        summary = "🏓 Ping",
        description = """
            ## 📋 Descrição
            
            Endpoint simples para verificar conectividade básica.
            
            ## 🎯 Uso
            
            - Teste rápido de conectividade
            - Verificação de latência
            - Health check mínimo
            
            ## ✅ Resposta
            
            Retorna "pong" se a aplicação estiver respondendo.
            
            ## 🔓 Acesso Público
            
            Este endpoint não requer autenticação.
            """,
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "✅ Aplicação respondendo",
                content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                        name = "Sucesso",
                        value = "pong"
                    )
                )
            )
        }
    )
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}

