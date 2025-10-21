package com.bartofinance.security;

import com.bartofinance.service.LogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta todas as requisições e valida o token
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogService logService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain chain) throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        try {
            // Extrai o token do header Authorization
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                email = jwtUtil.extractEmail(jwt);
            }

            // Se temos email e não há autenticação no contexto ainda
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                // Valida o token
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    logger.debug("Token JWT válido para o usuário: {}", email);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage());
            
            // Registra tentativa de acesso com token inválido
            String ip = request.getRemoteAddr();
            logService.registrarAcaoSistema(
                request.getMethod(),
                request.getRequestURI(),
                "Falha na validação do token JWT: " + e.getMessage(),
                ip,
                false
            );
        }

        chain.doFilter(request, response);
    }
}

