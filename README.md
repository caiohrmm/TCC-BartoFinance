# BartoFinance - Sistema de Assessoria de Investimentos

Sistema backend completo para assessores financeiros gerenciarem investidores, carteiras de investimento, aplicações financeiras, relatórios e insights com IA.

---

## 📋 Sobre o Projeto

**BartoFinance** é uma plataforma exclusiva para assessores de investimento que centraliza:

- **Gestão de Investidores**: Cadastro completo com perfil de risco e objetivos
- **Carteiras de Investimento**: Criação de carteiras modelo ou personalizadas
- **Aplicações Financeiras**: Controle de ações, LCI, LCA, CDB, fundos e outros ativos
- **Relatórios Detalhados**: Análise de rentabilidade e evolução patrimonial
- **Insights com IA**: Recomendações geradas por IA simulada (mock Gemini)
- **Auditoria Completa**: Logs automáticos de todas operações via AOP

---

## 🔄 Fluxo do Sistema e Funcionalidades

### 1️⃣ **Autenticação e Registro**

#### Cadastro de Assessor
```
POST /auth/register
```
O assessor cria sua conta fornecendo:
- Nome completo
- Email (único no sistema)
- Senha (criptografada com BCrypt)

**Retorna:** Token JWT válido por 24 horas + dados do assessor

#### Login
```
POST /auth/login
```
Autenticação com email e senha.

**Retorna:** Token JWT + ID do assessor

**✅ Recursos de Segurança:**
- Senhas hasheadas com BCrypt
- Tokens JWT com expiração configurável
- Validação automática em todas as rotas protegidas
- Logs de tentativas de acesso

---

### 2️⃣ **Gestão de Investidores**

O assessor gerencia sua base de clientes investidores.

#### Criar Investidor
```
POST /investors
```
**Dados cadastrados:**
- Nome, CPF, email, telefone
- **Perfil de Investidor**: CONSERVADOR | MODERADO | AGRESSIVO
- Patrimônio atual
- Renda mensal
- Objetivos financeiros

**✅ Validações:**
- CPF único **por assessor** (múltiplos assessores podem ter o mesmo CPF)
- Todos os campos obrigatórios validados
- Email em formato válido

#### Listar Investidores
```
GET /investors
GET /investors?perfilInvestidor=CONSERVADOR
```
- Lista todos os investidores do assessor autenticado
- **Filtro opcional** por perfil (CONSERVADOR, MODERADO, AGRESSIVO)

#### Buscar por ID
```
GET /investors/{id}
```
Retorna dados completos de um investidor específico.

**✅ Segurança:** Valida se o investidor pertence ao assessor autenticado.

#### Atualizar Investidor
```
PUT /investors/{id}
```
Permite atualizar todos os dados do investidor, incluindo perfil e objetivos.

**✅ Validações:**
- CPF único entre investidores do mesmo assessor
- Validação de propriedade (ownership)

#### Deletar Investidor
```
DELETE /investors/{id}
```
Remove o investidor do sistema.

**⚠️ Impacto:** Ao deletar um investidor, considere remover também suas carteiras e aplicações.

---

### 3️⃣ **Gestão de Carteiras (Portfolios)**

Criação e gerenciamento de carteiras de investimento.

#### Tipos de Carteira
- **MODELO**: Templates genéricos, sem investidor vinculado (reutilizáveis)
- **PERSONALIZADA**: Vinculada a um investidor específico

#### Criar Carteira
```
POST /portfolios
```
**Dados:**
- Nome (único por assessor)
- Descrição
- **Tipo**: MODELO | PERSONALIZADA
- **Nível de Risco**: BAIXO | MEDIO | ALTO
- Meta de rentabilidade (%)
- `investidorId` (obrigatório se PERSONALIZADA)

**✅ Validações:**
- Nome único por assessor
- Se personalizada, `investidorId` é obrigatório
- Valida se o investidor pertence ao assessor

#### Simular Desempenho
```
POST /portfolios/simulate
```
Simula uma carteira hipotética **sem salvar** no banco.

