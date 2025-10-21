package com.bartofinance.service;

import com.bartofinance.dto.request.InvestidorRequest;
import com.bartofinance.dto.response.InvestidorResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.ResourceNotFoundException;
import com.bartofinance.model.Investidor;
import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.repository.InvestidorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Investidores
 */
@Service
@Slf4j
public class InvestidorService {

    @Autowired
    private InvestidorRepository investidorRepository;

    /**
     * Cria um novo investidor
     */
    public InvestidorResponse criarInvestidor(InvestidorRequest request, String assessorId) {
        log.info("Criando novo investidor: {} para assessor: {}", request.getNome(), assessorId);

        // Valida CPF único APENAS para este assessor
        if (investidorRepository.existsByCpfAndAssessorId(request.getCpf(), assessorId)) {
            throw new BadRequestException("CPF já cadastrado para este assessor");
        }

        Investidor investidor = Investidor.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .perfilInvestidor(request.getPerfilInvestidor())
                .patrimonioAtual(request.getPatrimonioAtual())
                .rendaMensal(request.getRendaMensal())
                .objetivos(request.getObjetivos())
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();

        investidor = investidorRepository.save(investidor);
        log.info("Investidor criado com sucesso: ID {}", investidor.getId());

        return mapToResponse(investidor);
    }

    /**
     * Busca todos os investidores de um assessor
     */
    public List<InvestidorResponse> listarInvestidores(String assessorId) {
        log.info("Listando investidores do assessor: {}", assessorId);
        return investidorRepository.findByAssessorId(assessorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca investidores por perfil
     */
    public List<InvestidorResponse> listarPorPerfil(String assessorId, String perfilInvestidor) {
        log.info("Listando investidores do assessor: {} com perfil: {}", assessorId, perfilInvestidor);
        return investidorRepository.findByPerfilInvestidor(PerfilInvestidor.valueOf(perfilInvestidor.toUpperCase()))
                .stream()
                .filter(inv -> inv.getAssessorId().equals(assessorId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca investidor por ID
     */
    public InvestidorResponse buscarPorId(String id, String assessorId) {
        log.info("Buscando investidor: {}", id);
        Investidor investidor = investidorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", id));

        // Valida se o investidor pertence ao assessor
        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Investidor não pertence a este assessor");
        }

        return mapToResponse(investidor);
    }

    /**
     * Atualiza um investidor
     */
    public InvestidorResponse atualizarInvestidor(String id, InvestidorRequest request, String assessorId) {
        log.info("Atualizando investidor: {}", id);
        Investidor investidor = investidorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", id));

        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Investidor não pertence a este assessor");
        }

        // Valida CPF único (se alterado) APENAS para este assessor
        if (!investidor.getCpf().equals(request.getCpf())) {
            Optional<Investidor> investidorExistente = investidorRepository.findByCpfAndAssessorId(request.getCpf(), assessorId);
            if (investidorExistente.isPresent() && !investidorExistente.get().getId().equals(id)) {
                throw new BadRequestException("CPF já cadastrado para outro investidor deste assessor");
            }
        }

        investidor.setNome(request.getNome());
        investidor.setCpf(request.getCpf());
        investidor.setEmail(request.getEmail());
        investidor.setTelefone(request.getTelefone());
        investidor.setPerfilInvestidor(request.getPerfilInvestidor());
        investidor.setPatrimonioAtual(request.getPatrimonioAtual());
        investidor.setRendaMensal(request.getRendaMensal());
        investidor.setObjetivos(request.getObjetivos());
        investidor.setUpdatedAt(LocalDateTime.now());

        investidor = investidorRepository.save(investidor);
        log.info("Investidor atualizado com sucesso: ID {}", investidor.getId());

        return mapToResponse(investidor);
    }

    /**
     * Deleta um investidor
     */
    public void deletarInvestidor(String id, String assessorId) {
        log.info("Deletando investidor: {}", id);
        Investidor investidor = investidorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", id));

        if (!investidor.getAssessorId().equals(assessorId)) {
            throw new BadRequestException("Investidor não pertence a este assessor");
        }

        investidorRepository.delete(investidor);
        log.info("Investidor deletado com sucesso: ID {}", id);
    }

    private InvestidorResponse mapToResponse(Investidor investidor) {
        return InvestidorResponse.builder()
                .id(investidor.getId())
                .nome(investidor.getNome())
                .cpf(investidor.getCpf())
                .email(investidor.getEmail())
                .telefone(investidor.getTelefone())
                .perfilInvestidor(investidor.getPerfilInvestidor())
                .patrimonioAtual(investidor.getPatrimonioAtual())
                .rendaMensal(investidor.getRendaMensal())
                .objetivos(investidor.getObjetivos())
                .assessorId(investidor.getAssessorId())
                .createdAt(investidor.getCreatedAt())
                .updatedAt(investidor.getUpdatedAt())
                .build();
    }
}

