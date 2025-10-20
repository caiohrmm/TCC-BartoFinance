package com.bartofinance.security;

import com.bartofinance.model.Assessor;
import com.bartofinance.repository.AssessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Serviço customizado para carregar detalhes do usuário (Assessor)
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AssessorRepository assessorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Assessor assessor = assessorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Assessor não encontrado com email: " + email));

        if (!assessor.getAtivo()) {
            throw new UsernameNotFoundException("Assessor inativo");
        }

        return new User(assessor.getEmail(), assessor.getSenha(), new ArrayList<>());
    }
}