**Uso:** Testar configurações antes de criar definitivamente.

**Retorna:** Rentabilidade estimada (mock: 80% da meta)

#### Listar Carteiras
```
GET /portfolios              # Todas do assessor
GET /portfolios/models       # Apenas modelos (templates)
GET /portfolios/{id}         # Específica por ID
```

#### Atualizar Carteira
```
PUT /portfolios/{id}
```
Atualiza nome, descrição, risco ou meta de rentabilidade.

**✅ Validação:** Nome único entre carteiras do mesmo assessor.

#### Deletar Carteira
```
DELETE /portfolios/{id}
```
Remove a carteira.

**⚠️ Atenção:** Considere o impacto nas aplicações vinculadas.

---

### 4️⃣ **Gestão de Aplicações Financeiras**

Controle detalhado de cada investimento dentro de uma carteira.

#### Criar Aplicação
```
POST /applications
```
**Dados obrigatórios:**
- `portfolioId` (carteira de destino)
- **Tipo de Produto**: ACAO | FII | LCI | LCA | CDB | TESOURO_DIRETO | FUNDO_INVESTIMENTO
- Código do ativo (ex: PETR4, BBAS3, KNRI11)
- Valor aplicado (R$)
- Quantidade de cotas/ações
- Data de compra
- **Status**: ATIVA | VENDIDA | VENCIDA
- Notas/observações

**Campos opcionais:**
- Data de venda
- Rentabilidade atual (%)

**✅ Validações:**
- Portfolio deve pertencer ao assessor
- Código do ativo único **por portfolio** (não pode duplicar PETR4 no mesmo portfolio)
- Valor e quantidade > 0

#### Listar Aplicações
```
GET /applications                                    # Todas do assessor
GET /applications?portfolioId={id}                  # Por carteira
GET /applications?status=ATIVA                      # Por status
GET /applications?portfolioId={id}&status=ATIVA     # Filtros combinados
```

**Filtros disponíveis:**
- `portfolioId`: Aplicações de uma carteira específica
- `status`: ATIVA | VENDIDA | VENCIDA

#### Buscar por ID
```
GET /applications/{id}
```

#### Atualizar Aplicação
```
PUT /applications/{id}
```
Permite atualizar todos os dados, incluindo rentabilidade e status.

**Caso de uso:** Registrar venda de ativo (atualizar status para VENDIDA e informar data de venda).

**✅ Validação:** Código do ativo único por portfolio.

#### Deletar Aplicação
```
DELETE /applications/{id}
```

---

### 5️⃣ **Insights com IA (Mock Gemini)**

Sistema de recomendações baseado em IA simulada.

#### Gerar Insight
```
POST /insights/generate
{
  "investidorId": "...",
  "tipo": "RECOMENDACAO_PORTFOLIO"
}
```

**Tipos de Insight:**
- `RECOMENDACAO_PORTFOLIO`: Sugestões de alocação
- `ANALISE_RISCO`: Análise de exposição a risco
- `ALERTA_MERCADO`: Alertas sobre cenário econômico
- `OPORTUNIDADE`: Oportunidades de investimento

**Como funciona:**
1. Sistema analisa o perfil do investidor
2. Gera texto personalizado (mock simula IA Gemini)
3. Salva no banco com timestamp

**Exemplo de insight gerado:**
```
"Olá Maria! Com seu perfil MODERADO e patrimônio de R$ 150.000,
recomendamos diversificar 40% em renda fixa (CDB, LCI), 40% em
ações de empresas consolidadas e 20% em fundos imobiliários..."
```

#### Listar Insights
```
GET /insights?investorId={id}
```
Retorna todos os insights gerados para um investidor.

**✅ Segurança:** Valida se o investidor pertence ao assessor.

---

### 6️⃣ **Sistema de Logs e Auditoria**

Logging automático via **Spring AOP** de todas as requisições.

