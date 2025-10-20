package com.bartofinance.repository;

import com.bartofinance.model.Assessor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações de persistência de Assessores
 */
@Repository
public interface AssessorRepository extends MongoRepository<Assessor, String> {
    
    /**
     * Busca um assessor pelo email
     * @param email Email do assessor
     * @return Optional contendo o assessor se encontrado
     */
    Optional<Assessor> findByEmail(String email);
    
    /**
     * Verifica se existe um assessor com o email fornecido
     * @param email Email a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
}

