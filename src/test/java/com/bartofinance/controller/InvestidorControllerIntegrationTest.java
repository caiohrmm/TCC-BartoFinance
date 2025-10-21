package com.bartofinance.controller;

import com.bartofinance.dto.request.InvestidorRequest;
import com.bartofinance.dto.request.LoginRequest;
import com.bartofinance.dto.request.RegisterRequest;
import com.bartofinance.dto.response.AuthResponse;
import com.bartofinance.model.enums.PerfilInvestidor;
import com.bartofinance.repository.AssessorRepository;
import com.bartofinance.repository.InvestidorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para InvestidorController
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.data.mongodb.database=bartofinance_test",
    "jwt.secret=testSecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm",
    "jwt.expiration=86400000"
})
class InvestidorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssessorRepository assessorRepository;

    @Autowired
    private InvestidorRepository investidorRepository;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // Limpar banco de dados de teste
        assessorRepository.deleteAll();
        investidorRepository.deleteAll();

        // Registrar e fazer login
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNome("Assessor Teste");
        registerRequest.setEmail("teste@bartofinance.com");
        registerRequest.setSenha("senha123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@bartofinance.com");
        loginRequest.setSenha("senha123");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        // Parse manual do JSON para extrair o token
        token = responseBody.split("\"token\":\"")[1].split("\"")[0];
    }

    @Test
    void deveCriarInvestidorComSucesso() throws Exception {
        // Arrange
        InvestidorRequest request = new InvestidorRequest(
                "João Silva Teste",
                "98765432100",
                "joao.teste@email.com",
                "11987654321",
                PerfilInvestidor.MODERADO,
                new BigDecimal("150000.00"),
                new BigDecimal("10000.00"),
                "Aposentadoria"
        );

        // Act & Assert
        mockMvc.perform(post("/investors")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("João Silva Teste"))
                .andExpect(jsonPath("$.data.cpf").value("98765432100"))
                .andExpect(jsonPath("$.data.perfilInvestidor").value("MODERADO"));
    }

    @Test
    void deveListarInvestidores() throws Exception {
        // Arrange - Criar um investidor primeiro
        InvestidorRequest request = new InvestidorRequest(
                "Maria Santos",
                "11122233344",
                "maria@email.com",
                "11999999999",
                PerfilInvestidor.CONSERVADOR,
                new BigDecimal("80000.00"),
                new BigDecimal("5000.00"),
                "Segurança"
        );

        mockMvc.perform(post("/investors")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get("/investors")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nome").value("Maria Santos"));
    }

    @Test
    void naoDeveCriarInvestidorComCpfDuplicado() throws Exception {
        // Arrange - Criar primeiro investidor
        InvestidorRequest request = new InvestidorRequest(
                "Pedro Costa",
                "55566677788",
                "pedro@email.com",
                "11888888888",
                PerfilInvestidor.AGRESSIVO,
                new BigDecimal("200000.00"),
                new BigDecimal("15000.00"),
                "Alto retorno"
        );

        mockMvc.perform(post("/investors")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Act & Assert - Tentar criar com mesmo CPF
        mockMvc.perform(post("/investors")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar403SemToken() throws Exception {
        // Arrange
        InvestidorRequest request = new InvestidorRequest(
                "Teste Sem Auth",
                "99988877766",
                "teste@email.com",
                "11777777777",
                PerfilInvestidor.MODERADO,
                new BigDecimal("100000.00"),
                new BigDecimal("8000.00"),
                "Teste"
        );

        // Act & Assert - Spring Security retorna 403 (Forbidden) quando não há token
        mockMvc.perform(post("/investors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}