#### Dados Registrados Automaticamente
Cada requisição salva:
- **Usuário**: Email do assessor autenticado
- **Endpoint**: Rota acessada (ex: `/investors`)
- **Método HTTP**: GET, POST, PUT, DELETE
- **Status Code**: 200, 201, 400, 404, 500, etc.
- **IP do Cliente**: Origem da requisição
- **Sucesso**: `true` ou `false`
- **Mensagem**: Descrição da operação
- **Timestamp**: Data/hora exata

#### Consultar Logs
```
GET /logs                                    # Todos os logs
GET /logs?usuario=joao@email.com            # Por usuário
GET /logs?endpoint=/investors               # Por endpoint
GET /logs?metodo=POST                       # Por método HTTP
GET /logs?sucesso=false                     # Apenas erros
GET /logs/erros                             # Apenas falhas
GET /logs/periodo?inicio=...&fim=...        # Por período
```

**Caso de uso:** Auditoria de segurança, análise de erros, debug.

---

### 7️⃣ **Health Checks (Públicos)**

Endpoints para monitoramento da aplicação.

```
GET /health        # Status completo da aplicação
GET /health/ping   # Teste rápido de conectividade
```

**✅ Acesso público:** Não requer autenticação (ideal para monitoramento externo).

**Retorna:**
```json
{
  "status": "UP",
  "application": "BartoFinance Backend",
  "version": "1.0.0",
  "timestamp": "2025-01-21T15:30:00"
}
```

---

## 🎯 Fluxo Típico de Uso

### Cenário: Assessor cadastrando novo cliente

```
1. Login do Assessor
   POST /auth/login → Recebe token JWT

2. Cadastrar Investidor
   POST /investors
   {
     "nome": "Maria Santos",
     "cpf": "12345678900",
     "email": "maria@email.com",
     "perfilInvestidor": "MODERADO",
     "patrimonioAtual": 150000,
     "rendaMensal": 8500,
     "objetivos": "Aposentadoria e viagens"
   }
   → Salva investidorId retornado

3. Criar Carteira Personalizada
   POST /portfolios
   {
     "nome": "Carteira Maria 2025",
     "tipo": "PERSONALIZADA",
     "risco": "MEDIO",
     "metaRentabilidade": 15.5,
     "investidorId": "{investidorId}"
   }
   → Salva portfolioId retornado

4. Adicionar Aplicações
   POST /applications (PETR4 - Ações)
   POST /applications (KNRI11 - FII)
   POST /applications (CDB - Renda Fixa)
   → Todas vinculadas ao portfolioId

5. Gerar Insights
   POST /insights/generate
   {
     "investidorId": "{investidorId}",
     "tipo": "RECOMENDACAO_PORTFOLIO"
   }
   → IA analisa perfil e gera recomendações

6. Consultar Logs
   GET /logs?usuario=assessor@email.com
   → Audita todas ações realizadas
```

---

## 🔐 Segurança e Isolamento Multi-Assessor

### Validações Implementadas

#### ✅ Isolamento por Assessor
Cada assessor **só acessa seus próprios dados**:
- Investidores
- Carteiras
- Aplicações
- Insights

#### ✅ Unicidade Correta
- **CPF**: Único **por assessor** (assessores diferentes podem ter o mesmo CPF)
- **Nome de Carteira**: Único **por assessor**
- **Código de Ativo**: Único **por portfolio**
- **Email de Assessor**: Único **globalmente** (é o login)

#### ✅ Validações de Propriedade
Todas as operações validam:
```java
if (!recurso.getAssessorId().equals(assessorIdAutenticado)) {
    throw new BadRequestException("Acesso negado");
}
```

#### ✅ Operações Seguras
- GET: Retorna apenas recursos do assessor autenticado
- POST: Vincula automaticamente ao assessor autenticado
- PUT/DELETE: Valida propriedade antes de executar

### Exemplo Prático

**Cenário:**
- Assessor A cadastra CPF 123.456.789-00 (investidor João)
- Assessor B cadastra CPF 123.456.789-00 (investidor Maria)

