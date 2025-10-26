package com.bartofinance.service;

import com.bartofinance.dto.request.AplicacaoRequest;
import com.bartofinance.dto.response.AplicacaoResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Aplicacao;
import com.bartofinance.model.InvestmentPortfolio;
import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.repository.AplicacaoRepository;
import com.bartofinance.repository.InvestmentPortfolioRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Aplicações Financeiras
 */
@Service
@Slf4j
public class AplicacaoService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Autowired
    private InvestmentPortfolioRepository portfolioRepository;
    
    @Autowired
    private PortfolioService portfolioService;

    /**
     * Cria uma nova aplicação
     */
    public AplicacaoResponse criarAplicacao(AplicacaoRequest request, String assessorId) {
        log.info("Criando nova aplicação no portfolio: {}", request.getPortfolioId());

        // Valida se o portfolio existe e pertence ao assessor
        InvestmentPortfolio portfolio = portfolioRepository.findById(request.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", request.getPortfolioId()));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }
        
        // Valida se já existe aplicação com o mesmo código de ativo no portfolio
        if (aplicacaoRepository.existsByPortfolioIdAndCodigoAtivo(request.getPortfolioId(), request.getCodigoAtivo())) {
            throw new BadRequestException("Já existe uma aplicação com o código " + request.getCodigoAtivo() + " neste portfolio");
        }

        Aplicacao aplicacao = Aplicacao.builder()
                .portfolioId(request.getPortfolioId())
                .tipoProduto(request.getTipoProduto())
                .codigoAtivo(request.getCodigoAtivo())
                .valorAplicado(request.getValorAplicado())
                .quantidade(request.getQuantidade())
                .dataCompra(request.getDataCompra())
                .dataVenda(request.getDataVenda())
                .rentabilidadeAtual(request.getRentabilidadeAtual() != null ? request.getRentabilidadeAtual() : BigDecimal.ZERO)
                .status(request.getStatus() != null ? request.getStatus() : StatusAplicacao.ATIVA)
                .notas(request.getNotas())
                .createdAt(LocalDateTime.now())
                .build();

        aplicacao = aplicacaoRepository.save(aplicacao);
        log.info("Aplicação criada com sucesso: ID {}", aplicacao.getId());
        
        // Atualizar estatísticas do portfolio
        portfolioService.atualizarEstatisticasPortfolio(aplicacao.getPortfolioId());

        return mapToResponse(aplicacao);
    }

    /**
     * Lista todas as aplicações do assessor
     */
    public List<AplicacaoResponse> listarTodas(String assessorId) {
        log.info("Listando todas aplicações do assessor: {}", assessorId);
        return aplicacaoRepository.findAll()
                .stream()
                .filter(app -> {
                    InvestmentPortfolio portfolio = portfolioRepository.findById(app.getPortfolioId()).orElse(null);
                    return portfolio != null && portfolio.getAssessorId().equals(assessorId);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista aplicações por portfolio
     */
    public List<AplicacaoResponse> listarPorPortfolio(String portfolioId, String assessorId) {
        log.info("Listando aplicações do portfolio: {}", portfolioId);
        
        // Valida se o portfolio pertence ao assessor
        InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }

        return aplicacaoRepository.findByPortfolioId(portfolioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista aplicações por status
     */
    public List<AplicacaoResponse> listarPorStatus(String status, String assessorId) {
        log.info("Listando aplicações com status: {}", status);
        return aplicacaoRepository.findByStatus(StatusAplicacao.valueOf(status.toUpperCase()))
                .stream()
                .filter(app -> {
                    InvestmentPortfolio portfolio = portfolioRepository.findById(app.getPortfolioId()).orElse(null);
                    return portfolio != null && portfolio.getAssessorId().equals(assessorId);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista aplicações por portfolio e status
     */
    public List<AplicacaoResponse> listarPorPortfolioEStatus(String portfolioId, String status, String assessorId) {
        log.info("Listando aplicações do portfolio: {} com status: {}", portfolioId, status);
        
        // Valida se o portfolio pertence ao assessor
        InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Portfolio não pertence a este assessor");
        }

        return aplicacaoRepository.findByPortfolioIdAndStatus(portfolioId, StatusAplicacao.valueOf(status.toUpperCase()))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca aplicação por ID
     */
    public AplicacaoResponse buscarPorId(String id, String assessorId) {
        log.info("Buscando aplicação: {}", id);
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação", "id", id));

        // Valida se o portfolio pertence ao assessor
        InvestmentPortfolio portfolio = portfolioRepository.findById(aplicacao.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", aplicacao.getPortfolioId()));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Aplicação não pertence a este assessor");
        }

        return mapToResponse(aplicacao);
    }

    /**
     * Atualiza uma aplicação
     */
    public AplicacaoResponse atualizarAplicacao(String id, AplicacaoRequest request, String assessorId) {
        log.info("Atualizando aplicação: {}", id);
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação", "id", id));

        // Valida se o portfolio pertence ao assessor
        final String portfolioId = aplicacao.getPortfolioId();
        InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Aplicação não pertence a este assessor");
        }

        // Valida se o código do ativo (se alterado) não conflita com outra aplicação do mesmo portfolio
        if (!aplicacao.getCodigoAtivo().equals(request.getCodigoAtivo())) {
            java.util.Optional<Aplicacao> aplicacaoExistente = aplicacaoRepository.findByPortfolioIdAndCodigoAtivo(
                portfolioId, request.getCodigoAtivo()
            );
            if (aplicacaoExistente.isPresent() && !aplicacaoExistente.get().getId().equals(id)) {
                throw new BadRequestException("Já existe uma aplicação com o código " + request.getCodigoAtivo() + " neste portfolio");
            }
        }

        aplicacao.setTipoProduto(request.getTipoProduto());
        aplicacao.setCodigoAtivo(request.getCodigoAtivo());
        aplicacao.setValorAplicado(request.getValorAplicado());
        aplicacao.setQuantidade(request.getQuantidade());
        aplicacao.setDataCompra(request.getDataCompra());
        aplicacao.setDataVenda(request.getDataVenda());
        aplicacao.setRentabilidadeAtual(request.getRentabilidadeAtual() != null ? request.getRentabilidadeAtual() : BigDecimal.ZERO);
        aplicacao.setStatus(request.getStatus() != null ? request.getStatus() : StatusAplicacao.ATIVA);
        aplicacao.setNotas(request.getNotas());
        aplicacao.setUpdatedAt(LocalDateTime.now());

        aplicacao = aplicacaoRepository.save(aplicacao);
        log.info("Aplicação atualizada com sucesso: ID {}", aplicacao.getId());
        
        // Atualizar estatísticas do portfolio
        portfolioService.atualizarEstatisticasPortfolio(portfolioId);

        return mapToResponse(aplicacao);
    }

    /**
     * Deleta uma aplicação
     */
    public void deletarAplicacao(String id, String assessorId) {
        log.info("Deletando aplicação: {}", id);
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação", "id", id));

        // Valida se o portfolio pertence ao assessor
        InvestmentPortfolio portfolio = portfolioRepository.findById(aplicacao.getPortfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", aplicacao.getPortfolioId()));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Aplicação não pertence a este assessor");
        }

        String portfolioIdParaAtualizar = aplicacao.getPortfolioId();
        
        aplicacaoRepository.delete(aplicacao);
        log.info("Aplicação deletada com sucesso: ID {}", id);
        
        // Atualizar estatísticas do portfolio
        portfolioService.atualizarEstatisticasPortfolio(portfolioIdParaAtualizar);
    }

    /**
     * Encerra uma aplicação (registra venda)
     */
    public AplicacaoResponse encerrarAplicacao(String id, String dataVenda, Double rentabilidadeFinal, String assessorId) {
        log.info("Encerrando aplicação: {}", id);
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação", "id", id));

        // Valida se o portfolio pertence ao assessor
        final String portfolioId = aplicacao.getPortfolioId();
        InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        if (!portfolio.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Aplicação não pertence a este assessor");
        }

        // Atualiza a aplicação
        aplicacao.setDataVenda(LocalDateTime.parse(dataVenda));
        aplicacao.setRentabilidadeAtual(BigDecimal.valueOf(rentabilidadeFinal));
        aplicacao.setStatus(StatusAplicacao.ENCERRADA);

        aplicacao = aplicacaoRepository.save(aplicacao);
        log.info("Aplicação encerrada com sucesso: ID {}", id);
        
        // Atualizar estatísticas do portfolio
        portfolioService.atualizarEstatisticasPortfolio(portfolioId);

        return mapToResponse(aplicacao);
    }

    private AplicacaoResponse mapToResponse(Aplicacao aplicacao) {
        return AplicacaoResponse.builder()
                .id(aplicacao.getId())
                .portfolioId(aplicacao.getPortfolioId())
                .tipoProduto(aplicacao.getTipoProduto())
                .codigoAtivo(aplicacao.getCodigoAtivo())
                .valorAplicado(aplicacao.getValorAplicado())
                .quantidade(aplicacao.getQuantidade())
                .dataCompra(aplicacao.getDataCompra())
                .dataVenda(aplicacao.getDataVenda())
                .rentabilidadeAtual(aplicacao.getRentabilidadeAtual())
                .status(aplicacao.getStatus())
                .notas(aplicacao.getNotas())
                .createdAt(aplicacao.getCreatedAt())
                .updatedAt(aplicacao.getUpdatedAt())
                .build();
    }
}

