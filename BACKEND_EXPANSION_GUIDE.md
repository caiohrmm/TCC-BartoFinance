# 🚀 BartoFinance - Guia de Expansão do Backend

## ✅ Status Atual

**Completado até agora:**
- ✅ Modelos atualizados (Investidor, Portfolio, Aplicacao, Relatorio, Insight, Log)
- ✅ Enums (TipoCarteira, RiscoCarteira, TipoRelatorio)
- ✅ Repositories atualizados para todos os módulos
- ✅ DTOs de Request e Response
- ✅ InvestidorService implementado

---

## 📋 Próximos Passos para Completar

### 1. Services Restantes

Criar os seguintes services seguindo o padrão do `InvestidorService`:

#### **PortfolioService.java**
```java
// Métodos:
- criarPortfolio(PortfolioRequest, assessorId)
- listarPortfolios(assessorId, filtros)
- buscarPorId(id, assessorId)
- atualizarPortfolio(id, request, assessorId)
- deletarPortfolio(id, assessorId)
- listarPortfoliosModelo()
- calcularRentabilidade(portfolioId)
```

#### **AplicacaoService.java**
```java
// Métodos:
- criarAplicacao(AplicacaoRequest, assessorId)
- listarAplicacoesPorPortfolio(portfolioId, assessorId)
- buscarPorId(id, assessorId)
- atualizarAplicacao(id, request, assessorId)
- deletarAplicacao(id, assessorId)
- calcularRentabilidadeTotal(portfolioId)
```

#### **RelatorioService.java**
```java
// Métodos:
- gerarRelatorioInvestidor(investidorId, assessorId)
- gerarRelatorioPortfolio(portfolioId, assessorId)
- buscarRelatorios(assessorId, tipo)
- exportarRelatorio(id, formato) // PDF, CSV, XLSX
```

#### **InsightService.java**
```java
// Métodos:
- gerarInsight(investidorId, assessorId) // Mock Gemini
- listarInsights(investidorId, assessorId)
- gerarInsightsPorPerfil(perfil)
```

---

### 2. Sistema de Logging com AOP

Criar `LoggingAspect.java`:

```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Autowired
    private LogRepository logRepository;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        String usuario = getUsuarioAtual();
        
        try {
            Object result = joinPoint.proceed();
            
            // Log de sucesso
            Log log = Log.builder()
                    .usuario(usuario)
                    .endpoint(request.getRequestURI())
                    .metodo(request.getMethod())
                    .ip(getClientIp(request))
                    .sucesso(true)
                    .mensagem("Requisição executada com sucesso")
                    .build();
            
            logRepository.save(log);
            
            return result;
        } catch (Exception e) {
            // Log de erro
            Log log = Log.builder()
                    .usuario(usuario)
                    .endpoint(request.getRequestURI())
                    .metodo(request.getMethod())
                    .ip(getClientIp(request))
                    .sucesso(false)
                    .mensagem(e.getMessage())
                    .build();
            
            logRepository.save(log);
            throw e;
        }
    }
}
```

---

### 3. Controllers

Criar controllers seguindo o padrão do `AuthController`:

#### **InvestidorController.java**
```java
@RestController
@RequestMapping("/investors")
@Tag(name = "Investidores", description = "Gerenciamento de investidores")
public class InvestidorController {
    
    @PostMapping
    @Operation(summary = "Criar novo investidor")
    public ResponseEntity<ApiResponse<InvestidorResponse>> criar(
            @Valid @RequestBody InvestidorRequest request,
            Authentication auth) {
        // Extrai assessorId do token
        // Chama service.criarInvestidor()
        // Retorna ApiResponse.success()
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvestidorResponse>>> listar(Auth auth) {}
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestidorResponse>> buscar(@PathVariable String id, Auth auth) {}
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestidorResponse>> atualizar(...) {}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable String id, Auth auth) {}
}
```

#### **PortfolioController.java**
- Endpoints: POST, GET (list), GET /{id}, PUT /{id}, DELETE /{id}
- Adicionar: GET /models (listar carteiras modelo)

#### **AplicacaoController.java**
- Endpoints: POST, GET (por portfolio), GET /{id}, PUT /{id}, DELETE /{id}

#### **RelatorioController.java**
- GET /investor/{id}
- GET /portfolio/{id}
- GET /export?id={id}&format={pdf|csv}

#### **InsightController.java**
- POST /generate (gera insight para investidor)
- GET ?investorId={id}

#### **LogController.java**
- GET / (listar logs com filtros)
- GET /failures (logs com sucesso=false)

---

### 4. Utilitário para Extrair Assessor do Token

Criar `AuthUtil.java`:

```java
@Component
public class AuthUtil {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public String getAssessorIdFromAuth(Authentication auth) {
        String email = auth.getName();
        // Buscar assessor por email e retornar ID
        // Ou extrair diretamente do token
    }
}
```

---

### 5. Testes Rápidos

Após implementar tudo, testar endpoints:

```bash
# 1. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","senha":"123456"}'

# 2. Criar Investidor (com token)
curl -X POST http://localhost:8080/investors \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "telefone": "11999999999",
    "perfilInvestidor": "MODERADO",
    "patrimonioAtual": 50000,
    "rendaMensal": 5000,
    "objetivos": "Aposentadoria"
  }'

# 3. Listar Investidores
curl -X GET http://localhost:8080/investors \
  -H "Authorization: Bearer {TOKEN}"
```

---

## 📁 Estrutura Final Esperada

```
src/main/java/com/bartofinance/
├── config/
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java ✅
│   ├── InvestidorController.java ⚠️
│   ├── PortfolioController.java ⚠️
│   ├── AplicacaoController.java ⚠️
│   ├── RelatorioController.java ⚠️
│   ├── InsightController.java ⚠️
│   ├── LogController.java ⚠️
│   └── HealthController.java ✅
├── dto/
│   ├── request/ ✅
│   └── response/ ✅
├── exception/
│   ├── BadRequestException.java ✅
│   ├── GlobalExceptionHandler.java ✅
│   ├── ResourceNotFoundException.java ✅
│   └── UnauthorizedException.java ✅
├── model/ ✅
├── repository/ ✅
├── security/ ✅
├── service/
│   ├── AuthService.java ✅
│   ├── LogService.java ✅
│   ├── InvestidorService.java ✅
│   ├── PortfolioService.java ⚠️
│   ├── AplicacaoService.java ⚠️
│   ├── RelatorioService.java ⚠️
│   └── InsightService.java ⚠️
└── aspect/
    └── LoggingAspect.java ⚠️
```

**Legenda:**
- ✅ Completado
- ⚠️ A implementar

---

## 🎯 Prioridade de Implementação

1. **Alta Prioridade:**
   - PortfolioService + Controller
   - AplicacaoService + Controller
   - LoggingAspect (AOP)

2. **Média Prioridade:**
   - RelatorioService + Controller
   - InsightService + Controller

3. **Baixa Prioridade:**
   - LogController
   - Endpoints de exportação (PDF/CSV)

---

## 💡 Dicas de Implementação

### Para extrair assessorId do token:
```java
@GetMapping
public ResponseEntity<ApiResponse<List<InvestidorResponse>>> listar(Authentication auth) {
    String email = auth.getName();
    Assessor assessor = assessorRepository.findByEmail(email)
        .orElseThrow(() -> new UnauthorizedException("Assessor não encontrado"));
    
    List<InvestidorResponse> investidores = investidorService.listarInvestidores(assessor.getId());
    return ResponseEntity.ok(ApiResponse.success("Investidores listados", investidores));
}
```

### Para validações customizadas:
```java
// Validar se portfolio pertence ao assessor
private void validarPropriedade(String portfolioId, String assessorId) {
    InvestmentPortfolio portfolio = portfolioRepository.findById(portfolioId)
        .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));
    
    if (!portfolio.getAssessorId().equals(assessorId)) {
        throw new BadRequestException("Portfolio não pertence a este assessor");
    }
}
```

---

## 📊 Mock de Insights (Gemini)

Para o InsightService, criar insights baseados no perfil:

```java
private String gerarInsightMock(Investidor investidor) {
    switch (investidor.getPerfilInvestidor()) {
        case CONSERVADOR:
            return "Seu perfil conservador indica preferência por investimentos de baixo risco. " +
                   "Recomenda-se manter 70% em renda fixa (Tesouro Direto, CDBs) e 30% em fundos conservadores.";
        case MODERADO:
            return "Com perfil moderado, você pode diversificar entre renda fixa (50%) e renda variável (50%). " +
                   "Considere fundos multimercado e algumas ações de empresas consolidadas.";
        case AGRESSIVO:
            return "Seu perfil agressivo permite maior exposição a ações (70%) e investimentos alternativos (20%), " +
                   "mantendo 10% em reserva de emergência. Aproveite oportunidades de mercado.";
        default:
            return "Perfil não identificado. Recomenda-se análise detalhada antes de investir.";
    }
}
```

---

## 🔥 Comando para Compilar e Testar

```bash
# 1. Recompilar
mvn clean install -DskipTests

# 2. Executar
mvn spring-boot:run

# 3. Testar Swagger
http://localhost:8080/swagger-ui.html
```

---

<p align="center">
  <strong>Continue implementando os services e controllers restantes seguindo este guia!</strong>
</p>