**Resultado:** ✅ **PERMITIDO**
- São pessoas diferentes
- Cada assessor tem seu próprio João/Maria
- Não há conflito ou vazamento de dados

---

## 🚀 Tecnologias

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3.2.0** - Framework web
- **Spring Security** - Autenticação e autorização
- **Spring Data MongoDB** - Persistência de dados
- **JWT (jjwt 0.12.3)** - Tokens de autenticação
- **Spring AOP** - Logs automáticos
- **Lombok** - Redução de boilerplate
- **Swagger/OpenAPI** - Documentação da API

### Banco de Dados
- **MongoDB 7** - NoSQL Database

### Build & Deploy
- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Containerização (opcional)

---

## 🏗️ Arquitetura

```
com.bartofinance/
├── aspect/           # Logging AOP
├── config/           # Configurações (Security, Swagger)
├── controller/       # REST Controllers
├── dto/              # Data Transfer Objects
│   ├── request/      # DTOs de entrada
│   └── response/     # DTOs de saída
├── exception/        # Exceções customizadas
├── handler/          # GlobalExceptionHandler
├── model/            # Entidades do domínio
│   └── enums/        # Enumerações
├── repository/       # Interfaces MongoDB
├── security/         # JWT Filter, JwtUtil
├── service/          # Lógica de negócio
└── util/             # Utilitários (AuthUtil)
```

**Padrões Aplicados:**
- Clean Architecture
- Repository Pattern
- DTO Pattern
- AOP para cross-cutting concerns
- JWT Stateless Authentication

---

## 📊 Modelos de Dados

### Assessor
Profissional que usa o sistema para gerenciar investidores.
- `id`, `nome`, `email`, `senha` (BCrypt)
- `dataCadastro`, `ultimoLogin`, `ativo`

### Investidor
Cliente do assessor com dados de perfil e investimentos.
- `id`, `nome`, `cpf` (único), `email`, `telefone`
- `perfilInvestidor` (CONSERVADOR, MODERADO, ARROJADO)
- `patrimonioAtual`, `rendaMensal`, `objetivos`
- `assessorId` (referência ao assessor)
- `createdAt`, `updatedAt`

### InvestmentPortfolio (Carteira de Investimento)
Agrupa aplicações financeiras.
- `id`, `nome`, `descricao`
- `tipo` (MODELO, PERSONALIZADA)
- `risco` (BAIXO, MODERADO, ALTO)
- `metaRentabilidade` (percentual)
- `investidorId` (opcional)
- `createdAt`, `updatedAt`

### Aplicacao (Aplicação Financeira)
Ativo individual dentro de uma carteira.
- `id`, `portfolioId`, `tipoProduto`, `codigoAtivo`
- `valorAplicado`, `quantidade`
- `dataCompra`, `dataVenda` (opcional)
- `rentabilidadeAtual`, `status` (ATIVA, VENDIDA, EXPIRADA)
- `notas`, `createdAt`, `updatedAt`

### Relatorio
Relatórios gerados sobre investidores ou carteiras.
- `id`, `tipo` (INVESTIDOR, CARTEIRA)
- `referenciaId` (ID do investidor ou carteira)
- `dadosResumo` (Map com estatísticas)
- `totalAplicado`, `rendimento`
- `criadoPor` (assessorId), `createdAt`

### Insight
Recomendações geradas por IA simulada.
- `id`, `investidorId`, `texto`
- `geradoPor` (ex: "Gemini AI Mock")
- `tipo` (RECOMENDACAO, ALERTA, OPORTUNIDADE)
- `dataGeracao`

### Log
Auditoria automática de todas requisições.
- `id`, `usuario`, `endpoint`, `metodo` (GET/POST/PUT/DELETE)
- `ip`, `sucesso` (true/false), `mensagem`
- `timestamp`

---

## 🔐 Segurança

### Autenticação JWT
1. **Register/Login** → Retorna token JWT
2. **Token** válido por 24h
3. **Header**: `Authorization: Bearer <token>`
4. **Endpoints públicos**: `/auth/**`, `/swagger-ui/**`, `/api-docs/**`
5. **Endpoints protegidos**: Todos os outros (requerem token)

