package com.bartofinance.service;

import com.bartofinance.dto.request.PortfolioRequest;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.InvestmentPortfolio;
import com.bartofinance.model.enums.RiscoCarteira;
import com.bartofinance.model.enums.TipoCarteira;
import com.bartofinance.repository.InvestmentPortfolioRepository;
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
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PortfolioService
 */
@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private InvestmentPortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private PortfolioRequest portfolioRequest;
    private InvestmentPortfolio portfolio;
    private String assessorId;

    @BeforeEach
    void setUp() {
        assessorId = "assessor123";

        portfolioRequest = new PortfolioRequest(
                "Carteira Equilibrada",
                "Mix de renda fixa e variável",
                TipoCarteira.PERSONALIZADA,
                RiscoCarteira.MODERADO,
                new BigDecimal("12.5"),
                "inv123"
        );

        portfolio = InvestmentPortfolio.builder()
                .id("port123")
                .nome("Carteira Equilibrada")
                .descricao("Mix de renda fixa e variável")
                .tipo(TipoCarteira.PERSONALIZADA)
                .risco(RiscoCarteira.MODERADO)
                .metaRentabilidade(new BigDecimal("12.5"))
                .rentabilidadeAtual(BigDecimal.ZERO)
                .valorTotal(BigDecimal.ZERO)
                .investidorId("inv123")
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void deveCriarPortfolioPersonalizadoComSucesso() {
        // Arrange
        when(portfolioRepository.save(any(InvestmentPortfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponse response = portfolioService.criarPortfolio(portfolioRequest, assessorId);

        // Assert
        assertNotNull(response);
        assertEquals("Carteira Equilibrada", response.getNome());
        assertEquals(TipoCarteira.PERSONALIZADA, response.getTipo());
        assertEquals(RiscoCarteira.MODERADO, response.getRisco());
        verify(portfolioRepository, times(1)).save(any(InvestmentPortfolio.class));
    }

    @Test
    void deveLancarExcecaoQuandoCarteiraPersonalizadaSemInvestidor() {
        // Arrange
        portfolioRequest.setInvestidorId(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            portfolioService.criarPortfolio(portfolioRequest, assessorId);
        });
    }

    @Test
    void deveCriarCarteiraModeloSemInvestidor() {
        // Arrange
        portfolioRequest.setTipo(TipoCarteira.MODELO);
        portfolioRequest.setInvestidorId(null);
        
        InvestmentPortfolio modelo = InvestmentPortfolio.builder()
                .id("modelo123")
                .nome("Carteira Equilibrada")
                .tipo(TipoCarteira.MODELO)
                .risco(RiscoCarteira.MODERADO)
                .assessorId(assessorId)
                .build();
        
        when(portfolioRepository.save(any(InvestmentPortfolio.class))).thenReturn(modelo);

        // Act
        PortfolioResponse response = portfolioService.criarPortfolio(portfolioRequest, assessorId);

        // Assert
        assertNotNull(response);
        assertEquals(TipoCarteira.MODELO, response.getTipo());
        assertNull(response.getInvestidorId());
    }

    @Test
    void deveListarPortfoliosDoAssessor() {
        // Arrange
        List<InvestmentPortfolio> portfolios = Arrays.asList(portfolio);
        when(portfolioRepository.findByAssessorId(assessorId)).thenReturn(portfolios);

        // Act
        List<PortfolioResponse> response = portfolioService.listarPortfolios(assessorId);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(portfolioRepository, times(1)).findByAssessorId(assessorId);
    }

    @Test
    void deveListarApenasPortfoliosModelo() {
        // Arrange
        InvestmentPortfolio modelo = InvestmentPortfolio.builder()
                .id("modelo123")
                .tipo(TipoCarteira.MODELO)
                .build();
        
        when(portfolioRepository.findByTipoAndInvestidorIdIsNull(TipoCarteira.MODELO))
                .thenReturn(Arrays.asList(modelo));

        // Act
        List<PortfolioResponse> response = portfolioService.listarPortfoliosModelo();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void deveBuscarPortfolioPorId() {
        // Arrange
        when(portfolioRepository.findById("port123")).thenReturn(Optional.of(portfolio));

        // Act
        PortfolioResponse response = portfolioService.buscarPorId("port123", assessorId);

        // Assert
        assertNotNull(response);
        assertEquals("port123", response.getId());
    }

    @Test
    void deveLancarExcecaoQuandoPortfolioNaoEncontrado() {
        // Arrange
        when(portfolioRepository.findById("port999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            portfolioService.buscarPorId("port999", assessorId);
        });
    }

    @Test
    void deveAtualizarPortfolioComSucesso() {
        // Arrange
        when(portfolioRepository.findById("port123")).thenReturn(Optional.of(portfolio));
        when(portfolioRepository.save(any(InvestmentPortfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponse response = portfolioService.atualizarPortfolio("port123", portfolioRequest, assessorId);

        // Assert
        assertNotNull(response);
        verify(portfolioRepository, times(1)).save(any(InvestmentPortfolio.class));
    }

    @Test
    void deveDeletarPortfolioComSucesso() {
        // Arrange
        when(portfolioRepository.findById("port123")).thenReturn(Optional.of(portfolio));
        doNothing().when(portfolioRepository).delete(any(InvestmentPortfolio.class));

        // Act
        portfolioService.deletarPortfolio("port123", assessorId);

        // Assert
        verify(portfolioRepository, times(1)).delete(portfolio);
    }
}

