package com.bartofinance.repository;

import com.bartofinance.model.InvestmentPortfolio;
import com.bartofinance.model.enums.RiscoCarteira;
import com.bartofinance.model.enums.TipoCarteira;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Carteiras de Investimento
 */
@Repository
public interface InvestmentPortfolioRepository extends MongoRepository<InvestmentPortfolio, String> {
    
    /**
     * Busca todas as carteiras de um investidor
     * @param investidorId ID do investidor
     * @return Lista de carteiras
     */
    List<InvestmentPortfolio> findByInvestidorId(String investidorId);
    
    /**
     * Busca carteiras por tipo
     * @param tipo Tipo da carteira (MODELO ou PERSONALIZADA)
     * @return Lista de carteiras
     */
    List<InvestmentPortfolio> findByTipo(TipoCarteira tipo);
    
    /**
     * Busca carteiras por nível de risco
     * @param risco Nível de risco
     * @return Lista de carteiras
     */
    List<InvestmentPortfolio> findByRisco(RiscoCarteira risco);
    
    /**
     * Busca todas as carteiras modelo (templates)
     * @return Lista de carteiras modelo
     */
    List<InvestmentPortfolio> findByTipoAndInvestidorIdIsNull(TipoCarteira tipo);
    
    /**
     * Busca carteiras de um assessor específico
     * @param assessorId ID do assessor
     * @return Lista de carteiras
     */
    List<InvestmentPortfolio> findByAssessorId(String assessorId);
}

