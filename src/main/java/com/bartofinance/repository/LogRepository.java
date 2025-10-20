package com.bartofinance.repository;

import com.bartofinance.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de persistência de Logs
 */
@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    
    /**
     * Busca todos os logs de um assessor
     * @param assessorId ID do assessor
     * @return Lista de logs
     */
    List<Log> findByAssessorId(String assessorId);
    
    /**
     * Busca logs por sucesso/falha
     * @param sucesso Flag indicando sucesso ou falha
     * @return Lista de logs
     */
    List<Log> findBySucesso(Boolean sucesso);
    
    /**
     * Busca logs por ação
     * @param acao Ação realizada
     * @return Lista de logs
     */
    List<Log> findByAcao(String acao);
    
    /**
     * Busca logs em um período de tempo
     * @param inicio Data/hora de início
     * @param fim Data/hora de fim
     * @return Lista de logs
     */
    List<Log> findByTimestampBetween(LocalDateTime inicio, LocalDateTime fim);
}