### CORS
- Configurado para aceitar **todas origens** (desenvolvimento)
- Em produção, restringir para domínios específicos

### Senhas
- Criptografadas com **BCrypt**
- Nunca retornadas em responses

---

## 📡 Endpoints da API

### Autenticação (`/auth`)
- `POST /auth/register` - Cadastrar assessor
- `POST /auth/login` - Login e obtenção de token

### Investidores (`/investors`)
- `POST /investors` - Criar investidor
- `GET /investors` - Listar todos
- `GET /investors?perfilInvestidor=CONSERVADOR` - **✨ NOVO:** Filtrar por perfil
- `GET /investors/{id}` - Buscar por ID
- `PUT /investors/{id}` - Atualizar investidor
- `DELETE /investors/{id}` - Excluir investidor

### Carteiras (`/portfolios`)
- `POST /portfolios` - Criar carteira
- `POST /portfolios/simulate` - **✨ NOVO:** Simular desempenho (não salva)
- `GET /portfolios` - Listar todas
- `GET /portfolios/models` - Listar carteiras modelo
- `GET /portfolios/{id}` - Buscar por ID
- `PUT /portfolios/{id}` - Atualizar carteira
- `DELETE /portfolios/{id}` - Excluir carteira

### Aplicações (`/applications`) - **✨ NOVO MÓDULO**
- `POST /applications` - Registrar aplicação financeira
- `GET /applications` - Listar todas
- `GET /applications?portfolioId={id}` - Filtrar por carteira
- `GET /applications?status=ATIVA` - Filtrar por status
- `GET /applications?portfolioId={id}&status=ATIVA` - Filtros combinados
- `GET /applications/{id}` - Buscar por ID
- `PUT /applications/{id}` - Atualizar aplicação
- `DELETE /applications/{id}` - Excluir aplicação

### Insights (`/insights`)
- `POST /insights/generate` - Gerar insight com IA (mock Gemini)
- `GET /insights` - Listar todos insights
- `GET /insights?investorId={id}` - Filtrar por investidor

### Logs (`/logs`) - **✨ MELHORADO:** Agora com statusCode
- `GET /logs` - Listar todos logs
- `GET /logs?usuario={email}` - Filtrar por usuário
- `GET /logs?endpoint={path}` - Filtrar por endpoint
- `GET /logs?metodo=POST` - Filtrar por método HTTP
- `GET /logs?sucesso=false` - Filtrar por sucesso/erro
- `GET /logs/erros` - Listar apenas erros
- `GET /logs/periodo?inicio={data}&fim={data}` - Filtrar por período

### Health Checks (`/health`) - **✨ AGORA PÚBLICO**
- `GET /health` - Status da aplicação (sem autenticação)
- `GET /health/ping` - Teste de conectividade (sem autenticação)

---

## 🔍 Sistema de Logs (AOP)

### Logging Automático
Todas as requisições REST são interceptadas e logadas automaticamente via **Spring AOP**.

**Aspectos capturados:**
- Endpoint acessado
- Método HTTP (GET, POST, PUT, DELETE)
- **✨ Status Code HTTP** (200, 201, 400, 404, 500, etc.)
- Usuário autenticado (email do assessor)
- IP do cliente
- Sucesso ou falha da operação
- Mensagem descritiva
- Timestamp

**Implementação:**
```java
@Aspect
@Component
public class LoggingAspect {
    // Intercepta todos métodos de controllers
    @Around("execution(* com.bartofinance.controller..*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) {
        // Registra log antes e depois da requisição
    }
}
```

**Consultas disponíveis:**
- Logs por usuário
- Logs por endpoint
- Logs por período
- Logs de falhas (sucesso=false)

---

## ⚙️ Configuração

