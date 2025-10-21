package com.bartofinance.service;

import com.bartofinance.model.Log;
import com.bartofinance.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para gerenciamento de Logs do sistema
 * 
 * Nota: A maioria dos logs agora é gerada automaticamente pelo LoggingAspect (AOP).
 * Este service é mantido para logs customizados quando necessário.
 */
@Service
@Slf4j
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * Registra um log de ação no sistema
     */
    public void registrarLog(String usuario, String metodo, String endpoint, String mensagem, String ip, boolean sucesso) {
        try {
            Log logEntry = Log.builder()
                    .usuario(usuario)
                    .metodo(metodo)
                    .endpoint(endpoint)
                    .mensagem(mensagem)
                    .ip(ip)
                    .sucesso(sucesso)
                    .timestamp(LocalDateTime.now())
                    .build();

            logRepository.save(logEntry);
            log.debug("Log registrado: {} {} - {}", metodo, endpoint, mensagem);
        } catch (Exception e) {
            log.error("Erro ao salvar log no MongoDB: {}", e.getMessage());
        }
    }

    /**
     * Registra um log de sucesso
     */
    public void registrarSucesso(String usuario, String metodo, String endpoint, String ip) {
        registrarLog(usuario, metodo, endpoint, "Operação realizada com sucesso", ip, true);
    }

    /**
     * Registra um log de erro
     */
    public void registrarErro(String usuario, String metodo, String endpoint, String mensagem, String ip) {
        registrarLog(usuario, metodo, endpoint, "Erro: " + mensagem, ip, false);
    }

    /**
     * Registra uma ação de sistema (sem usuário específico)
     */
    public void registrarAcaoSistema(String metodo, String endpoint, String mensagem, String ip, boolean sucesso) {
        registrarLog("Sistema", metodo, endpoint, mensagem, ip, sucesso);
    }

    /**
     * Busca logs de um usuário
     */
    public List<Log> buscarLogsPorUsuario(String usuario) {
        return logRepository.findByUsuario(usuario);
    }

    /**
     * Busca logs por endpoint
     */
    public List<Log> buscarLogsPorEndpoint(String endpoint) {
        return logRepository.findByEndpoint(endpoint);
    }

    /**
     * Busca logs por método HTTP
     */
    public List<Log> buscarLogsPorMetodo(String metodo) {
        return logRepository.findByMetodo(metodo);
    }

    /**
     * Busca logs com erro (sucesso = false)
     */
    public List<Log> buscarLogsComErro() {
        return logRepository.findBySucesso(false);
    }

    /**
     * Busca logs por período
     */
    public List<Log> buscarLogsPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return logRepository.findByTimestampBetween(inicio, fim);
    }

    /**
     * Busca todos os logs
     */
    public List<Log> buscarTodosLogs() {
        return logRepository.findAll();
    }
}

