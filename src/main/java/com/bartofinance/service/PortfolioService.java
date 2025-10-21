package com.bartofinance.service;

import com.bartofinance.dto.request.PortfolioRequest;
import com.bartofinance.dto.response.PortfolioResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.InvestmentPortfolio;
import com.bartofinance.model.enums.TipoCarteira;
import com.bartofinance.repository.InvestmentPortfolioRepository;
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

    /**
     * Cria uma nova carteira
     */
    public PortfolioResponse criarPortfolio(PortfolioRequest request, String assessorId) {
        log.info("Criando nova carteira: {} para assessor: {}", request.getNome(), assessorId);

        // Valida se é personalizada e tem investidorId
        if (request.getTipo() == TipoCarteira.PERSONALIZADA && request.getInvestidorId() == null) {
            throw new BadRequestException("Carteira personalizada deve ter um investidorId");
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

