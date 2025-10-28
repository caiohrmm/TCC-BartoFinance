# 📊 Documentação Completa do Backend - BartoFinance

## 📋 Índice

1. [Visão Geral do Sistema](#visão-geral-do-sistema)
2. [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)
3. [Estrutura do Banco de Dados](#estrutura-do-banco-de-dados)
4. [Modelos e Entidades](#modelos-e-entidades)
5. [Sistema de Autenticação](#sistema-de-autenticação)
6. [APIs e Endpoints](#apis-e-endpoints)
7. [Serviços e Lógica de Negócio](#serviços-e-lógica-de-negócio)
8. [Integração com IA (Gemini)](#integração-com-ia-gemini)
9. [Sistema de Logs e Auditoria](#sistema-de-logs-e-auditoria)
10. [Configurações e Deploy](#configurações-e-deploy)

---

## 🎯 Visão Geral do Sistema

O **BartoFinance** é um sistema completo de assessoria de investimentos desenvolvido em Spring Boot com integração de IA. O sistema permite que assessores financeiros gerenciem seus clientes (investidores), criem carteiras de investimento personalizadas e obtenham insights inteligentes através da API do Google Gemini.

### Principais Funcionalidades:
- ✅ **Gestão de Assessores**: Cadastro e autenticação de assessores financeiros
- ✅ **Gestão de Investidores**: CRUD completo de clientes com perfis de risco
- ✅ **Carteiras de Investimento**: Criação de carteiras modelo e personalizadas
- ✅ **Aplicações Financeiras**: Registro de investimentos em diferentes produtos
- ✅ **Inteligência Artificial**: Análises e insights gerados pelo Gemini AI
- ✅ **Relatórios**: Geração de relatórios consolidados
- ✅ **Auditoria**: Sistema completo de logs para rastreamento

---

## 🏗️ Arquitetura e Tecnologias

### Stack Tecnológico

```xml
<!-- Spring Boot 3.2.0 com Java 17 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>
```

### Principais Dependências

```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Data MongoDB -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<!-- Spring Boot Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>

<!-- Google Generative AI SDK -->
<dependency>
    <groupId>com.google.genai</groupId>
    <artifactId>google-genai</artifactId>
    <version>0.1.0</version>
</dependency>

<!-- Swagger/OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### Arquitetura do Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   MongoDB       │
│   (Angular)     │◄──►│   (Spring Boot) │◄──►│   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   Gemini AI     │
                       │   (Google API)  │
                       └─────────────────┘
```

---

## 🗄️ Estrutura do Banco de Dados

O sistema utiliza **MongoDB** como banco de dados NoSQL, organizando os dados em coleções específicas:

### Coleções Principais

| Coleção | Descrição | Documentos Relacionados |
|---------|-----------|-------------------------|
| `assessores` | Assessores financeiros | - |
| `investidores` | Clientes dos assessores | `assessorId` |
| `portfolios` | Carteiras de investimento | `assessorId`, `investidorId` |
| `aplicacoes` | Aplicações financeiras | `portfolioId` |
| `relatorios` | Relatórios consolidados | `assessorId` |
| `insights` | Insights gerados por IA | `investidorId` |
| `logs` | Logs de auditoria | - |

### Relacionamentos

```
Assessor (1) ──► (N) Investidor
Investidor (1) ──► (N) Portfolio
Portfolio (1) ──► (N) Aplicacao
Assessor (1) ──► (N) Relatorio
Investidor (1) ──► (N) Insight
```

---

## 📊 Modelos e Entidades

### 1. Assessor

```java
@Document(collection = "assessores")
public class Assessor {
    @Id
    private String id;
    
    private String nome;
    
    @Indexed(unique = true)
    private String email;
    
    private String senha; // Criptografada com BCrypt
    
    @CreatedDate
    private LocalDateTime dataCadastro;
    
    private LocalDateTime ultimoLogin;
    
    @Builder.Default
    private Boolean ativo = true;
}
```

**Funcionalidade**: Representa o usuário principal do sistema (assessor financeiro). Possui autenticação JWT e controle de acesso.

### 2. Investidor

```java
@Document(collection = "investidores")
public class Investidor {
    @Id
    private String id;
    
    private String nome;
    
    @Indexed(unique = true)
    private String cpf;
    
    private String email;
    private String telefone;
    
    private PerfilInvestidor perfilInvestidor;
    
    @Builder.Default
    private BigDecimal patrimonioAtual = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal rendaMensal = BigDecimal.ZERO;
    
    private String objetivos;
    private String assessorId; // Referência ao Assessor
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

**Funcionalidade**: Cliente do assessor com perfil de investimento, patrimônio e objetivos financeiros.

### 3. InvestmentPortfolio (Carteira)

```java
@Document(collection = "portfolios")
public class InvestmentPortfolio {
    @Id
    private String id;
    
    private String nome;
    private String descricao;
    
    private TipoCarteira tipo; // MODELO ou PERSONALIZADA
    private RiscoCarteira risco; // BAIXO, MODERADO, ALTO
    
    @Builder.Default
    private BigDecimal metaRentabilidade = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal rentabilidadeAtual = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;
    
    private String investidorId; // Nullable para carteiras modelo
    private String assessorId;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

**Funcionalidade**: Carteira de investimentos que pode ser modelo (template) ou personalizada para um investidor específico.

### 4. Aplicacao

```java
@Document(collection = "aplicacoes")
public class Aplicacao {
    @Id
    private String id;
    
    private String portfolioId; // Referência à Carteira
    
    private TipoProduto tipoProduto;
    private String codigoAtivo; // Ex: PETR4, VALE3, LCI123
    
    @Builder.Default
    private BigDecimal valorAplicado = BigDecimal.ZERO;
    
    @Builder.Default
    private BigDecimal quantidade = BigDecimal.ZERO;
    
    private LocalDateTime dataCompra;
    private LocalDateTime dataVenda; // Opcional
    
    @Builder.Default
    private BigDecimal rentabilidadeAtual = BigDecimal.ZERO;
    
    @Builder.Default
    private StatusAplicacao status = StatusAplicacao.ATIVA;
    
    private String notas;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

**Funcionalidade**: Registra investimentos específicos dentro de uma carteira, com controle de rentabilidade e status.

### 5. Enums Principais

#### PerfilInvestidor
```java
public enum PerfilInvestidor {
    CONSERVADOR("Conservador - Prioriza segurança e baixo risco"),
    MODERADO("Moderado - Busca equilíbrio entre segurança e rentabilidade"),
    AGRESSIVO("Agressivo - Aceita alto risco em busca de maior rentabilidade");
}
```

#### TipoProduto
```java
public enum TipoProduto {
    CDB("Certificado de Depósito Bancário"),
    TESOURO_DIRETO("Tesouro Direto"),
    ACOES("Ações"),
    FUNDOS("Fundos de Investimento"),
    CRIPTOMOEDAS("Criptomoedas"),
    FII("Fundos Imobiliários"),
    LCI("Letra de Crédito Imobiliário"),
    LCA("Letra de Crédito do Agronegócio"),
    OUTROS("Outros");
}
```

---

## 🔐 Sistema de Autenticação

### JWT (JSON Web Token)

O sistema utiliza JWT para autenticação stateless com as seguintes características:

#### Configuração JWT

```yaml
# application.yml
jwt:
  secret: ${JWT_SECRET:BartoFinanceSecretKeyForJWTTokenGenerationAndValidation2024MustBeAtLeast256BitsLong}
  expiration: 86400000 # 24 horas em millisegundos
```

#### Implementação JwtUtil

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Gera token JWT para o usuário
     */
    public String generateToken(String email, String assessorId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("assessorId", assessorId);
        return createToken(claims, email);
    }
    
    /**
     * Valida o token verificando email e expiração
     */
    public Boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }
}
```

### Fluxo de Autenticação

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A as AuthController
    participant S as AuthService
    participant J as JwtUtil
    participant R as AssessorRepository
    
    C->>A: POST /auth/login
    A->>S: login(request, ip)
    S->>R: findByEmail(email)
    R-->>S: Assessor
    S->>S: Verificar senha (BCrypt)
    S->>J: generateToken(email, assessorId)
    J-->>S: JWT Token
    S-->>A: AuthResponse
    A-->>C: Token + Dados do Assessor
```

### Criptografia de Senhas

```java
@Service
public class AuthService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthResponse register(RegisterRequest request, String ip) {
        // Cria o novo assessor com senha criptografada
        Assessor assessor = Assessor.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // BCrypt
                .dataCadastro(LocalDateTime.now())
                .ativo(true)
                .build();
        
        assessor = assessorRepository.save(assessor);
        
        // Gera o token JWT
        String token = jwtUtil.generateToken(assessor.getEmail(), assessor.getId());
        
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .assessorId(assessor.getId())
                .nome(assessor.getNome())
                .email(assessor.getEmail())
                .mensagem("Assessor registrado com sucesso!")
                .build();
    }
}
```

---

## 🌐 APIs e Endpoints

### 1. Autenticação (`/auth`)

#### POST `/auth/register`
**Descrição**: Registra um novo assessor no sistema

**Request Body**:
```json
{
  "nome": "João Silva",
  "email": "joao@bartofinance.com",
  "senha": "senha123"
}
```

**Response**:
```json
{
  "sucesso": true,
  "mensagem": "Assessor registrado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "assessorId": "64f8a1b2c3d4e5f6a7b8c9d0",
    "nome": "João Silva",
    "email": "joao@bartofinance.com"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

#### POST `/auth/login`
**Descrição**: Autentica um assessor e retorna o token JWT

**Request Body**:
```json
{
  "email": "joao@bartofinance.com",
  "senha": "senha123"
}
```

**Response**: Mesmo formato do registro

### 2. Investidores (`/investors`)

#### POST `/investors`
**Descrição**: Cria um novo investidor

**Headers**: `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "nome": "Maria Santos",
  "cpf": "12345678901",
  "email": "maria@email.com",
  "telefone": "(11) 99999-9999",
  "perfilInvestidor": "MODERADO",
  "patrimonioAtual": 50000.00,
  "rendaMensal": 8000.00,
  "objetivos": "Aposentadoria e reserva de emergência"
}
```

#### GET `/investors`
**Descrição**: Lista todos os investidores do assessor

**Query Parameters**:
- `perfilInvestidor` (opcional): Filtra por perfil específico

#### GET `/investors/{id}`
**Descrição**: Busca investidor específico por ID

#### PUT `/investors/{id}`
**Descrição**: Atualiza dados do investidor

#### DELETE `/investors/{id}`
**Descrição**: Remove investidor do sistema

### 3. Carteiras (`/portfolios`)

#### POST `/portfolios`
**Descrição**: Cria uma nova carteira de investimentos

**Request Body**:
```json
{
  "nome": "Carteira Conservadora",
  "descricao": "Carteira focada em segurança",
  "tipo": "PERSONALIZADA",
  "risco": "BAIXO",
  "metaRentabilidade": 8.5,
  "investidorId": "64f8a1b2c3d4e5f6a7b8c9d1"
}
```

#### GET `/portfolios`
**Descrição**: Lista todas as carteiras do assessor

#### GET `/portfolios/models`
**Descrição**: Lista carteiras modelo (templates)

#### POST `/portfolios/simulate`
**Descrição**: Simula o desempenho de uma carteira hipotética

### 4. Aplicações (`/applications`)

#### POST `/applications`
**Descrição**: Registra uma nova aplicação financeira

**Request Body**:
```json
{
  "portfolioId": "64f8a1b2c3d4e5f6a7b8c9d2",
  "tipoProduto": "CDB",
  "codigoAtivo": "CDB123456",
  "valorAplicado": 10000.00,
  "quantidade": 1.0,
  "dataCompra": "2024-01-15T10:30:00",
  "rentabilidadeAtual": 12.5,
  "notas": "CDB com liquidez diária"
}
```

#### GET `/applications`
**Descrição**: Lista aplicações com filtros opcionais

**Query Parameters**:
- `portfolioId`: Filtra por carteira específica
- `status`: Filtra por status (ATIVA, ENCERRADA)

#### PATCH `/applications/{id}/encerrar`
**Descrição**: Encerra uma aplicação (registra venda)

**Request Body**:
```json
{
  "dataVenda": "2024-01-20T15:30:00",
  "rentabilidadeAtual": 15.2
}
```

### 5. Inteligência Artificial (`/ai`)

#### POST `/ai/analisar-perfil`
**Descrição**: Analisa perfil de investidor com IA

**Request Body**:
```json
{
  "nome": "Maria Santos",
  "perfil": "MODERADO",
  "rendaMensal": 8000.0,
  "patrimonioAtual": 50000.0
}
```

**Response**:
```json
{
  "sucesso": true,
  "mensagem": "Análise gerada com sucesso",
  "data": {
    "analise": "Para um perfil moderado com renda de R$ 8.000 e patrimônio de R$ 50.000, recomendo uma diversificação equilibrada..."
  }
}
```

#### POST `/ai/analisar-carteira`
**Descrição**: Analisa carteira de investimentos

#### POST `/ai/sugerir-diversificacao`
**Descrição**: Sugere estratégia de diversificação

#### POST `/ai/gerar-insight`
**Descrição**: Gera insight genérico usando IA

---

## ⚙️ Serviços e Lógica de Negócio

### 1. AuthService

```java
@Service
public class AuthService {
    
    @Autowired
    private AssessorRepository assessorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Registra um novo assessor no sistema
     */
    public AuthResponse register(RegisterRequest request, String ip) {
        // Verifica se o email já existe
        if (assessorRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email já cadastrado no sistema");
        }
        
        // Cria o novo assessor com senha criptografada
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
        
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .assessorId(assessor.getId())
                .nome(assessor.getNome())
                .email(assessor.getEmail())
                .mensagem("Assessor registrado com sucesso!")
                .build();
    }
}
```

**Funcionalidade**: Gerencia autenticação, registro e validação de assessores.

### 2. InvestidorService

```java
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
}
```

**Funcionalidade**: Gerencia CRUD de investidores com validações de negócio.

### 3. PortfolioService

```java
@Service
@Slf4j
public class PortfolioService {
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private AplicacaoRepository aplicacaoRepository;
    
    /**
     * Cria uma nova carteira
     */
    public PortfolioResponse criarPortfolio(PortfolioRequest request, String assessorId) {
        log.info("Criando nova carteira: {} para assessor: {}", request.getNome(), assessorId);
        
        InvestmentPortfolio portfolio = InvestmentPortfolio.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .tipo(request.getTipo())
                .risco(request.getRisco())
                .metaRentabilidade(request.getMetaRentabilidade())
                .investidorId(request.getInvestidorId())
                .assessorId(assessorId)
                .createdAt(LocalDateTime.now())
                .build();
        
        portfolio = portfolioRepository.save(portfolio);
        log.info("Carteira criada com sucesso: ID {}", portfolio.getId());
        
        return mapToResponse(portfolio);
    }
    
    /**
     * Calcula rentabilidade atual da carteira
     */
    public BigDecimal calcularRentabilidadeAtual(String portfolioId) {
        List<Aplicacao> aplicacoes = aplicacaoRepository.findByPortfolioId(portfolioId);
        
        if (aplicacoes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal valorTotalAplicado = aplicacoes.stream()
                .map(Aplicacao::getValorAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal valorTotalAtual = aplicacoes.stream()
                .map(aplicacao -> aplicacao.getValorAplicado()
                        .multiply(BigDecimal.ONE.add(aplicacao.getRentabilidadeAtual().divide(BigDecimal.valueOf(100)))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (valorTotalAplicado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return valorTotalAtual.subtract(valorTotalAplicado)
                .divide(valorTotalAplicado, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
```

**Funcionalidade**: Gerencia carteiras e calcula rentabilidades automaticamente.

---

## 🤖 Integração com IA (Gemini)

### GeminiAIService

```java
@Service
@Slf4j
public class GeminiAIService {
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.model:gemini-1.5-flash}")
    private String model;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    
    /**
     * Gera conteúdo usando Gemini AI via REST API
     */
    public String generateContent(String prompt) {
        try {
            log.info("Enviando requisição para Gemini AI via REST...");
            
            // Verificar se a API key é válida
            if (apiKey == null || apiKey.isEmpty()) {
                log.warn("API key não configurada, usando fallback");
                return generateFallbackResponse(prompt);
            }
            
            // Construir URL com API key
            String url = GEMINI_API_URL + model + ":generateContent?key=" + apiKey;
            
            // Construir body da requisição
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);
            
            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(part));
            
            requestBody.put("contents", Collections.singletonList(content));
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Fazer requisição
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Processar resposta...
                return extractTextFromResponse(response.getBody());
            }
            
            log.warn("Resposta inválida do Gemini, usando fallback");
            return generateFallbackResponse(prompt);
            
        } catch (Exception e) {
            log.error("Erro ao se comunicar com Gemini AI: {}", e.getMessage());
            return generateFallbackResponse(prompt);
        }
    }
    
    /**
     * Gera análise de perfil de investidor
     */
    public String analisarPerfilInvestidor(String nome, String perfil, Double rendaMensal, Double patrimonioAtual) {
        String prompt = String.format(
            "Você é um assessor de investimentos experiente. Analise o seguinte perfil de investidor e forneça uma breve recomendação (máximo 150 palavras):\n\n" +
            "Nome: %s\n" +
            "Perfil de Investidor: %s\n" +
            "Renda Mensal: R$ %.2f\n" +
            "Patrimônio Atual: R$ %.2f\n\n" +
            "Forneça recomendações práticas e relevantes para este perfil.",
            nome, perfil, rendaMensal, patrimonioAtual
        );
        
        return generateContent(prompt);
    }
}
```

### Sistema de Fallback

O sistema possui um mecanismo robusto de fallback que funciona quando a API do Gemini não está disponível:

```java
private String generateFallbackResponse(String prompt) {
    String lowerPrompt = prompt.toLowerCase().trim();
    
    // Saudações e cumprimentos
    if (lowerPrompt.matches("^(oi|olá|ola|hello|hi|bom dia|boa tarde|boa noite)$")) {
        return "👋 **Olá! Bem-vindo ao BartoFinance!**\n\n" +
               "Sou seu assistente virtual especializado em assessoria de investimentos.\n\n" +
               "Posso ajudar com:\n" +
               "• 📊 Análise de perfis de investidores\n" +
               "• 🎯 Estratégias de diversificação\n" +
               "• 📈 Definição de metas de rentabilidade\n" +
               "• 🏦 Interpretação de produtos financeiros\n" +
               "• 💼 Gestão de carteiras\n\n" +
               "💡 **Como posso auxiliá-lo hoje?**";
    }
    
    // Análise de perfis
    if (lowerPrompt.contains("perfil") || lowerPrompt.contains("investidor")) {
        return "📊 **Análise de Perfil de Investidor**\n\n" +
               "**Conservador**: Prioriza segurança, prefere renda fixa (CDB, Tesouro Direto, LCI/LCA)\n" +
               "**Moderado**: Equilibra segurança e rentabilidade, diversifica entre renda fixa e variável\n" +
               "**Agressivo**: Aceita maior risco por maior potencial de retorno, foca em ações e fundos\n\n" +
               "💡 **Dica**: Analise a tolerância ao risco, objetivos e horizonte de investimento do cliente.";
    }
    
    // Resposta padrão
    return "🤖 **Assistente BartoFinance**\n\n" +
           "Entendi sua pergunta! Posso ajudar com:\n\n" +
           "• 📊 **Perfis de investidor** (Conservador, Moderado, Agressivo)\n" +
           "• 🎯 **Diversificação** de carteiras\n" +
           "• 📈 **Metas de rentabilidade** realistas\n" +
           "• 🏦 **Produtos financeiros** (CDB, Tesouro, Ações, Fundos)\n" +
           "• ⚖️ **Gestão de risco**\n\n" +
           "💡 **Seja mais específico na sua pergunta para uma resposta mais detalhada!**";
}
```

---

## 📝 Sistema de Logs e Auditoria

### LoggingAspect (AOP)

O sistema utiliza Aspect-Oriented Programming (AOP) para logging automático de todas as requisições:

```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    @Autowired
    private LogRepository logRepository;
    
    /**
     * Intercepta todas as chamadas aos controllers REST
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return joinPoint.proceed();
        }
        
        String endpoint = request.getRequestURI();
        String metodo = request.getMethod();
        String usuario = getUsuarioAtual();
        String ip = getClientIp(request);
        
        log.debug("Interceptando requisição: {} {} - Usuário: {}", metodo, endpoint, usuario);
        
        Integer statusCode = 200;
        boolean sucesso = true;
        String mensagem = "Requisição executada com sucesso";
        
        try {
            // Executa o método
            Object result = joinPoint.proceed();
            
            // Tenta inferir status code baseado no resultado
            if (result != null && result.getClass().getSimpleName().contains("ResponseEntity")) {
                try {
                    org.springframework.http.ResponseEntity<?> response = (org.springframework.http.ResponseEntity<?>) result;
                    statusCode = response.getStatusCode().value();
                    sucesso = statusCode >= 200 && statusCode < 400;
                } catch (Exception ex) {
                    // Ignora se não conseguir extrair
                }
            }
            
            // Log de sucesso
            Log logEntry = Log.builder()
                    .usuario(usuario)
                    .endpoint(endpoint)
                    .metodo(metodo)
                    .ip(ip)
                    .statusCode(statusCode)
                    .sucesso(sucesso)
                    .mensagem(mensagem)
                    .timestamp(LocalDateTime.now())
                    .build();
            
            logRepository.save(logEntry);
            log.debug("Log registrado para: {} {} [{}]", metodo, endpoint, statusCode);
            
            return result;
            
        } catch (Exception e) {
            // Determina status code baseado no tipo de erro
            statusCode = determineStatusCode(e);
            mensagem = "Erro: " + e.getMessage();
            sucesso = false;
            
            // Log de erro
            Log logEntry = Log.builder()
                    .usuario(usuario)
                    .endpoint(endpoint)
                    .metodo(metodo)
                    .ip(ip)
                    .statusCode(statusCode)
                    .sucesso(sucesso)
                    .mensagem(mensagem)
                    .timestamp(LocalDateTime.now())
                    .build();
            
            logRepository.save(logEntry);
            log.error("Log de erro registrado para: {} {} [{}] - Erro: {}", metodo, endpoint, statusCode, e.getMessage());
            
            throw e;
        }
    }
}
```

### Modelo de Log

```java
@Document(collection = "logs")
public class Log {
    @Id
    private String id;
    
    private String usuario; // Email ou ID do assessor
    private String endpoint;
    private String metodo; // GET, POST, PUT, DELETE
    private String ip;
    private Integer statusCode; // HTTP Status Code
    private Boolean sucesso;
    private String mensagem;
    
    @CreatedDate
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
```

### Configuração de Logging

```yaml
# application.yml
logging:
  level:
    root: INFO
    com.bartofinance: DEBUG
    org.springframework.web: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/bartofinance.log
```

---

## ⚙️ Configurações e Deploy

### Configuração Principal (application.yml)

```yaml
spring:
  application:
    name: bartofinance-backend
  
  data:
    mongodb:
      uri: mongodb://localhost:27017/bartofinance
      # Para MongoDB Atlas (cloud), use o formato:
      # uri: mongodb+srv://<username>:<password>@cluster.mongodb.net/bartofinance?retryWrites=true&w=majority
  
  jackson:
    serialization:
      write-dates-as-timestamps: false
    time-zone: America/Sao_Paulo

server:
  port: 8080
  address: 0.0.0.0 # Aceita conexões de qualquer IP
  error:
    include-message: always
    include-binding-errors: always

# JWT Configuration
jwt:
  secret: ${JWT_SECRET:BartoFinanceSecretKeyForJWTTokenGenerationAndValidation2024MustBeAtLeast256BitsLong}
  expiration: 86400000 # 24 horas em millisegundos

# Gemini AI Configuration
gemini:
  api:
    key: AIzaSyCAJX9yD-xeE9zyTJRxS4naOg9TdlGm_o8
    model: gemini-2.5-flash

# Swagger/OpenAPI Configuration
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
```

### Docker Configuration

#### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/bartofinance-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml
```yaml
version: '3.8'

services:
  bartofinance-backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/bartofinance
      - JWT_SECRET=BartoFinanceSecretKeyForJWTTokenGenerationAndValidation2024MustBeAtLeast256BitsLong
    depends_on:
      - mongodb
    networks:
      - bartofinance-network

  mongodb:
    image: mongo:7.0
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    networks:
      - bartofinance-network

volumes:
  mongodb_data:

networks:
  bartofinance-network:
    driver: bridge
```

### Deploy Commands

```bash
# Compilar o projeto
mvn clean package -DskipTests

# Executar localmente
java -jar target/bartofinance-backend-1.0.0.jar

# Build Docker
docker build -t bartofinance-backend .

# Executar com Docker Compose
docker-compose up -d

# Verificar logs
docker-compose logs -f bartofinance-backend
```

### Health Check

```java
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("service", "BartoFinance Backend");
        status.put("version", "1.0.0");
        
        return ResponseEntity.ok(status);
    }
}
```

---

## 📚 Documentação da API

### Swagger/OpenAPI

O sistema possui documentação automática da API através do Swagger UI:

- **URL Local**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

### Configuração OpenAPI

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BartoFinance API")
                        .version("1.0.0")
                        .description("Sistema de Assessoria de Investimentos com IA"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

---

## 🔧 Utilitários e Validações

### AuthUtil

```java
@Component
public class AuthUtil {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Extrai o ID do assessor do token JWT
     */
    public String getAssessorId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }
        
        String token = extractTokenFromAuthentication(authentication);
        return jwtUtil.extractAssessorId(token);
    }
    
    private String extractTokenFromAuthentication(Authentication authentication) {
        // Implementação para extrair token do contexto de segurança
        return null; // Simplificado para exemplo
    }
}
```

### Validações Customizadas

```java
@Component
public class CpfValidator implements ConstraintValidator<CpfValid, String> {
    
    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação do algoritmo do CPF
        return isValidCpfAlgorithm(cpf);
    }
    
    private boolean isValidCpfAlgorithm(String cpf) {
        // Implementação do algoritmo de validação do CPF
        return true; // Simplificado para exemplo
    }
}
```

---

## 🚀 Conclusão

O **BartoFinance Backend** é um sistema robusto e completo para assessoria de investimentos, desenvolvido com as melhores práticas de desenvolvimento:

### ✅ Características Principais:

1. **Arquitetura Moderna**: Spring Boot 3.2.0 com Java 17
2. **Segurança Robusta**: JWT + BCrypt + Spring Security
3. **Banco NoSQL**: MongoDB para flexibilidade e escalabilidade
4. **IA Integrada**: Google Gemini para análises inteligentes
5. **Auditoria Completa**: Sistema de logs com AOP
6. **Documentação Automática**: Swagger/OpenAPI
7. **Fallback Inteligente**: Sistema resiliente quando IA não está disponível
8. **Docker Ready**: Containerização completa

### 🎯 Funcionalidades Implementadas:

- ✅ Autenticação e autorização JWT
- ✅ CRUD completo de assessores e investidores
- ✅ Gestão de carteiras modelo e personalizadas
- ✅ Registro de aplicações financeiras
- ✅ Análises de IA com Gemini
- ✅ Sistema de logs e auditoria
- ✅ Relatórios consolidados
- ✅ Validações de negócio
- ✅ Tratamento de exceções global
- ✅ Documentação automática da API

### 📈 Próximos Passos Sugeridos:

1. **Testes Automatizados**: Implementar testes unitários e de integração
2. **Cache**: Adicionar Redis para cache de consultas frequentes
3. **Notificações**: Sistema de notificações em tempo real
4. **Relatórios Avançados**: Dashboards e gráficos
5. **Integração Externa**: APIs de cotações em tempo real
6. **Backup Automático**: Estratégias de backup do MongoDB
7. **Monitoramento**: Métricas e alertas com Prometheus/Grafana

---

**Desenvolvido com ❤️ pela equipe BartoFinance**

*Documentação gerada automaticamente - Versão 1.0.0*
