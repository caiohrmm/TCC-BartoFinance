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
     * Busca todas as aplicações de um investidor
     * @param investidorId ID do investidor
     * @return Lista de aplicações
     */
    List<Aplicacao> findByInvestidorId(String investidorId);
    
    /**
     * Busca aplicações por status
     * @param status Status da aplicação
     * @return Lista de aplicações
     */
    List<Aplicacao> findByStatus(StatusAplicacao status);
    
    /**
     * Busca aplicações de um investidor por status
     * @param investidorId ID do investidor
     * @param status Status da aplicação
     * @return Lista de aplicações
     */
    List<Aplicacao> findByInvestidorIdAndStatus(String investidorId, StatusAplicacao status);
    
    /**
     * Busca aplicações por tipo de produto
     * @param tipoProduto Tipo do produto
     * @return Lista de aplicações
     */
    List<Aplicacao> findByTipoProduto(TipoProduto tipoProduto);
}

