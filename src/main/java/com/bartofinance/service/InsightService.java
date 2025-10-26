package com.bartofinance.service;

import com.bartofinance.dto.response.InsightResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Insight;
import com.bartofinance.model.Investidor;
import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.model.enums.TipoInsight;
import com.bartofinance.repository.InsightRepository;
import com.bartofinance.repository.InvestidorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service para geração de Insights com IA (Mock Gemini)
 */
@Service
@Slf4j
public class InsightService {

    @Autowired
    private InsightRepository insightRepository;

    @Autowired
    private InvestidorRepository investidorRepository;

    private final Random random = new Random();

    /**
     * Gera um novo insight para um investidor (Mock Gemini AI)
     */
    public InsightResponse gerarInsight(String investidorId, String assessorId) {
        log.info("Gerando insight para investidor: {}", investidorId);

        Investidor investidor = investidorRepository.findById(investidorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", investidorId));
        
        // Valida se o investidor pertence ao assessor
        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Investidor não pertence a este assessor");
        }

        // Gera insight baseado no perfil
        String textoInsight = gerarInsightPorPerfil(investidor);
        TipoInsight tipoInsight = determinarTipoInsight();

        Insight insight = Insight.builder()
                .investidorId(investidorId)
                .texto(textoInsight)
                .geradoPor("Gemini AI (Mock)")
                .tipo(tipoInsight)
                .dataGeracao(LocalDateTime.now())
                .build();

        insight = insightRepository.save(insight);
        log.info("Insight gerado com sucesso: ID {}", insight.getId());

        return mapToResponse(insight);
    }

    /**
     * Lista todos os insights de um investidor
     */
    public List<InsightResponse> listarInsights(String investidorId, String assessorId) {
        log.info("Listando insights do investidor: {}", investidorId);
        
        // Valida se o investidor pertence ao assessor
        Investidor investidor = investidorRepository.findById(investidorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", investidorId));
        
        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Investidor não pertence a este assessor");
        }
        
        return insightRepository.findByInvestidorId(investidorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gera texto do insight baseado no perfil do investidor (Mock)
     */
    private String gerarInsightPorPerfil(Investidor investidor) {
        PerfilInvestidor perfil = investidor.getPerfilInvestidor();
        String nome = investidor.getNome();

        switch (perfil) {
            case CONSERVADOR:
                return gerarInsightConservador(nome);
            case MODERADO:
                return gerarInsightModerado(nome);
            case AGRESSIVO:
                return gerarInsightAgressivo(nome);
            default:
                return "Perfil não identificado. Recomenda-se análise detalhada antes de investir.";
        }
    }

    private String gerarInsightConservador(String nome) {
        String[] insights = {
            String.format("%s, com seu perfil conservador, recomenda-se manter 70%% em renda fixa " +
                "de baixo risco (Tesouro Direto, CDBs de bancos grandes) e 30%% em fundos " +
                "conservadores. Priorize a segurança e liquidez.", nome),
            
            String.format("Olá %s! Para proteger seu patrimônio, considere diversificar entre " +
                "Tesouro Selic (alta liquidez), CDBs com garantia do FGC e fundos DI. " +
                "Evite exposição acima de 10%% em renda variável.", nome),
            
            String.format("%s, mantenha uma reserva de emergência equivalente a 6 meses de despesas " +
                "em investimentos de alta liquidez. O restante pode ser alocado em títulos " +
                "de renda fixa com prazos mais longos para melhor rentabilidade.", nome)
        };
        
        return insights[random.nextInt(insights.length)];
    }

    private String gerarInsightModerado(String nome) {
        String[] insights = {
            String.format("%s, com perfil moderado, você pode equilibrar entre renda fixa (50%%) " +
                "e renda variável (50%%). Considere fundos multimercado, ações de empresas " +
                "consolidadas (blue chips) e alguns fundos imobiliários.", nome),
            
            String.format("Olá %s! Diversifique sua carteira: 40%% em renda fixa (Tesouro IPCA+, CDBs), " +
                "40%% em ações de setores diversificados, 10%% em FIIs e 10%% em reserva de emergência. " +
                "Reavalie a cada 6 meses.", nome),
            
            String.format("%s, aproveite oportunidades de mercado mantendo o equilíbrio. " +
                "Invista em ETFs para diversificação automática, combine com alguns CDBs " +
                "de médio prazo e mantenha uma pequena posição em criptomoedas (máx 5%%).", nome)
        };
        
        return insights[random.nextInt(insights.length)];
    }

    private String gerarInsightAgressivo(String nome) {
        String[] insights = {
            String.format("%s, seu perfil agressivo permite maior exposição ao risco. " +
                "Considere 70%% em ações (diversificadas por setor), 20%% em investimentos " +
                "alternativos (FIIs, criptomoedas, ações internacionais) e apenas 10%% em reserva.", nome),
            
            String.format("Olá %s! Aproveite oportunidades de mercado. Foque em growth stocks, " +
                "small caps com potencial, ETFs internacionais e considere day trade/swing trade " +
                "com 10-15%% do capital. Mantenha disciplina e stop loss.", nome),
            
            String.format("%s, diversifique globalmente: ações brasileiras (40%%), REITs e stocks " +
                "americanas (30%%), criptomoedas selecionadas (15%%), FIIs de alto rendimento (10%%) " +
                "e reserva estratégica (5%%). Risco alto, potencial de retorno elevado.", nome)
        };
        
        return insights[random.nextInt(insights.length)];
    }

    /**
     * Determina aleatoriamente o tipo de insight
     */
    private TipoInsight determinarTipoInsight() {
        TipoInsight[] tipos = TipoInsight.values();
        return tipos[random.nextInt(tipos.length)];
    }

    private InsightResponse mapToResponse(Insight insight) {
        return InsightResponse.builder()
                .id(insight.getId())
                .investidorId(insight.getInvestidorId())
                .texto(insight.getTexto())
                .geradoPor(insight.getGeradoPor())
                .tipo(insight.getTipo())
                .dataGeracao(insight.getDataGeracao())
                .build();
    }
}

