package com.bartofinance.util;

import com.bartofinance.exception.UnauthorizedException;
import com.bartofinance.model.Assessor;
import com.bartofinance.repository.AssessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthUtil
 */
@ExtendWith(MockitoExtension.class)
class AuthUtilTest {

    @Mock
    private AssessorRepository assessorRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthUtil authUtil;

    private Assessor assessor;

    @BeforeEach
    void setUp() {
        assessor = Assessor.builder()
                .id("assessor123")
                .email("teste@bartofinance.com")
                .nome("Assessor Teste")
                .build();
    }

    @Test
    void deveExtrairAssessorIdComSucesso() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("teste@bartofinance.com");
        when(assessorRepository.findByEmail("teste@bartofinance.com"))
                .thenReturn(Optional.of(assessor));

        // Act
        String assessorId = authUtil.getAssessorId(authentication);

        // Assert
        assertEquals("assessor123", assessorId);
        verify(assessorRepository, times(1)).findByEmail("teste@bartofinance.com");
    }

    @Test
    void deveLancarExcecaoQuandoNaoAutenticado() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            authUtil.getAssessorId(authentication);
        });
    }

    @Test
    void deveLancarExcecaoQuandoAssessorNaoEncontrado() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("naoexiste@bartofinance.com");
        when(assessorRepository.findByEmail("naoexiste@bartofinance.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            authUtil.getAssessorId(authentication);
        });
    }

    @Test
    void deveExtrairEmailComSucesso() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("teste@bartofinance.com");

        // Act
        String email = authUtil.getEmail(authentication);

        // Assert
        assertEquals("teste@bartofinance.com", email);
    }

    @Test
    void deveRetornarAssessorCompleto() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("teste@bartofinance.com");
        when(assessorRepository.findByEmail("teste@bartofinance.com"))
                .thenReturn(Optional.of(assessor));

        // Act
        Assessor result = authUtil.getAssessor(authentication);

        // Assert
        assertNotNull(result);
        assertEquals("assessor123", result.getId());
        assertEquals("Assessor Teste", result.getNome());
    }
}

