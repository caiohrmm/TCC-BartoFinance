package com.bartofinance.repository;

import com.bartofinance.model.Investidor;
import com.bartofinance.model.enums.PerfilInvestidor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência de Investidores
 */
@Repository
public interface InvestidorRepository extends MongoRepository<Investidor, String> {
    
    /**
     * Busca um investidor pelo CPF
     * @param cpf CPF do investidor
     * @return Optional contendo o investidor se encontrado
     */
    Optional<Investidor> findByCpf(String cpf);
    
    /**
     * Busca todos os investidores de um assessor específico
     * @param assessorId ID do assessor
     * @return Lista de investidores
     */
    List<Investidor> findByAssessorId(String assessorId);
    
    /**
     * Busca investidores por perfil de investidor
     * @param perfil Perfil do investidor
     * @return Lista de investidores
     */
    List<Investidor> findByPerfilInvestidor(PerfilInvestidor perfil);
    
    /**
     * Verifica se existe um investidor com o CPF fornecido
     * @param cpf CPF a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByCpf(String cpf);
}

