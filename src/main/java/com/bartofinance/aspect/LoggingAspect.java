package com.bartofinance.aspect;

import com.bartofinance.model.Log;
import com.bartofinance.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * Aspecto AOP para logging automático de todas as requisições REST
 * 
 * Intercepta chamadas aos controllers e registra logs no MongoDB
 * para fins de auditoria e rastreamento.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Autowired
    private LogRepository logRepository;

    /**
     * Intercepta todas as chamadas aos controllers REST
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        String endpoint = request.getRequestURI();
        String metodo = request.getMethod();
        String usuario = getUsuarioAtual();
        String ip = getClientIp(request);

        log.debug("Interceptando requisição: {} {} - Usuário: {}", metodo, endpoint, usuario);

        try {
            // Executa o método
            Object result = joinPoint.proceed();

            // Log de sucesso
            Log logEntry = Log.builder()
                    .usuario(usuario)
                    .endpoint(endpoint)
                    .metodo(metodo)
                    .ip(ip)
                    .sucesso(true)
                    .mensagem("Requisição executada com sucesso")
                    .timestamp(LocalDateTime.now())
                    .build();

            logRepository.save(logEntry);
            log.debug("Log de sucesso registrado para: {} {}", metodo, endpoint);

            return result;

        } catch (Exception e) {
            // Log de erro
            Log logEntry = Log.builder()
                    .usuario(usuario)
                    .endpoint(endpoint)
                    .metodo(metodo)
                    .ip(ip)
                    .sucesso(false)
                    .mensagem("Erro: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            logRepository.save(logEntry);
            log.error("Log de erro registrado para: {} {} - Erro: {}", metodo, endpoint, e.getMessage());

            throw e;
        }
    }

    /**
     * Obtém o HttpServletRequest atual
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * Obtém o usuário autenticado atual
     */
    private String getUsuarioAtual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                return authentication.getName();
            }
        } catch (Exception e) {
            log.warn("Erro ao obter usuário autenticado: {}", e.getMessage());
        }
        return "Sistema";
    }

    /**
     * Obtém o IP do cliente
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

