package com.bartofinance.service;

import com.bartofinance.dto.response.InvestidorRelatorioResponse;
import com.bartofinance.dto.response.InvestidorResponse;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.dto.response.AplicacaoResponse;
import com.bartofinance.model.Aplicacao;
import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import com.bartofinance.repository.AplicacaoRepository;
import com.bartofinance.repository.InvestmentPortfolioRepository;
import com.bartofinance.repository.InvestidorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para geração de relatórios e estatísticas
 */
@Service
@Slf4j
public class RelatorioService {

    @Autowired
    private InvestidorRepository investidorRepository;
    
    @Autowired
    private InvestmentPortfolioRepository portfolioRepository;
    
    @Autowired
    private AplicacaoRepository aplicacaoRepository;
    
    @Autowired
    private PortfolioService portfolioService;
    
    @Autowired
    private InvestidorService investidorService;

    /**
     * Gera relatório completo do investidor
     */
    public InvestidorRelatorioResponse gerarRelatorioInvestidor(String investidorId, String assessorId) {
        log.info("Gerando relatório completo para investidor: {}", investidorId);
        
        // Valida se o investidor existe e pertence ao assessor
        var investidor = investidorRepository.findById(investidorId)
                .orElseThrow(() -> new com.bartofinance.exception.ResourceNotFoundException("Investidor", "id", investidorId));
        
        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new com.bartofinance.exception.BadRequestException("Investidor não pertence a este assessor");
        }
        
        // Buscar carteiras do investidor
        List<com.bartofinance.model.InvestmentPortfolio> carteiras = 
            portfolioRepository.findAll()
                .stream()
                .filter(c -> c.getInvestidorId() != null && c.getInvestidorId().equals(investidorId))
                .collect(Collectors.toList());
        
        // Buscar aplicações de todas as carteiras
        List<Aplicacao> aplicacoes = aplicacaoRepository.findAll()
            .stream()
            .filter(app -> carteiras.stream()
                .anyMatch(carteira -> carteira.getId().equals(app.getPortfolioId())))
            .collect(Collectors.toList());
        
        // Calcular estatísticas
        BigDecimal valorTotal = aplicacoes.stream()
            .map(Aplicacao::getValorAplicado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Estatísticas por tipo de produto
        BigDecimal valorEmAcoes = calcularValorPorTipo(aplicacoes, TipoProduto.ACOES);
        BigDecimal valorEmFII = calcularValorPorTipo(aplicacoes, TipoProduto.FII);
        BigDecimal valorEmRendaFixa = calcularValorPorTipoRendaFixa(aplicacoes);
        BigDecimal valorEmFundos = calcularValorPorTipo(aplicacoes, TipoProduto.FUNDOS);
        BigDecimal valorEmCripto = calcularValorPorTipo(aplicacoes, TipoProduto.CRIPTOMOEDAS);
        BigDecimal valorEmOutros = calcularValorPorTipo(aplicacoes, TipoProduto.OUTROS);
        
        // Estatísticas por status
        int aplicacoesAtivas = (int) aplicacoes.stream()
            .filter(a -> a.getStatus() == StatusAplicacao.ATIVA)
            .count();
        
        int aplicacoesEncerradas = (int) aplicacoes.stream()
            .filter(a -> a.getStatus() == StatusAplicacao.ENCERRADA)
            .count();
        
        int aplicacoesResgatadas = (int) aplicacoes.stream()
            .filter(a -> a.getStatus() == StatusAplicacao.RESGATADA)
            .count();
        
        // Rentabilidade média ponderada
        BigDecimal rentabilidadeMedia = calcularRentabilidadePonderada(aplicacoes);
        
        // Nível de alerta e recomendação
        var alerta = determinarNivelAlerta(carteiras.size(), valorTotal, rentabilidadeMedia);
        
        return InvestidorRelatorioResponse.builder()
                .id(investidor.getId())
                .nome(investidor.getNome())
                .cpf(investidor.getCpf())
                .email(investidor.getEmail())
                .perfilInvestidor(investidor.getPerfilInvestidor())
                .patrimonioAtual(investidor.getPatrimonioAtual())
                .rendaMensal(investidor.getRendaMensal())
                .objetivos(investidor.getObjetivos())
                .totalCarteiras(carteiras.size())
                .totalAplicacoes(aplicacoes.size())
                .valorTotalInvestido(valorTotal)
                .rentabilidadeMedia(rentabilidadeMedia)
                .valorEmAcoes(valorEmAcoes)
                .valorEmFII(valorEmFII)
                .valorEmRendaFixa(valorEmRendaFixa)
                .valorEmFundos(valorEmFundos)
                .valorEmCripto(valorEmCripto)
                .valorEmOutros(valorEmOutros)
                .aplicacoesAtivas(aplicacoesAtivas)
                .aplicacoesEncerradas(aplicacoesEncerradas)
                .aplicacoesResgatadas(aplicacoesResgatadas)
                .nivelAlerta(alerta.getNivel())
                .recomendacaoPrincipal(alerta.getRecomendacao())
                .createdAt(investidor.getCreatedAt())
                .updatedAt(investidor.getUpdatedAt())
                .build();
    }
    
