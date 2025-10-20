package com.bartofinance.service;

import com.bartofinance.dto.request.LoginRequest;
import com.bartofinance.dto.request.RegisterRequest;
import com.bartofinance.dto.response.AuthResponse;
import com.bartofinance.exception.BadRequestException;
import com.bartofinance.exception.UnauthorizedException;
import com.bartofinance.model.Assessor;
import com.bartofinance.repository.AssessorRepository;
import com.bartofinance.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Serviço responsável pela autenticação e registro de assessores
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogService logService;

    /**
     * Registra um novo assessor no sistema
     */
    public AuthResponse register(RegisterRequest request, String ip) {
        logger.info("Iniciando registro de novo assessor: {}", request.getEmail());

        // Verifica se o email já existe
        if (assessorRepository.existsByEmail(request.getEmail())) {
            logger.warn("Tentativa de registro com email já existente: {}", request.getEmail());
            logService.logAcaoSistema(
                "REGISTER_FAILED",
                "Tentativa de registro com email já existente: " + request.getEmail(),
                "/auth/register",
                ip,
                false
            );
            throw new BadRequestException("Email já cadastrado no sistema");
        }

        // Cria o novo assessor
        Assessor assessor = Assessor.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .dataCadastro(LocalDateTime.now())
                .ativo(true)
                .build();

        assessor = assessorRepository.save(assessor);

        // Gera o token JWT
        String token = jwtUtil.generateToken(assessor.getEmail(), assessor.getId());

        // Registra log de sucesso
        logService.logAcao(
            assessor.getId(),
            "REGISTER_SUCCESS",
            "Novo assessor registrado: " + assessor.getNome(),
            "/auth/register",
            ip,
            true
        );

        logger.info("Assessor registrado com sucesso: {} (ID: {})", assessor.getEmail(), assessor.getId());

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .assessorId(assessor.getId())
                .nome(assessor.getNome())
                .email(assessor.getEmail())
                .mensagem("Assessor registrado com sucesso!")
                .build();
    }

    /**
     * Realiza o login de um assessor
     */
    public AuthResponse login(LoginRequest request, String ip) {
        logger.info("Tentativa de login para: {}", request.getEmail());

        // Busca o assessor pelo email
        Assessor assessor = assessorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logService.logAcaoSistema(
                        "LOGIN_FAILED",
                        "Tentativa de login com email não encontrado: " + request.getEmail(),
                        "/auth/login",
                        ip,
                        false
                    );
                    return new UnauthorizedException("Email ou senha inválidos");
                });

        // Verifica se o assessor está ativo
        if (!assessor.getAtivo()) {
            logger.warn("Tentativa de login de assessor inativo: {}", request.getEmail());
            logService.logAcao(
                assessor.getId(),
                "LOGIN_FAILED",
                "Tentativa de login de assessor inativo",
                "/auth/login",
                ip,
                false
            );
            throw new UnauthorizedException("Assessor inativo. Entre em contato com o administrador.");
        }

        // Verifica a senha
        if (!passwordEncoder.matches(request.getSenha(), assessor.getSenha())) {
            logger.warn("Senha incorreta para: {}", request.getEmail());
            logService.logAcao(
                assessor.getId(),
                "LOGIN_FAILED",
                "Senha incorreta",
                "/auth/login",
                ip,
                false
            );
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        // Atualiza o último login
        assessor.setUltimoLogin(LocalDateTime.now());
        assessorRepository.save(assessor);

        // Gera o token JWT
        String token = jwtUtil.generateToken(assessor.getEmail(), assessor.getId());

        // Registra log de sucesso
        logService.logAcao(
            assessor.getId(),
            "LOGIN_SUCCESS",
            "Login realizado com sucesso",
            "/auth/login",
            ip,
            true
        );

        logger.info("Login realizado com sucesso: {} (ID: {})", assessor.getEmail(), assessor.getId());

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .assessorId(assessor.getId())
                .nome(assessor.getNome())
                .email(assessor.getEmail())
                .mensagem("Login realizado com sucesso!")
                .build();
    }

    /**
     * Busca um assessor por email
     */
    public Assessor getAssessorByEmail(String email) {
        return assessorRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Assessor não encontrado"));
    }
}

