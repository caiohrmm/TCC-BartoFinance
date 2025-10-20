package com.bartofinance.controller;

import com.bartofinance.dto.request.LoginRequest;
import com.bartofinance.dto.request.RegisterRequest;
import com.bartofinance.dto.response.ApiResponse;
import com.bartofinance.dto.response.AuthResponse;
import com.bartofinance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de autenticação
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e registro de assessores")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registro de novo assessor
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar novo assessor", description = "Cria uma nova conta de assessor no sistema")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        
        logger.info("POST /auth/register - Registro de novo assessor: {}", request.getEmail());
        
        String ip = getClientIp(httpRequest);
        AuthResponse response = authService.register(request, ip);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Assessor registrado com sucesso!", response));
    }

    /**
     * Endpoint para login de assessor
     */
    @PostMapping("/login")
    @Operation(summary = "Login de assessor", description = "Autentica um assessor e retorna o token JWT")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        logger.info("POST /auth/login - Tentativa de login: {}", request.getEmail());
        
        String ip = getClientIp(httpRequest);
        AuthResponse response = authService.login(request, ip);
        
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso!", response));
    }

    /**
     * Endpoint de health check para verificar se a API está funcionando
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Verifica se a API está funcionando")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("BartoFinance API está funcionando!", "OK"));
    }

    /**
     * Extrai o IP do cliente da requisição
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