### MongoDB
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/bartofinance
```

**Opções:**
- **Local**: `mongodb://localhost:27017/bartofinance`
- **Docker**: Usar `docker-compose.yml` fornecido
- **Cloud**: MongoDB Atlas (`mongodb+srv://...`)

### JWT
```yaml
jwt:
  secret: ${JWT_SECRET:defaultSecretKey...}
  expiration: 86400000  # 24 horas
```

**Variável de ambiente (produção):**
```bash
export JWT_SECRET=SuaChaveSecretaSuperSegura256Bits
```

### Server
```yaml
server:
  port: 8080
  address: 0.0.0.0  # Aceita conexões externas
```

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+**
- **Maven 3.8+**
- **MongoDB** (local ou Docker)

### Passo 1: Iniciar MongoDB

**Opção A: MongoDB Local**
```bash
# Windows (Serviço)
Start-Service MongoDB

# Linux/Mac
sudo systemctl start mongod
```

**Opção B: Docker**
```bash
docker-compose up -d
```

**Opção C: MongoDB Atlas**
- Criar cluster gratuito em https://mongodb.com/atlas
- Copiar connection string
- Atualizar `application.yml`

### Passo 2: Executar Aplicação

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

### Passo 3: Acessar

**Swagger UI (Documentação Interativa):**
```
http://localhost:8080/swagger-ui.html
```

**API Docs (JSON):**
```
http://localhost:8080/api-docs
```

**Health Check:**
```
http://localhost:8080/health
```

---

## 🌐 Acesso Externo (Rede Local / Hamachi)

### Configurações Aplicadas
- **Server bind**: `0.0.0.0` (aceita qualquer IP)
- **CORS**: Habilitado para todas origens

### Acessar de Outro Dispositivo

1. **Descobrir IP do servidor:**
```bash
ipconfig  # Windows
ifconfig  # Linux/Mac
```

2. **Acessar do outro dispositivo:**
```
http://SEU_IP:8080/swagger-ui.html
```

Exemplo: `http://192.168.1.100:8080/swagger-ui.html`

### Firewall (Windows)
Se necessário, liberar porta 8080:
```powershell
# PowerShell como Administrador
New-NetFirewallRule -DisplayName "BartoFinance API" -Direction Inbound -LocalPort 8080 -Protocol TCP -Action Allow
```

---

## 🧪 Testes

### Executar Testes
```bash
mvn test
```

### Estrutura de Testes
```
src/test/java/com/bartofinance/
├── service/
│   ├── InvestidorServiceTest.java
│   └── InsightServiceTest.java
└── controller/
    └── InvestidorControllerIntegrationTest.java
```

**Tipos de testes:**
- Unit Tests (Service layer)
- Integration Tests (Controller + Service)
- Repository Tests

---

## 🧪 Testando a API com Postman

### Importar Collection Automática

1. **Abra o Postman**
2. **Importe os arquivos:**
   - `BartoFinance.postman_collection.json` (collection completa)
   - `BartoFinance.postman_environment.json` (variáveis de ambiente)

3. **Selecione o environment** "BartoFinance - Local" no canto superior direito

### ✨ Recursos Automáticos

A collection foi projetada para **capturar automaticamente** tokens e IDs:

#### 🔐 Token JWT Automático
```javascript
// Após fazer Register ou Login:
✅ Token salvo automaticamente
✅ Usado em todas as requisições protegidas
```

#### 🎯 IDs Capturados Automaticamente
Ao criar recursos, os IDs são salvos e reutilizados:
- **investidorId** → Capturado ao criar investidor
- **portfolioId** → Capturado ao criar carteira
- **aplicacaoId** → Capturado ao criar aplicação
- **insightId** → Capturado ao gerar insight

#### 📋 Como Usar (Passo a Passo)

1. **Autenticação** (execute primeiro)
   - `POST /auth/register` → Cria conta e captura token
   - OU `POST /auth/login` → Faz login e captura token

2. **Criar Investidor**
   - `POST /investors` → Cria investidor e salva `investidorId`

3. **Criar Carteira**
   - `POST /portfolios` → Usa `investidorId` automaticamente, salva `portfolioId`

