package com.bartofinance.service;

import com.bartofinance.dto.request.PortfolioRequest;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Investidor;
import com.bartofinance.model.InvestmentPortfolio;
import com.bartofinance.model.enums.TipoCarteira;
import com.bartofinance.repository.AplicacaoRepository;
import com.bartofinance.repository.InvestidorRepository;
import com.bartofinance.repository.InvestmentPortfolioRepository;
import com.bartofinance.util.PerfilRiscoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Carteiras de Investimento
 */
@Service
@Slf4j
public class PortfolioService {

    @Autowired
    private InvestmentPortfolioRepository portfolioRepository;

    @Autowired
    private InvestidorRepository investidorRepository;
    
    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    /**
     * Cria uma nova carteira
     */
    public PortfolioResponse criarPortfolio(PortfolioRequest request, String assessorId) {
        log.info("Criando nova carteira: {} para assessor: {}", request.getNome(), assessorId);

        // Valida nome único por assessor
        if (portfolioRepository.existsByNomeAndAssessorId(request.getNome(), assessorId)) {
            throw new BadRequestException("Já existe uma carteira com este nome para este assessor");
        }

        // Valida se é personalizada e tem investidorId
        if (request.getTipo() == TipoCarteira.PERSONALIZADA && request.getInvestidorId() == null) {
            throw new BadRequestException("Carteira personalizada deve ter um investidorId");
        }
        
        // Valida se o investidor pertence ao assessor (quando fornecido)
        if (request.getInvestidorId() != null) {
            Investidor investidor = investidorRepository.findById(request.getInvestidorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", request.getInvestidorId()));
            
            if (!investidor.getAssessorId().equals(assessorId)) {
                throw new BadRequestException("Investidor não pertence a este assessor");
            }

            // Validação de compatibilidade perfil-risco
            if (!PerfilRiscoValidator.isCompatible(investidor.getPerfilInvestidor(), request.getRisco())) {
                throw new BadRequestException(PerfilRiscoValidator.getErrorMessage(investidor.getPerfilInvestidor(), request.getRisco()));
            }
        }

        InvestmentPortfolio portfolio = InvestmentPortfolio.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .tipo(request.getTipo())
                .risco(request.getRisco())
                .metaRentabilidade(request.getMetaRentabilidade() != null ? 
                    request.getMetaRentabilidade() : BigDecimal.ZERO)
                .rentabilidadeAtual(BigDecimal.ZERO)
                .valorTotal(BigDecimal.ZERO)
                .investidorId(request.getInvestidorId())
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();

        portfolio = portfolioRepository.save(portfolio);
        log.info("Carteira criada com sucesso: ID {}", portfolio.getId());

        return mapToResponse(portfolio);
    }

    /**
     * Lista todas as carteiras do assessor
     */
    public List<PortfolioResponse> listarPortfolios(String assessorId) {
        log.info("Listando carteiras do assessor: {}", assessorId);
        return portfolioRepository.findByAssessorId(assessorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas carteiras modelo (templates)
     */
    public List<PortfolioResponse> listarPortfoliosModelo() {
        log.info("Listando carteiras modelo");
        return portfolioRepository.findByTipoAndInvestidorIdIsNull(TipoCarteira.MODELO)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca portfolio por ID
     */
    public PortfolioResponse buscarPorId(String id, String assessorId) {
        log.info("Buscando portfolio: {}", id);
        InvestmentPortfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }

        return mapToResponse(portfolio);
    }

    /**
     * Atualiza um portfolio
     */
    public PortfolioResponse atualizarPortfolio(String id, PortfolioRequest request, String assessorId) {
        log.info("Atualizando portfolio: {}", id);
        InvestmentPortfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }

        // Valida nome único por assessor (se alterado)
        if (!portfolio.getNome().equals(request.getNome()) && 
            portfolioRepository.existsByNomeAndAssessorId(request.getNome(), assessorId)) {
            throw new BadRequestException("Já existe uma carteira com este nome para este assessor");
        }

        portfolio.setNome(request.getNome());
        portfolio.setDescricao(request.getDescricao());
        portfolio.setRisco(request.getRisco());
        portfolio.setMetaRentabilidade(request.getMetaRentabilidade());
        portfolio.setUpdatedAt(LocalDateTime.now());

        portfolio = portfolioRepository.save(portfolio);
        log.info("Portfolio atualizado com sucesso: ID {}", portfolio.getId());

        return mapToResponse(portfolio);
    }

    /**
     * Deleta um portfolio
     */
    public void deletarPortfolio(String id, String assessorId) {
        log.info("Deletando portfolio: {}", id);
        InvestmentPortfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }

        portfolioRepository.delete(portfolio);
        log.info("Portfolio deletado com sucesso: ID {}", id);
    }

    /**
     * Simula desempenho de um portfolio (sem salvar)
     */
    public PortfolioResponse simularPortfolio(PortfolioRequest request, String assessorId) {
        log.info("Simulando portfolio: {} do tipo {} com risco {}", request.getNome(), request.getTipo(), request.getRisco());

        // Cria portfolio temporário para simulação (não salva no banco)
        InvestmentPortfolio portfolioSimulado = InvestmentPortfolio.builder()
                .id("SIMULACAO-" + System.currentTimeMillis())
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .tipo(request.getTipo())
                .risco(request.getRisco())
                .metaRentabilidade(request.getMetaRentabilidade())
                .rentabilidadeAtual(request.getMetaRentabilidade().multiply(new java.math.BigDecimal("0.8"))) // Simulação: 80% da meta
                .valorTotal(java.math.BigDecimal.ZERO)
                .investidorId(request.getInvestidorId())
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();

        log.info("Simulação realizada: rentabilidade estimada de {}%", portfolioSimulado.getRentabilidadeAtual());
        return mapToResponse(portfolioSimulado);
    }

    /**
     * Atualiza estatísticas do portfolio baseado nas aplicações
     */
    public void atualizarEstatisticasPortfolio(String portfolioId) {
        log.info("Atualizando estatísticas do portfolio: {}", portfolioId);
        
        InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));
        
