package com.bartofinance.repository;

import com.bartofinance.model.Aplicacao;
import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Aplicações
 */
@Repository
public interface AplicacaoRepository extends MongoRepository<Aplicacao, String> {
    
    /**
     * Busca todas as aplicações de uma carteira
     * @param portfolioId ID da carteira
     * @return Lista de aplicações
     */
    List<Aplicacao> findByPortfolioId(String portfolioId);
    
    /**
     * Busca aplicações por status
     * @param status Status da aplicação
     * @return Lista de aplicações
     */
    List<Aplicacao> findByStatus(StatusAplicacao status);
    
    /**
     * Busca aplicações de uma carteira por status
     * @param portfolioId ID da carteira
     * @param status Status da aplicação
     * @return Lista de aplicações
     */
    List<Aplicacao> findByPortfolioIdAndStatus(String portfolioId, StatusAplicacao status);
    
    /**
     * Busca aplicações por tipo de produto
     * @param tipoProduto Tipo do produto
     * @return Lista de aplicações
     */
    List<Aplicacao> findByTipoProduto(TipoProduto tipoProduto);
    
    /**
     * Busca aplicações por código do ativo
     * @param codigoAtivo Código do ativo
     * @return Lista de aplicações
     */
    List<Aplicacao> findByCodigoAtivo(String codigoAtivo);
    
    /**
     * Busca aplicação por portfolio e código do ativo
     * @param portfolioId ID do portfolio
     * @param codigoAtivo Código do ativo
     * @return Optional contendo a aplicação se encontrada
     */
    java.util.Optional<Aplicacao> findByPortfolioIdAndCodigoAtivo(String portfolioId, String codigoAtivo);
    
    /**
     * Verifica se existe aplicação com código de ativo específico no portfolio
     * @param portfolioId ID do portfolio
     * @param codigoAtivo Código do ativo
     * @return true se existir, false caso contrário
     */
    boolean existsByPortfolioIdAndCodigoAtivo(String portfolioId, String codigoAtivo);
}

