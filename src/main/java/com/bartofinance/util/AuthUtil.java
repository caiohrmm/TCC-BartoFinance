package com.bartofinance.util;

import com.bartofinance.exception.UnauthorizedException;
import com.bartofinance.model.Assessor;
import com.bartofinance.repository.AssessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Utilitário para extrair informações de autenticação
 */
@Component
public class AuthUtil {

    @Autowired
    private AssessorRepository assessorRepository;

    /**
     * Extrai o ID do assessor a partir do Authentication
     * 
     * @param authentication Objeto de autenticação do Spring Security
     * @return ID do assessor autenticado
     */
    public String getAssessorId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        String email = authentication.getName();
        Assessor assessor = assessorRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Assessor não encontrado"));

        return assessor.getId();
    }

    /**
     * Extrai o email do assessor a partir do Authentication
     */
    public String getEmail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        return authentication.getName();
    }

    /**
     * Busca o assessor completo a partir do Authentication
     */
    public Assessor getAssessor(Authentication authentication) {
        String email = getEmail(authentication);
        return assessorRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Assessor não encontrado"));
    }
}