    /**
     * Calcula valor investido por tipo de produto
     */
    private BigDecimal calcularValorPorTipo(List<Aplicacao> aplicacoes, TipoProduto tipo) {
        return aplicacoes.stream()
            .filter(a -> a.getTipoProduto() == tipo)
            .map(Aplicacao::getValorAplicado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calcula valor em renda fixa (CDB, LCI, LCA, Tesouro)
     */
    private BigDecimal calcularValorPorTipoRendaFixa(List<Aplicacao> aplicacoes) {
        return aplicacoes.stream()
            .filter(a -> a.getTipoProduto() == TipoProduto.CDB 
                      || a.getTipoProduto() == TipoProduto.LCI
                      || a.getTipoProduto() == TipoProduto.LCA
                      || a.getTipoProduto() == TipoProduto.TESOURO_DIRETO)
            .map(Aplicacao::getValorAplicado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calcula rentabilidade média ponderada
     */
    private BigDecimal calcularRentabilidadePonderada(List<Aplicacao> aplicacoes) {
        List<Aplicacao> aplicacoesFinalizadas = aplicacoes.stream()
            .filter(a -> a.getStatus() == StatusAplicacao.ENCERRADA 
                      || a.getStatus() == StatusAplicacao.RESGATADA)
            .collect(Collectors.toList());
        
        if (aplicacoesFinalizadas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somaRentabilidadePonderada = BigDecimal.ZERO;
        BigDecimal somaValores = BigDecimal.ZERO;
        
        for (Aplicacao app : aplicacoesFinalizadas) {
            BigDecimal rentabilidade = app.getRentabilidadeAtual() != null 
                ? app.getRentabilidadeAtual() 
                : BigDecimal.ZERO;
            BigDecimal valor = app.getValorAplicado();
            
            somaRentabilidadePonderada = somaRentabilidadePonderada.add(
                rentabilidade.multiply(valor)
            );
            somaValores = somaValores.add(valor);
        }
        
        if (somaValores.compareTo(BigDecimal.ZERO) > 0) {
            return somaRentabilidadePonderada.divide(
                somaValores, 2, java.math.RoundingMode.HALF_UP
            );
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Determina nível de alerta e recomendação principal
     */
    private AlertaInfo determinarNivelAlerta(int totalCarteiras, BigDecimal valorTotal, BigDecimal rentabilidadeMedia) {
        if (totalCarteiras == 0) {
            return new AlertaInfo("ALTO", "⚠️ Investidor ainda não possui carteiras. Crie carteiras para começar a investir.");
        }
        
        if (valorTotal.compareTo(new BigDecimal("1000")) < 0) {
            return new AlertaInfo("MEDIO", "💡 Valor investido baixo. Considere aumentar os aportes gradualmente.");
        }
        
        if (rentabilidadeMedia.compareTo(new BigDecimal("5")) < 0) {
            return new AlertaInfo("MEDIO", "📊 Rentabilidade abaixo do esperado. Revise a estratégia de investimento.");
        }
        
        if (rentabilidadeMedia.compareTo(new BigDecimal("15")) < 0) {
            return new AlertaInfo("BAIXO", "✅ Rentabilidade dentro de parâmetros satisfatórios. Continue monitorando.");
        }
        
        return new AlertaInfo("BAIXO", "🎉 Excelente desempenho! Mantenha a estratégia.");
    }
    
    /**
     * Classe auxiliar para informações de alerta
     */
    private static class AlertaInfo {
        private final String nivel;
        private final String recomendacao;
        
        public AlertaInfo(String nivel, String recomendacao) {
            this.nivel = nivel;
            this.recomendacao = recomendacao;
        }
        
        public String getNivel() {
            return nivel;
        }
        
        public String getRecomendacao() {
            return recomendacao;
        }
    }
}

