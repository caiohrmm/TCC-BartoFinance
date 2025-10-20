package com.bartofinance.repository;

import com.bartofinance.model.Relatorio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Relatórios
 */
@Repository
public interface RelatorioRepository extends MongoRepository<Relatorio, String> {
    
    /**
     * Busca todos os relatórios de um investidor
     * @param investidorId ID do investidor
     * @return Lista de relatórios
     */
    List<Relatorio> findByInvestidorId(String investidorId);
    
    /**
     * Busca relatórios que tiveram insights gerados
     * @param insightsGerados Flag indicando se insights foram gerados
     * @return Lista de relatórios
     */
    List<Relatorio> findByInsightsGerados(Boolean insightsGerados);
}

