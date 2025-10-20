package com.bartofinance.service;

import com.bartofinance.model.Log;
import com.bartofinance.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável por gerenciar logs de auditoria do sistema
 */
@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private LogRepository logRepository;

    /**
     * Registra uma ação do assessor no sistema
     */
    public void logAcao(String assessorId, String acao, String descricao, String endpoint, String ip, Boolean sucesso) {
        try {
            Log log = Log.builder()
                    .assessorId(assessorId)
                    .acao(acao)
                    .descricao(descricao)
                    .endpoint(endpoint)
                    .timestamp(LocalDateTime.now())
                    .ip(ip)
                    .sucesso(sucesso)
                    .build();

            logRepository.save(log);
            
            logger.info("Log registrado: {} - {} - Sucesso: {}", acao, descricao, sucesso);
        } catch (Exception e) {
            logger.error("Erro ao salvar log no MongoDB: {}", e.getMessage());
        }
    }

    /**
     * Registra uma ação de sistema (sem assessor específico)
     */
    public void logAcaoSistema(String acao, String descricao, String endpoint, String ip, Boolean sucesso) {
        logAcao(null, acao, descricao, endpoint, ip, sucesso);
    }

    /**
     * Busca todos os logs de um assessor
     */
    public List<Log> getLogsByAssessor(String assessorId) {
        return logRepository.findByAssessorId(assessorId);
    }

    /**
     * Busca logs por ação
     */
    public List<Log> getLogsByAcao(String acao) {
        return logRepository.findByAcao(acao);
    }

    /**
     * Busca logs em um período
     */
    public List<Log> getLogsByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return logRepository.findByTimestampBetween(inicio, fim);
    }

    /**
     * Busca logs com falha
     */
    public List<Log> getLogsFalhas() {
        return logRepository.findBySucesso(false);
    }

    /**
     * Busca todos os logs
     */
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}