        // Buscar todas as aplicações deste portfolio
        List<com.bartofinance.model.Aplicacao> aplicacoes = 
            aplicacaoRepository.findAll().stream()
                .filter(app -> app.getPortfolioId().equals(portfolioId))
                .collect(java.util.stream.Collectors.toList());
        
        if (aplicacoes.isEmpty()) {
            portfolio.setValorTotal(BigDecimal.ZERO);
            portfolio.setRentabilidadeAtual(BigDecimal.ZERO);
        } else {
            // Valor total = soma dos valores aplicados (não considera rentabilidade de aplicações ativas)
            BigDecimal valorTotalAplicado = aplicacoes.stream()
                .map(com.bartofinance.model.Aplicacao::getValorAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Rentabilidade = média ponderada APENAS de aplicações ENCERRADAS/RESGATADAS
            List<com.bartofinance.model.Aplicacao> aplicacoesFinalizadas = aplicacoes.stream()
                .filter(app -> app.getStatus() == com.bartofinance.model.enums.StatusAplicacao.ENCERRADA || 
                              app.getStatus() == com.bartofinance.model.enums.StatusAplicacao.RESGATADA)
                .collect(java.util.stream.Collectors.toList());
            
            BigDecimal rentabilidadeMedia = BigDecimal.ZERO;
            if (!aplicacoesFinalizadas.isEmpty()) {
                BigDecimal somaRentabilidadePonderada = BigDecimal.ZERO;
                BigDecimal somaValores = BigDecimal.ZERO;
                
                for (com.bartofinance.model.Aplicacao app : aplicacoesFinalizadas) {
                    BigDecimal rentabilidade = app.getRentabilidadeAtual() != null ? 
                        app.getRentabilidadeAtual() : BigDecimal.ZERO;
                    BigDecimal valor = app.getValorAplicado();
                    
                    somaRentabilidadePonderada = somaRentabilidadePonderada.add(
                        rentabilidade.multiply(valor)
                    );
                    somaValores = somaValores.add(valor);
                }
                
                if (somaValores.compareTo(BigDecimal.ZERO) > 0) {
                    rentabilidadeMedia = somaRentabilidadePonderada.divide(
                        somaValores, 2, java.math.RoundingMode.HALF_UP
                    );
                }
            }
            
            portfolio.setValorTotal(valorTotalAplicado);
            portfolio.setRentabilidadeAtual(rentabilidadeMedia);
        }
        
        portfolio.setUpdatedAt(LocalDateTime.now());
        portfolioRepository.save(portfolio);
        
        log.info("Estatísticas atualizadas - Valor Total: {}, Rentabilidade: {}%", 
            portfolio.getValorTotal(), portfolio.getRentabilidadeAtual());
    }

    private PortfolioResponse mapToResponse(InvestmentPortfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .nome(portfolio.getNome())
                .descricao(portfolio.getDescricao())
                .tipo(portfolio.getTipo())
                .risco(portfolio.getRisco())
                .metaRentabilidade(portfolio.getMetaRentabilidade())
                .rentabilidadeAtual(portfolio.getRentabilidadeAtual())
                .valorTotal(portfolio.getValorTotal())
                .investidorId(portfolio.getInvestidorId())
                .assessorId(portfolio.getAssessorId())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .build();
    }
}

