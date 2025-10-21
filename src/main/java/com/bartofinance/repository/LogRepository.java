package com.bartofinance.repository;

import com.bartofinance.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de persistência de Logs via AOP
 */
@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    
    /**
     * Busca todos os logs de um usuário
     * @param usuario Email ou ID do usuário
     * @return Lista de logs
     */
    List<Log> findByUsuario(String usuario);
    
    /**
     * Busca logs por sucesso/falha
     * @param sucesso Flag indicando sucesso ou falha
     * @return Lista de logs
     */
    List<Log> findBySucesso(Boolean sucesso);
    
    /**
     * Busca logs por endpoint
     * @param endpoint Endpoint acessado
     * @return Lista de logs
     */
    List<Log> findByEndpoint(String endpoint);
    
    /**
     * Busca logs por método HTTP
     * @param metodo Método HTTP (GET, POST, PUT, DELETE)
     * @return Lista de logs
     */
    List<Log> findByMetodo(String metodo);
    
    /**
     * Busca logs em um período de tempo
     * @param inicio Data/hora de início
     * @param fim Data/hora de fim
     * @return Lista de logs
     */
    List<Log> findByTimestampBetween(LocalDateTime inicio, LocalDateTime fim);
}

