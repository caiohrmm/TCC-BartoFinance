package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Health Check", description = "Endpoints para verificação de status da aplicação")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health Check Principal", description = "Verifica o status geral da aplicação")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "BartoFinance Backend");
        status.put("version", "1.0.0");
        status.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(ApiResponse.success("Sistema operacional", status));
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping", description = "Endpoint simples para verificar conectividade")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}

