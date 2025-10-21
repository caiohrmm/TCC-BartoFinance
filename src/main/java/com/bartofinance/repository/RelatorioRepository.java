package com.bartofinance.repository;

import com.bartofinance.model.Relatorio;
import com.bartofinance.model.enums.TipoRelatorio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Relatórios
 */
@Repository
public interface RelatorioRepository extends MongoRepository<Relatorio, String> {
    
    /**
     * Busca relatórios por referência (ID do investidor ou carteira)
     * @param referenciaId ID da referência
     * @return Lista de relatórios
     */
    List<Relatorio> findByReferenciaId(String referenciaId);
    
    /**
     * Busca relatórios por tipo
     * @param tipo Tipo do relatório
     * @return Lista de relatórios
     */
    List<Relatorio> findByTipo(TipoRelatorio tipo);
    
    /**
     * Busca relatórios criados por um assessor
     * @param criadoPor ID do assessor
     * @return Lista de relatórios
     */
    List<Relatorio> findByCriadoPor(String criadoPor);
    
    /**
     * Busca relatórios por tipo e referência
     * @param tipo Tipo do relatório
     * @param referenciaId ID da referência
     * @return Lista de relatórios
     */
    List<Relatorio> findByTipoAndReferenciaId(TipoRelatorio tipo, String referenciaId);
}

