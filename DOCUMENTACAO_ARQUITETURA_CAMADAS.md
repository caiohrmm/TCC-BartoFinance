# 🏗️ Documentação: Arquitetura e Comunicação entre Camadas

## 📋 Índice

1. [Visão Geral da Arquitetura](#visão-geral-da-arquitetura)
2. [Arquitetura em Camadas](#arquitetura-em-camadas)
3. [Fluxo Completo de Requisição](#fluxo-completo-de-requisição)
4. [Fluxo de Autenticação JWT](#fluxo-de-autenticação-jwt)
5. [Fluxo de Tratamento de Erros](#fluxo-de-tratamento-de-erros)
6. [Fluxo de Logging Automático (AOP)](#fluxo-de-logging-automático-aop)
7. [Fluxo de Validação de Dados](#fluxo-de-validação-de-dados)
8. [Comunicação Frontend-Backend](#comunicação-frontend-backend)
9. [Padrões Arquiteturais Aplicados](#padrões-arquiteturais-aplicados)

---

## 🎯 Visão Geral da Arquitetura

O **BartoFinance** segue uma arquitetura em camadas (Layered Architecture) com separação clara de responsabilidades, facilitando manutenção, testes e escalabilidade.

### Princípios Arquiteturais

- ✅ **Separação de Responsabilidades**: Cada camada tem uma responsabilidade específica
- ✅ **Inversão de Dependências**: Camadas superiores dependem de abstrações
- ✅ **Single Responsibility**: Cada classe tem uma única responsabilidade
- ✅ **Open/Closed Principle**: Aberto para extensão, fechado para modificação
- ✅ **Dependency Injection**: Dependências injetadas via Spring

---

## 🏛️ Arquitetura em Camadas

### Visão Geral das Camadas

```mermaid
graph TB
    subgraph Frontend["🌐 Frontend (Angular)"]
        UI[Componentes UI<br/>Templates HTML]
        Services[Services<br/>HTTP Clients]
        Guards[Guards<br/>Proteção de Rotas]
        Interceptors[Interceptors<br/>Autenticação]
    end
    
    subgraph Backend["⚙️ Backend (Spring Boot)"]
        Controllers[Controller Layer<br/>REST Endpoints]
        Services[Service Layer<br/>Lógica de Negócio]
        Repositories[Repository Layer<br/>Acesso a Dados]
        Models[Model Layer<br/>Entidades de Domínio]
    end
    
    subgraph Security["🔐 Security Layer"]
        JWTFilter[JWT Filter<br/>Validação de Token]
        SecurityConfig[Security Config<br/>Configurações]
    end
    
    subgraph CrossCutting["🔧 Cross-Cutting Concerns"]
        AOP[AOP Aspect<br/>Logging Automático]
        ExceptionHandler[Exception Handler<br/>Tratamento Global]
        Validators[Validators<br/>Validação de Dados]
    end
    
    subgraph Database["💾 Database"]
        MongoDB[(MongoDB<br/>NoSQL Database)]
    end
    
    UI --> Services
    Services --> Guards
    Guards --> Interceptors
    Interceptors -->|HTTP Request| Controllers
    
    Controllers -->|Valida DTO| Validators
    Controllers -->|Chama| Services
    Services -->|Usa| Repositories
    Repositories -->|Persiste| Models
    Models -->|Salva| MongoDB
    
    JWTFilter -->|Intercepta| Controllers
    SecurityConfig -->|Configura| JWTFilter
    
    AOP -->|Intercepta| Controllers
    ExceptionHandler -->|Captura| Controllers
    
    style Frontend fill:#e1f5ff
    style Backend fill:#fff4e1
    style Security fill:#ffe1e1
    style CrossCutting fill:#f0e1ff
    style Database fill:#e1ffe1
```

### Detalhamento das Camadas

#### 1. **Frontend Layer (Angular)**

```mermaid
graph LR
    subgraph FrontendLayer["Frontend Layer"]
        Components[Components<br/>- DashboardComponent<br/>- InvestidorComponent<br/>- PortfolioComponent]
        Services[Services<br/>- AuthService<br/>- InvestidorService<br/>- PortfolioService<br/>- BrapiService]
        Guards[Guards<br/>- AuthGuard<br/>- GuestGuard]
        Interceptors[Interceptors<br/>- AuthInterceptor]
        Models[Models<br/>- DTOs TypeScript]
    end
    
    Components --> Services
    Services --> Interceptors
    Interceptors --> Guards
    Services --> Models
    
    style Components fill:#e1f5ff
    style Services fill:#fff4e1
    style Guards fill:#ffe1e1
    style Interceptors fill:#f0e1ff
    style Models fill:#e1ffe1
```

#### 2. **Backend Layer (Spring Boot)**

```mermaid
graph TB
    subgraph BackendLayer["Backend Layer"]
        Controllers["Controllers<br/>- AuthController<br/>- InvestidorController<br/>- PortfolioController<br/>- AplicacaoController"]
        Services["Services<br/>- AuthService<br/>- InvestidorService<br/>- PortfolioService<br/>- AplicacaoService"]
        Repositories["Repositories<br/>- InvestidorRepository<br/>- PortfolioRepository<br/>- AplicacaoRepository"]
        Models["Models<br/>- Investidor<br/>- InvestmentPortfolio<br/>- Aplicacao"]
        DTOs["DTOs<br/>- Request DTOs<br/>- Response DTOs"]
    end
    
    Controllers -->|Recebe| DTOs
    Controllers -->|Chama| Services
    Services -->|Usa| Repositories
    Repositories -->|Retorna| Models
    Services -->|Retorna| DTOs
    Controllers -->|Retorna| DTOs
    
    style Controllers fill:#e1f5ff
    style Services fill:#fff4e1
    style Repositories fill:#ffe1e1
    style Models fill:#f0e1ff
    style DTOs fill:#e1ffe1
```

---

## 🔄 Fluxo Completo de Requisição

### Fluxo: Criar Investidor (POST /investors)

```mermaid
sequenceDiagram
    participant User as 👤 Usuário
    participant Component as Component (Angular)
    participant Service as Service (Angular)
    participant Interceptor as AuthInterceptor
    participant Controller as InvestidorController
    participant Validator as Bean Validator
    participant ServiceBackend as InvestidorService
    participant Repository as InvestidorRepository
    participant MongoDB as MongoDB
    participant AOP as LoggingAspect
    participant ExceptionHandler as GlobalExceptionHandler

    User->>Component: Preenche formulário
    Component->>Service: criarInvestidor(request)
    Service->>Interceptor: HTTP Request + Token
    Interceptor->>Interceptor: Adiciona Authorization Header
    Interceptor->>Controller: POST /investors
    
    Controller->>Validator: Valida @Valid InvestidorRequest
    alt Validação Falha
        Validator-->>Controller: ValidationException
        Controller->>ExceptionHandler: Captura exceção
        ExceptionHandler-->>Component: 400 Bad Request
    else Validação OK
        Controller->>AOP: Intercepta (antes)
        AOP->>AOP: Registra início da requisição
        Controller->>ServiceBackend: criarInvestidor(request, assessorId)
        ServiceBackend->>ServiceBackend: Valida regras de negócio
        ServiceBackend->>Repository: existsByCpfAndAssessorId()
        Repository->>MongoDB: Query
        MongoDB-->>Repository: Resultado
        alt CPF já existe
            Repository-->>ServiceBackend: true
            ServiceBackend-->>Controller: BadRequestException
            Controller->>ExceptionHandler: Captura exceção
            ExceptionHandler-->>Component: 400 Bad Request
        else CPF não existe
            ServiceBackend->>ServiceBackend: Cria entidade Investidor
            ServiceBackend->>Repository: save(investidor)
            Repository->>MongoDB: Insert
            MongoDB-->>Repository: Investidor salvo
            Repository-->>ServiceBackend: Investidor
            ServiceBackend->>ServiceBackend: mapToResponse()
            ServiceBackend-->>Controller: InvestidorResponse
            Controller->>AOP: Intercepta (depois)
            AOP->>AOP: Registra sucesso
            Controller-->>Service: 201 Created + Response
            Service-->>Component: Dados do investidor
            Component-->>User: Exibe mensagem de sucesso
        end
    end
```

### Fluxo: Listar Investidores (GET /investors)

```mermaid
sequenceDiagram
    participant User as 👤 Usuário
    participant Component as Component
    participant Service as Service
    participant Interceptor as AuthInterceptor
    participant JWTFilter as JWT Filter
    participant Controller as InvestidorController
    participant AuthUtil as AuthUtil
    participant ServiceBackend as InvestidorService
    participant Repository as InvestidorRepository
    participant MongoDB as MongoDB
    participant AOP as LoggingAspect

    User->>Component: Acessa página de investidores
    Component->>Service: listarInvestidores()
    Service->>Interceptor: GET /investors + Token
    Interceptor->>JWTFilter: Request com Authorization Header
    JWTFilter->>JWTFilter: Extrai token
    JWTFilter->>JWTFilter: Valida token
    alt Token Inválido
        JWTFilter-->>Service: 401 Unauthorized
    else Token Válido
        JWTFilter->>JWTFilter: Define Authentication no Context
        JWTFilter->>Controller: Request autenticado
        Controller->>AOP: Intercepta (antes)
        Controller->>AuthUtil: getAssessorId(authentication)
        AuthUtil-->>Controller: assessorId
        Controller->>ServiceBackend: listarInvestidores(assessorId)
        ServiceBackend->>Repository: findByAssessorId(assessorId)
        Repository->>MongoDB: Query
        MongoDB-->>Repository: List<Investidor>
        Repository-->>ServiceBackend: List<Investidor>
        ServiceBackend->>ServiceBackend: mapToResponse() para cada
        ServiceBackend-->>Controller: List<InvestidorResponse>
        Controller->>AOP: Intercepta (depois)
        AOP->>AOP: Registra sucesso
        Controller-->>Service: 200 OK + List
        Service-->>Component: Dados dos investidores
        Component-->>User: Renderiza lista
    end
```

---

## 🔐 Fluxo de Autenticação JWT

### Fluxo: Login e Obtenção de Token

```mermaid
sequenceDiagram
    participant User as 👤 Usuário
    participant Component as LoginComponent
    participant Service as AuthService
    participant Controller as AuthController
    participant AuthServiceBackend as AuthService (Backend)
    participant PasswordEncoder as BCryptPasswordEncoder
    participant Repository as AssessorRepository
    participant MongoDB as MongoDB
    participant JwtUtil as JwtUtil
    participant LogService as LogService

    User->>Component: Preenche email e senha
    Component->>Service: login(email, senha)
    Service->>Controller: POST /auth/login
    
    Controller->>AuthServiceBackend: login(request, ip)
    AuthServiceBackend->>Repository: findByEmail(email)
    Repository->>MongoDB: Query
    MongoDB-->>Repository: Assessor ou null
    
    alt Assessor não encontrado
        Repository-->>AuthServiceBackend: null
        AuthServiceBackend-->>Controller: UnauthorizedException
        Controller-->>Service: 401 Unauthorized
    else Assessor encontrado
        AuthServiceBackend->>PasswordEncoder: matches(senha, hash)
        PasswordEncoder-->>AuthServiceBackend: true/false
        
        alt Senha incorreta
            AuthServiceBackend-->>Controller: UnauthorizedException
            Controller-->>Service: 401 Unauthorized
        else Senha correta
            AuthServiceBackend->>JwtUtil: generateToken(email)
            JwtUtil-->>AuthServiceBackend: JWT Token
            AuthServiceBackend->>Repository: updateUltimoLogin()
            AuthServiceBackend->>LogService: registrarLogin()
            AuthServiceBackend-->>Controller: AuthResponse (token + dados)
            Controller-->>Service: 200 OK + AuthResponse
            Service->>Service: Salva token no localStorage
            Service-->>Component: Token e dados do assessor
            Component-->>User: Redireciona para dashboard
        end
    end
```

### Fluxo: Validação de Token em Requisições Protegidas

```mermaid
sequenceDiagram
    participant Client as Cliente (Frontend)
    participant Interceptor as AuthInterceptor
    participant JWTFilter as JWT Authentication Filter
    participant JwtUtil as JwtUtil
    participant UserDetailsService as UserDetailsService
    participant SecurityContext as SecurityContextHolder
    participant Controller as Controller

    Client->>Interceptor: HTTP Request + Token
    Interceptor->>Interceptor: Adiciona Authorization: Bearer {token}
    Interceptor->>JWTFilter: Request com header
    
    JWTFilter->>JWTFilter: Extrai token do header
    JWTFilter->>JwtUtil: extractEmail(token)
    JwtUtil-->>JWTFilter: email
    
    JWTFilter->>JwtUtil: validateToken(token, email)
    JwtUtil->>JwtUtil: Verifica expiração
    JwtUtil->>JwtUtil: Verifica assinatura
    
    alt Token Inválido
        JwtUtil-->>JWTFilter: false
        JWTFilter-->>Client: 401 Unauthorized
    else Token Válido
        JwtUtil-->>JWTFilter: true
        JWTFilter->>UserDetailsService: loadUserByUsername(email)
        UserDetailsService-->>JWTFilter: UserDetails
        JWTFilter->>SecurityContext: setAuthentication()
        SecurityContext-->>JWTFilter: OK
        JWTFilter->>Controller: Request autenticado
        Controller->>Controller: Processa requisição
        Controller-->>Client: Response
    end
```

---

## ⚠️ Fluxo de Tratamento de Erros

### Hierarquia de Tratamento de Exceções

```mermaid
graph TB
    subgraph ExceptionFlow["Fluxo de Tratamento de Exceções"]
        Request[Requisição HTTP] --> Controller[Controller]
        Controller --> Service[Service]
        Service --> Repository[Repository]
        
        Repository -->|Erro DB| DBException[DatabaseException]
        Service -->|Erro Negócio| BusinessException[BadRequestException<br/>ResourceNotFoundException]
        Controller -->|Erro Validação| ValidationException[ValidationException]
        Controller -->|Erro Autenticação| AuthException[UnauthorizedException]
        
        DBException --> ExceptionHandler[GlobalExceptionHandler]
        BusinessException --> ExceptionHandler
        ValidationException --> ExceptionHandler
        AuthException --> ExceptionHandler
        
        ExceptionHandler -->|Mapeia| ErrorResponse[ErrorResponse DTO]
        ErrorResponse -->|Retorna| Client[Cliente HTTP Response]
        
        Client -->|400| BadRequest[Bad Request]
        Client -->|401| Unauthorized[Unauthorized]
        Client -->|404| NotFound[Not Found]
        Client -->|500| ServerError[Internal Server Error]
    end
    
    style Request fill:#e1f5ff
    style ExceptionHandler fill:#ffe1e1
    style ErrorResponse fill:#fff4e1
    style Client fill:#e1ffe1
```

### Fluxo Detalhado: Tratamento de Exceção

```mermaid
sequenceDiagram
    participant Controller as Controller
    participant Service as Service
    participant Repository as Repository
    participant MongoDB as MongoDB
    participant ExceptionHandler as GlobalExceptionHandler
    participant Client as Cliente

    Controller->>Service: método()
    Service->>Repository: operação()
    Repository->>MongoDB: Query
    
    alt Erro no MongoDB
        MongoDB-->>Repository: Exception
        Repository-->>Service: Propaga exceção
        Service-->>Controller: Propaga exceção
    else Erro de Negócio
        Service->>Service: Valida regra
        Service-->>Controller: BadRequestException
    else Erro de Validação
        Controller->>Controller: @Valid falha
        Controller-->>Controller: ValidationException
    end
    
    Controller->>ExceptionHandler: Exceção capturada
    ExceptionHandler->>ExceptionHandler: Identifica tipo de exceção
    ExceptionHandler->>ExceptionHandler: Mapeia para HTTP Status
    
    alt ResourceNotFoundException
        ExceptionHandler->>ExceptionHandler: 404 Not Found
    else BadRequestException
        ExceptionHandler->>ExceptionHandler: 400 Bad Request
    else UnauthorizedException
        ExceptionHandler->>ExceptionHandler: 401 Unauthorized
    else Exception genérica
        ExceptionHandler->>ExceptionHandler: 500 Internal Server Error
    end
    
    ExceptionHandler->>ExceptionHandler: Cria ErrorResponse
    ExceptionHandler-->>Client: HTTP Response + ErrorResponse
```

---

## 📝 Fluxo de Logging Automático (AOP)

### Aspecto de Logging Interceptando Controllers

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as Controller
    participant LoggingAspect as LoggingAspect (AOP)
    participant LogService as LogService
    participant MongoDB as MongoDB

    Client->>Controller: HTTP Request
    
    Note over LoggingAspect: @Around intercepta ANTES
    LoggingAspect->>LoggingAspect: Captura método, parâmetros, IP
    LoggingAspect->>Controller: proceed() - Executa método
    
    Controller->>Controller: Processa requisição
    
    alt Sucesso
        Controller-->>LoggingAspect: Response 200/201
        Note over LoggingAspect: @Around intercepta DEPOIS
        LoggingAspect->>LoggingAspect: Captura response, status code
        LoggingAspect->>LogService: registrarLog(sucesso=true)
        LogService->>MongoDB: Salva log
        LoggingAspect-->>Client: Response
    else Erro
        Controller-->>LoggingAspect: Exception
        Note over LoggingAspect: @Around intercepta DEPOIS
        LoggingAspect->>LoggingAspect: Captura exception, status code
        LoggingAspect->>LogService: registrarLog(sucesso=false)
        LogService->>MongoDB: Salva log
        LoggingAspect-->>Client: Error Response
    end
```

### Estrutura do Logging Aspect

```mermaid
graph TB
    subgraph AOPFlow["Fluxo AOP - Logging"]
        Request[HTTP Request] --> Aspect[LoggingAspect<br/>@Around]
        Aspect -->|Antes| Before[Before Execution<br/>- Captura método<br/>- Captura parâmetros<br/>- Captura IP]
        Before --> Controller[Controller Execution]
        Controller -->|Depois| After[After Execution<br/>- Captura response<br/>- Captura status code<br/>- Captura tempo]
        After --> LogService[LogService]
        LogService --> Log[Log Entity]
        Log --> MongoDB[(MongoDB)]
    end
    
    style Aspect fill:#e1f5ff
    style Before fill:#fff4e1
    style After fill:#ffe1e1
    style LogService fill:#f0e1ff
    style MongoDB fill:#e1ffe1
```

---

## ✅ Fluxo de Validação de Dados

### Validação em Múltiplas Camadas

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as Controller
    participant BeanValidator as Bean Validator
    participant CustomValidator as Custom Validator
    participant Service as Service
    participant BusinessValidator as Business Validator

    Client->>Controller: POST /investors + JSON
    
    Note over Controller,BeanValidator: Camada 1: Validação de Formato
    Controller->>BeanValidator: @Valid InvestidorRequest
    BeanValidator->>BeanValidator: Valida @NotBlank, @Email, @Size
    
    alt Validação Bean falha
        BeanValidator-->>Controller: ValidationException
        Controller-->>Client: 400 Bad Request
    else Validação Bean OK
        BeanValidator-->>Controller: DTO válido
        
        Note over Controller,CustomValidator: Camada 2: Validação Customizada
        Controller->>CustomValidator: @ValidCpf, @ValidCodigoAtivo
        CustomValidator->>CustomValidator: Valida CPF, Código Ativo
        
        alt Validação Custom falha
            CustomValidator-->>Controller: ValidationException
            Controller-->>Client: 400 Bad Request
        else Validação Custom OK
            CustomValidator-->>Controller: DTO validado
            
            Note over Controller,BusinessValidator: Camada 3: Validação de Negócio
            Controller->>Service: criarInvestidor(request)
            Service->>BusinessValidator: Valida regras de negócio
            BusinessValidator->>BusinessValidator: - CPF único por assessor<br/>- Datas válidas<br/>- Valores >= 0
            
            alt Validação Negócio falha
                BusinessValidator-->>Service: BadRequestException
                Service-->>Controller: BadRequestException
                Controller-->>Client: 400 Bad Request
            else Validação Negócio OK
                BusinessValidator-->>Service: OK
                Service->>Service: Processa criação
                Service-->>Controller: InvestidorResponse
                Controller-->>Client: 201 Created
            end
        end
    end
```

---

## 🌐 Comunicação Frontend-Backend

### Arquitetura de Comunicação Completa

```mermaid
graph TB
    subgraph Frontend["🌐 Frontend (Angular)"]
        UI[Componentes UI]
        Services[Services HTTP]
        Interceptor[Auth Interceptor]
        Guards[Route Guards]
    end
    
    subgraph Network["🌍 Network Layer"]
        HTTP[HTTP/HTTPS]
        CORS[CORS Config]
    end
    
    subgraph Backend["⚙️ Backend (Spring Boot)"]
        Security[Security Filter Chain]
        JWTFilter[JWT Filter]
        Controller[Controllers]
        Service[Services]
        Repository[Repositories]
    end
    
    subgraph Database["💾 Database"]
        MongoDB[(MongoDB)]
    end
    
    UI --> Services
    Services --> Interceptor
    Interceptor --> Guards
    Guards --> HTTP
    HTTP --> CORS
    CORS --> Security
    Security --> JWTFilter
    JWTFilter --> Controller
    Controller --> Service
    Service --> Repository
    Repository --> MongoDB
    
    style Frontend fill:#e1f5ff
    style Network fill:#fff4e1
    style Backend fill:#ffe1e1
    style Database fill:#e1ffe1
```

### Fluxo de Dados: Request e Response

```mermaid
sequenceDiagram
    participant Component as Angular Component
    participant Service as Angular Service
    participant Interceptor as Auth Interceptor
    participant Controller as Spring Controller
    participant ServiceBackend as Spring Service
    participant Repository as Repository
    participant MongoDB as MongoDB

    Component->>Service: método() com dados
    Service->>Service: Cria HTTP Request
    Service->>Interceptor: Request + Token
    
    Interceptor->>Interceptor: Adiciona headers
    Interceptor->>Controller: HTTP POST/GET/PUT/DELETE
    
    Controller->>Controller: Valida DTO
    Controller->>ServiceBackend: processa()
    ServiceBackend->>ServiceBackend: Lógica de negócio
    ServiceBackend->>Repository: operação()
    Repository->>MongoDB: Query/Insert/Update/Delete
    MongoDB-->>Repository: Resultado
    Repository-->>ServiceBackend: Entidade
    ServiceBackend->>ServiceBackend: mapToResponse()
    ServiceBackend-->>Controller: Response DTO
    Controller-->>Interceptor: HTTP Response + DTO
    Interceptor-->>Service: Response
    Service->>Service: Processa response
    Service-->>Component: Dados tipados
    Component->>Component: Atualiza UI
```

---

## 🎨 Padrões Arquiteturais Aplicados

### 1. Repository Pattern

```mermaid
graph LR
    Service[Service Layer] -->|Usa Interface| Repository[Repository Interface]
    Repository -->|Implementado por| MongoRepository[Spring Data MongoDB]
    MongoRepository -->|Acessa| MongoDB[(MongoDB)]
    
    style Service fill:#e1f5ff
    style Repository fill:#fff4e1
    style MongoRepository fill:#ffe1e1
    style MongoDB fill:#e1ffe1
```

### 2. DTO Pattern

```mermaid
graph TB
    Client[Cliente HTTP] -->|Request| RequestDTO[Request DTO<br/>- InvestidorRequest<br/>- PortfolioRequest<br/>- AplicacaoRequest]
    RequestDTO --> Controller[Controller]
    Controller --> Service[Service]
    Service --> Model[Model Entity]
    Model --> Repository[Repository]
    Repository --> MongoDB[(MongoDB)]
    MongoDB --> Repository
    Repository --> Model
    Model --> Service
    Service -->|Mapeia| ResponseDTO[Response DTO<br/>- InvestidorResponse<br/>- PortfolioResponse<br/>- AplicacaoResponse]
    ResponseDTO --> Controller
    Controller -->|Response| Client
    
    style RequestDTO fill:#e1f5ff
    style ResponseDTO fill:#fff4e1
    style Model fill:#ffe1e1
    style MongoDB fill:#e1ffe1
```

### 3. Dependency Injection

```mermaid
graph TB
    SpringContainer[Spring IoC Container]
    
    SpringContainer --> Controller[Controller]
    SpringContainer --> Service[Service]
    SpringContainer --> Repository[Repository]
    SpringContainer --> Config[Configuration]
    
    Controller -.->|@Autowired| Service
    Service -.->|@Autowired| Repository
    Controller -.->|@Autowired| AuthUtil
    Service -.->|@Autowired| LogService
    
    style SpringContainer fill:#e1f5ff
    style Controller fill:#fff4e1
    style Service fill:#ffe1e1
    style Repository fill:#f0e1ff
```

### 4. Aspect-Oriented Programming (AOP)

```mermaid
graph TB
    Request[HTTP Request] --> Controller[Controller Method]
    Controller -.->|Interceptado por| Aspect[LoggingAspect<br/>@Around]
    Aspect -->|Antes| BeforeAdvice[Before Advice<br/>Captura início]
    BeforeAdvice --> Controller
    Controller -->|Depois| AfterAdvice[After Advice<br/>Captura fim]
    AfterAdvice --> Aspect
    Aspect --> LogService[LogService]
    
    style Aspect fill:#e1f5ff
    style BeforeAdvice fill:#fff4e1
    style AfterAdvice fill:#ffe1e1
    style LogService fill:#f0e1ff
```

---

## 📊 Resumo da Arquitetura

### Camadas e Responsabilidades

| Camada | Responsabilidade | Tecnologias |
|--------|------------------|-------------|
| **Frontend - UI** | Interface do usuário | Angular, HTML, CSS, TypeScript |
| **Frontend - Services** | Comunicação HTTP | Angular HTTP Client, RxJS |
| **Frontend - Guards** | Proteção de rotas | Angular Guards |
| **Frontend - Interceptors** | Interceptação de requisições | Angular Interceptors |
| **Backend - Controllers** | Endpoints REST | Spring MVC, Swagger |
| **Backend - Services** | Lógica de negócio | Spring Services |
| **Backend - Repositories** | Acesso a dados | Spring Data MongoDB |
| **Backend - Models** | Entidades de domínio | MongoDB Documents |
| **Security - JWT Filter** | Autenticação | Spring Security, JWT |
| **Cross-Cutting - AOP** | Logging automático | Spring AOP |
| **Cross-Cutting - Exception Handler** | Tratamento de erros | Spring @ControllerAdvice |
| **Database** | Persistência | MongoDB |

### Fluxo de Dados Resumido

```mermaid
flowchart LR
    A[Cliente] -->|1. Request| B[Controller]
    B -->|2. Valida| C[DTO]
    C -->|3. Chama| D[Service]
    D -->|4. Valida Negócio| E[Business Rules]
    E -->|5. Usa| F[Repository]
    F -->|6. Query| G[(MongoDB)]
    G -->|7. Retorna| F
    F -->|8. Entidade| D
    D -->|9. Mapeia| C
    C -->|10. Response| B
    B -->|11. HTTP Response| A
    
    style A fill:#e1f5ff
    style B fill:#fff4e1
    style D fill:#ffe1e1
    style F fill:#f0e1ff
    style G fill:#e1ffe1
```

---

## 🔍 Pontos de Interceptação

### Onde Acontecem as Interceptações

```mermaid
graph TB
    Request[HTTP Request] --> Filter1[CORS Filter]
    Filter1 --> Filter2[JWT Authentication Filter]
    Filter2 --> Filter3[Controller Method]
    Filter3 -.->|Interceptado| AOP[LoggingAspect]
    Filter3 --> Service[Service Method]
    Service --> Repository[Repository Method]
    
    AOP --> LogService[LogService]
    
    Filter3 -.->|Erro| ExceptionHandler[GlobalExceptionHandler]
    Service -.->|Erro| ExceptionHandler
    Repository -.->|Erro| ExceptionHandler
    
    ExceptionHandler --> ErrorResponse[Error Response]
    
    style Filter2 fill:#e1f5ff
    style AOP fill:#fff4e1
    style ExceptionHandler fill:#ffe1e1
    style ErrorResponse fill:#f0e1ff
```

---

## ✅ Benefícios da Arquitetura

### Vantagens da Separação em Camadas

1. **Manutenibilidade**: Fácil localizar e corrigir bugs
2. **Testabilidade**: Cada camada pode ser testada isoladamente
3. **Escalabilidade**: Fácil adicionar novas funcionalidades
4. **Reutilização**: Services e Repositories podem ser reutilizados
5. **Segurança**: Validação em múltiplas camadas
6. **Performance**: Cache e otimizações por camada
7. **Documentação**: Código auto-documentado pela estrutura

---

**Versão do Documento**: 1.0.0  
**Última Atualização**: Janeiro 2025  
**Autor**: Equipe BartoFinance