4. **Criar Aplicação**
   - `POST /applications` → Usa `portfolioId` automaticamente, salva `aplicacaoId`

5. **Consultar, Atualizar, Deletar**
   - Todas as requisições `GET`, `PUT`, `DELETE` usam os IDs salvos

### 🔍 Console de Debug

O Postman Console mostra os IDs capturados:
```
✅ Token salvo: eyJhbGciOiJIUzI1NiIsIn...
✅ Investidor ID salvo: 67f8a...
✅ Portfolio ID salvo: 67f8b...
✅ Aplicação ID salvo: 67f8c...
```

### 🌐 Testar de Outro Dispositivo

Para testar de outro computador na mesma rede:

1. **Configure o base_url no environment:**
   ```
   http://SEU_IP_HAMACHI:8080
   ```

2. **Health Checks públicos (sem autenticação):**
   - `GET /health` → Status da aplicação
   - `GET /health/ping` → Teste de conectividade

---

## 📦 Build para Produção

```bash
# Gerar JAR
mvn clean package -DskipTests

# JAR gerado em:
target/bartofinance-backend-0.0.1-SNAPSHOT.jar

# Executar JAR
java -jar target/bartofinance-backend-0.0.1-SNAPSHOT.jar
```

### Variáveis de Ambiente (Produção)
```bash
export JWT_SECRET=SuaChaveSecreta256BitsMinimo
export SPRING_DATA_MONGODB_URI=mongodb://usuario:senha@host:27017/bartofinance
export SERVER_PORT=8080
```

---

## 🔧 Principais Recursos Técnicos

### 1. Validação Automática
Todas as entradas são validadas com Bean Validation:
```java
@NotBlank(message = "Nome é obrigatório")
@Email(message = "Email inválido")
@CPF(message = "CPF inválido")
```

### 2. Tratamento Global de Erros
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Captura todas exceções
    // Retorna responses padronizadas
}
```

### 3. DTOs para Request/Response
Entrada e saída controladas, sem exposição de entidades internas.

### 4. Utilidades
- **AuthUtil**: Extrai usuário autenticado do contexto Spring Security
- **LogService**: Centraliza gravação de logs

### 5. Relacionamentos
```
Assessor → [1:N] → Investidor → [1:N] → Portfolio → [1:N] → Aplicacao
                                      ↓
                                  Relatorio, Insight
```

---

## 📈 Próximos Passos (Roadmap)

### Backend
- [ ] Implementar RelatórioService completo
- [ ] Adicionar exportação de relatórios (PDF, CSV, Excel)
- [ ] Integração real com Gemini AI
- [ ] Websockets para notificações em tempo real
- [ ] Métricas e dashboards com Spring Actuator

### Frontend
- [ ] Desenvolver dashboard em React
- [ ] Gráficos de rentabilidade (Chart.js / Recharts)
- [ ] Chatbot fixo com IA Gemini
- [ ] Filtros avançados e busca
- [ ] Interface responsiva (mobile-first)

### Infraestrutura
- [ ] CI/CD com GitHub Actions
- [ ] Deploy em nuvem (AWS / GCP / Azure)
- [ ] HTTPS com SSL/TLS
- [ ] Backup automático do MongoDB
- [ ] Monitoramento com Prometheus + Grafana

---

## 🛠️ Troubleshooting

### Erro: "MongoDB connection refused"
**Causa:** MongoDB não está rodando.

**Solução:**
```bash
# Verificar status
docker ps                     # Se usando Docker
Get-Service MongoDB           # Windows
sudo systemctl status mongod  # Linux

# Iniciar
docker-compose up -d          # Docker
Start-Service MongoDB         # Windows
sudo systemctl start mongod   # Linux
```

### Erro: "Port 8080 already in use"
**Causa:** Outra aplicação usando a porta.

**Solução:**
```bash
# Encontrar processo
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac

