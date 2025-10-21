package com.bartofinance.controller;

import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.model.Log;
import com.bartofinance.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Logs", description = "Endpoints para consulta de logs de auditoria do sistema")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * Listar todos os logs com filtros opcionais
     */
    @GetMapping
    @Operation(summary = "Listar logs", description = "Lista logs do sistema com filtros opcionais (usuário, endpoint, método, sucesso)")
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
    @Operation(summary = "Listar logs por período", description = "Lista logs entre duas datas")
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
    @Operation(summary = "Listar erros", description = "Lista apenas logs de requisições com falha")
    public ResponseEntity<ApiResponse<List<Log>>> listarErros() {
        
        log.info("GET /logs/erros - Listando apenas erros");
        
        List<Log> logs = logService.buscarLogsComErro();
        
        return ResponseEntity.ok(ApiResponse.success("Logs de erro listados com sucesso", logs));
    }
}

