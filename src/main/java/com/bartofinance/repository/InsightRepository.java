package com.bartofinance.repository;

import com.bartofinance.model.Insight;
import com.bartofinance.model.enums.TipoInsight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Insights
 */
@Repository
public interface InsightRepository extends MongoRepository<Insight, String> {
    
    /**
     * Busca todos os insights de um investidor
     * @param investidorId ID do investidor
     * @return Lista de insights
     */
    List<Insight> findByInvestidorId(String investidorId);
    
    /**
     * Busca insights por tipo
     * @param tipoInsight Tipo do insight
     * @return Lista de insights
     */
    List<Insight> findByTipoInsight(TipoInsight tipoInsight);
    
    /**
     * Busca insights de um investidor por tipo
     * @param investidorId ID do investidor
     * @param tipoInsight Tipo do insight
     * @return Lista de insights
     */
    List<Insight> findByInvestidorIdAndTipoInsight(String investidorId, TipoInsight tipoInsight);
}