# Matar processo
taskkill /PID <PID> /F        # Windows
kill -9 <PID>                 # Linux/Mac
```

### Erro: "JWT token expired"
**Causa:** Token com mais de 24h.

**Solução:** Fazer login novamente para obter novo token.

### Erro: "Access Denied" ao iniciar MongoDB
**Causa:** Falta de permissões.

**Solução:** Executar PowerShell como Administrador.

---

## 📄 Estrutura de Resposta Padrão

Todas as respostas seguem o formato:

```json
{
  "timestamp": "2024-10-20T21:30:00",
  "status": 200,
  "message": "Operação realizada com sucesso",
  "data": { ... },
  "path": "/investors"
}
```

**Erros:**
```json
{
  "timestamp": "2024-10-20T21:30:00",
  "status": 400,
  "message": "Email já cadastrado no sistema",
  "data": null,
  "path": "/auth/register"
}
```

---

## 👥 Fluxo de Uso

1. **Assessor se registra** → `POST /auth/register`
2. **Faz login** → `POST /auth/login` (recebe token JWT)
3. **Cadastra investidores** → `POST /investors`
4. **Cria carteiras** → `POST /portfolios`
5. **Adiciona aplicações** → `POST /applications`
6. **Gera insights** → `POST /insights/generate`
7. **Consulta relatórios** → `GET /reports/investor/{id}`
8. **Audita ações** → `GET /logs`

---

## 📞 Comandos Úteis

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Testes
mvn test

# Build
mvn clean package

# Docker MongoDB
docker-compose up -d
docker-compose down
docker-compose logs -f mongo

# Verificar MongoDB
docker exec -it tcc-bartofinance-mongo-1 mongosh
> show dbs
> use bartofinance
> show collections
> db.investidores.find()

# Descobrir IP
ipconfig           # Windows
ifconfig           # Linux/Mac
```

---

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

**Swagger UI (Interface Visual):**
```
http://localhost:8080/swagger-ui.html
```

**OpenAPI Spec (JSON):**
```
http://localhost:8080/api-docs
```

---

## 🎓 Observações para TCC

### Pontos Fortes do Projeto:
1. **Arquitetura Limpa** - Separação de responsabilidades
2. **Segurança Robusta** - JWT + BCrypt + CORS configurado
3. **Auditoria Completa** - Logs automáticos via AOP
4. **Validações** - Bean Validation em todas entradas
5. **Documentação** - Swagger/OpenAPI integrado
6. **Testes** - Cobertura de services e controllers
7. **Escalabilidade** - NoSQL (MongoDB) para crescimento
8. **Padrões** - DTOs, Repository Pattern, Exception Handling

### Diferenciais:
- Sistema de logs automático com AOP
- Insights com IA (preparado para integração real)
- Suporte a múltiplos assessores simultâneos
- Relacionamentos complexos (Assessor → Investidor → Carteira → Aplicação)
- Pronto para frontend React

---

## ✅ Status do Projeto

**Backend:** ✅ **COMPLETO E FUNCIONAL**

### Módulos Implementados:
- ✅ Autenticação JWT
- ✅ CRUD Investidores
- ✅ CRUD Carteiras de Investimento
- ✅ CRUD Aplicações Financeiras
- ✅ Geração de Insights (Mock IA)
- ✅ Sistema de Logs (AOP)
- ✅ Tratamento Global de Erros
- ✅ Documentação Swagger
- ✅ Testes Unitários e Integração
- ✅ Configuração para Acesso Externo

### Pendências:
- ⏳ RelatórioService completo (estrutura criada)
- ⏳ Exportação de relatórios (PDF/CSV/Excel)
- ⏳ Integração real com Gemini AI
- ⏳ Frontend React

---

<p align="center">
  <strong>🎉 BartoFinance Backend - Pronto para Uso 🎉</strong>
</p>

<p align="center">
  Desenvolvido com ❤️ para TCC
</p>

---

**Versão:** 1.0.0  
**Última Atualização:** Outubro 2024  
**Java:** 17+  
**Spring Boot:** 3.2.0  
**MongoDB:** 7.0
