package com.bartofinance.service;

import com.bartofinance.dto.response.InsightResponse;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Insight;
import com.bartofinance.model.Investidor;
import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.model.enums.TipoInsight;
import com.bartofinance.repository.InsightRepository;
import com.bartofinance.repository.InvestidorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para InsightService
 */
@ExtendWith(MockitoExtension.class)
class InsightServiceTest {

    @Mock
    private InsightRepository insightRepository;

    @Mock
    private InvestidorRepository investidorRepository;

    @InjectMocks
    private InsightService insightService;

    private Investidor investidorConservador;
    private Investidor investidorModerado;
    private Investidor investidorAgressivo;
    private Insight insight;

    @BeforeEach
    void setUp() {
        investidorConservador = Investidor.builder()
                .id("inv123")
                .nome("Maria Conservadora")
                .perfilInvestidor(PerfilInvestidor.CONSERVADOR)
                .build();

        investidorModerado = Investidor.builder()
                .id("inv456")
                .nome("João Moderado")
                .perfilInvestidor(PerfilInvestidor.MODERADO)
                .build();

        investidorAgressivo = Investidor.builder()
                .id("inv789")
                .nome("Pedro Agressivo")
                .perfilInvestidor(PerfilInvestidor.AGRESSIVO)
                .build();

        insight = Insight.builder()
                .id("insight123")
                .investidorId("inv123")
                .texto("Recomendação personalizada")
                .geradoPor("Gemini AI (Mock)")
                .tipo(TipoInsight.SUGESTAO)
                .dataGeracao(LocalDateTime.now())
                .build();
    }

    @Test
    void deveGerarInsightParaInvestidorConservador() {
        // Arrange
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidorConservador));
        when(insightRepository.save(any(Insight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InsightResponse response = insightService.gerarInsight("inv123");

        // Assert
        assertNotNull(response);
        assertEquals("inv123", response.getInvestidorId());
        assertEquals("Gemini AI (Mock)", response.getGeradoPor());
        assertNotNull(response.getTexto());
        assertTrue(response.getTexto().toLowerCase().contains("conservador") || 
                   response.getTexto().toLowerCase().contains("renda fixa") ||
                   response.getTexto().toLowerCase().contains("maria"));
        verify(insightRepository, times(1)).save(any(Insight.class));
    }

    @Test
    void deveGerarInsightParaInvestidorModerado() {
        // Arrange
        when(investidorRepository.findById("inv456")).thenReturn(Optional.of(investidorModerado));
        when(insightRepository.save(any(Insight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InsightResponse response = insightService.gerarInsight("inv456");

        // Assert
        assertNotNull(response);
        assertEquals("inv456", response.getInvestidorId());
        assertNotNull(response.getTexto());
        assertTrue(response.getTexto().toLowerCase().contains("moderado") || 
                   response.getTexto().toLowerCase().contains("equilibr") ||
                   response.getTexto().toLowerCase().contains("joão") ||
                   response.getTexto().toLowerCase().contains("50"));
    }

    @Test
    void deveGerarInsightParaInvestidorAgressivo() {
        // Arrange
        when(investidorRepository.findById("inv789")).thenReturn(Optional.of(investidorAgressivo));
        when(insightRepository.save(any(Insight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InsightResponse response = insightService.gerarInsight("inv789");

        // Assert
        assertNotNull(response);
        assertEquals("inv789", response.getInvestidorId());
        assertNotNull(response.getTexto());
        assertTrue(response.getTexto().toLowerCase().contains("agressivo") || 
                   response.getTexto().toLowerCase().contains("ações") ||
                   response.getTexto().toLowerCase().contains("pedro") ||
                   response.getTexto().toLowerCase().contains("70"));
    }

    @Test
    void deveLancarExcecaoQuandoInvestidorNaoEncontrado() {
        // Arrange
        when(investidorRepository.findById("inv999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            insightService.gerarInsight("inv999");
        });

        verify(insightRepository, never()).save(any(Insight.class));
    }

    @Test
    void deveListarInsightsDoInvestidor() {
        // Arrange
        List<Insight> insights = Arrays.asList(insight);
        when(insightRepository.findByInvestidorId("inv123")).thenReturn(insights);

        // Act
        List<InsightResponse> response = insightService.listarInsights("inv123");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("inv123", response.get(0).getInvestidorId());
        verify(insightRepository, times(1)).findByInvestidorId("inv123");
    }

    @Test
    void deveGerarInsightComTipoAleatorio() {
        // Arrange
        when(investidorRepository.findById("inv123")).thenReturn(Optional.of(investidorConservador));
        when(insightRepository.save(any(Insight.class))).thenReturn(insight);

        // Act
        InsightResponse response = insightService.gerarInsight("inv123");

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTipo());
        assertTrue(Arrays.asList(TipoInsight.values()).contains(response.getTipo()));
    }
}

