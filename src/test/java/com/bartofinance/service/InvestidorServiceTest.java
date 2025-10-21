package com.bartofinance.service;

import com.bartofinance.dto.request.InvestidorRequest;
import com.bartofinance.dto.response.InvestidorResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Investidor;
import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.repository.InvestidorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para InvestidorService
 */
@ExtendWith(MockitoExtension.class)
class InvestidorServiceTest {

    @Mock
    private InvestidorRepository investidorRepository;

    @InjectMocks
    private InvestidorService investidorService;

    private InvestidorRequest investidorRequest;
    private Investidor investidor;
    private String assessorId;

    @BeforeEach
    void setUp() {
        assessorId = "assessor123";

        investidorRequest = new InvestidorRequest(
                "João Silva",
                "12345678901",
                "joao@email.com",
                "11987654321",
                PerfilInvestidor.MODERADO,
                new BigDecimal("100000.00"),
                new BigDecimal("8000.00"),
                "Aposentadoria confortável"
        );

        investidor = Investidor.builder()
                .id("inv123")
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11987654321")
                .perfilInvestidor(PerfilInvestidor.MODERADO)
                .patrimonioAtual(new BigDecimal("100000.00"))
                .rendaMensal(new BigDecimal("8000.00"))
                .objetivos("Aposentadoria confortável")
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void deveCriarInvestidorComSucesso() {
        // Arrange
        when(investidorRepository.existsByCpf(anyString())).thenReturn(false);
        when(investidorRepository.save(any(Investidor.class))).thenReturn(investidor);

        // Act
        InvestidorResponse response = investidorService.criarInvestidor(investidorRequest, assessorId);

        // Assert
        assertNotNull(response);
        assertEquals("João Silva", response.getNome());
        assertEquals("12345678901", response.getCpf());
        assertEquals(PerfilInvestidor.MODERADO, response.getPerfilInvestidor());
        verify(investidorRepository, times(1)).existsByCpf("12345678901");
        verify(investidorRepository, times(1)).save(any(Investidor.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        // Arrange
        when(investidorRepository.existsByCpf(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            investidorService.criarInvestidor(investidorRequest, assessorId);
        });

        verify(investidorRepository, times(1)).existsByCpf("12345678901");
        verify(investidorRepository, never()).save(any(Investidor.class));
    }

    @Test
    void deveListarInvestidoresDoAssessor() {
        // Arrange
        List<Investidor> investidores = Arrays.asList(investidor);
        when(investidorRepository.findByAssessorId(assessorId)).thenReturn(investidores);

        // Act
        List<InvestidorResponse> response = investidorService.listarInvestidores(assessorId);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("João Silva", response.get(0).getNome());
        verify(investidorRepository, times(1)).findByAssessorId(assessorId);
    }

    @Test
    void deveBuscarInvestidorPorId() {
        // Arrange
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidor));

        // Act
        InvestidorResponse response = investidorService.buscarPorId("inv123", assessorId);

        // Assert
        assertNotNull(response);
        assertEquals("inv123", response.getId());
        assertEquals("João Silva", response.getNome());
        verify(investidorRepository, times(1)).findById("inv123");
    }

    @Test
    void deveLancarExcecaoQuandoInvestidorNaoEncontrado() {
        // Arrange
        when(investidorRepository.findById("inv999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            investidorService.buscarPorId("inv999", assessorId);
        });
    }

    @Test
    void deveLancarExcecaoQuandoInvestidorNaoPertenceAoAssessor() {
        // Arrange
        Investidor investidorOutroAssessor = Investidor.builder()
                .id("inv123")
                .assessorId("outroAssessor")
                .build();
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidorOutroAssessor));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            investidorService.buscarPorId("inv123", assessorId);
        });
    }

    @Test
    void deveAtualizarInvestidorComSucesso() {
        // Arrange
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidor));
        when(investidorRepository.save(any(Investidor.class))).thenReturn(investidor);

        // Act
        InvestidorResponse response = investidorService.atualizarInvestidor("inv123", investidorRequest, assessorId);

        // Assert
        assertNotNull(response);
        verify(investidorRepository, times(1)).save(any(Investidor.class));
    }

    @Test
    void deveDeletarInvestidorComSucesso() {
        // Arrange
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidor));
        doNothing().when(investidorRepository).delete(any(Investidor.class));

        // Act
        investidorService.deletarInvestidor("inv123", assessorId);

        // Assert
        verify(investidorRepository, times(1)).delete(investidor);
    }
}

