# 🏦 BartoFinance - Sistema de Assessoria de Investimentos

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Sobre o Projeto

O **BartoFinance** é um sistema completo de assessoria de investimentos, projetado para centralizar e gerenciar informações de investidores, aplicações, relatórios e insights gerados por IA. O sistema é voltado para uso exclusivo de assessores financeiros, com autenticação JWT e suporte a múltiplos assessores em contas distintas.

### 🎉 NOVIDADES - Backend Expandido! (Out/2025)

- ⭐ **Logging Automático com AOP** - Todos os requests são logados automaticamente sem código extra
- 🏦 **Gestão de Portfolios** - Carteiras modelo e personalizadas com classificação de risco
- 🤖 **Insights Personalizados** - Mock Gemini gerando recomendações por perfil de risco
- 🔧 **AuthUtil** - Extração simplificada de dados do token JWT
- 📊 **18+ Endpoints** REST funcionais e documentados
- ✅ **Validações Completas** - Bean Validation em todos os inputs
- 📚 **Documentação Expandida** - 9 novos arquivos de guias e tutoriais

### 🎯 Características Principais

- ✅ **Autenticação JWT** - Segurança robusta com tokens JWT
- 📊 **Gestão de Investidores** - CRUD completo de investidores e seus perfis
- 🏦 **Gestão de Portfolios** - Carteiras modelo (templates) e personalizadas
- 💰 **Controle de Aplicações** - Gerenciamento de investimentos em diversos produtos financeiros
- 📈 **Relatórios Detalhados** - Geração de relatórios com análise de rentabilidade
- 🤖 **Insights com IA** - Mock Gemini para análises inteligentes (pronto para API real)
- 📝 **Logging AOP** - Auditoria automática de todas as requisições
- 🔒 **Segurança Multi-Camada** - BCrypt + JWT + validação de propriedade

---

## 🏗️ Arquitetura

### Stack Tecnológica

**Backend:**
- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.2.0
- **Banco de Dados:** MongoDB
- **Autenticação:** JWT (JSON Web Tokens)
- **Segurança:** Spring Security + BCrypt
- **Logging:** SLF4J + Spring AOP
- **Documentação:** Swagger/OpenAPI 3.0
- **Build:** Maven

### Estrutura do Projeto

```
bartofinance-backend/
├── src/
│   ├── main/
│   │   ├── java/com/bartofinance/
│   │   │   ├── config/           # Configurações (Security, CORS, etc)
│   │   │   ├── controller/       # Controllers REST
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── request/      # DTOs de requisição
│   │   │   │   └── response/     # DTOs de resposta
│   │   │   ├── exception/        # Exceções customizadas
│   │   │   ├── model/            # Entidades do domínio
│   │   │   │   └── enums/        # Enumerações
│   │   │   ├── repository/       # Repositories MongoDB
│   │   │   ├── security/         # Configurações JWT e Segurança
│   │   │   └── service/          # Lógica de negócio
│   │   └── resources/
│   │       └── application.yml   # Configurações da aplicação
│   └── test/                     # Testes unitários e integração
├── pom.xml                       # Dependências Maven
└── README.md                     # Este arquivo
```

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8+**
- **MongoDB** (local ou Atlas)
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/seu-usuario/bartofinance.git
cd bartofinance
```

### 2️⃣ Configure o MongoDB

**Opção A: MongoDB Local**

```yaml
# src/main/resources/application.yml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/bartofinance
```

**Opção B: MongoDB Atlas (Cloud)**

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<username>:<password>@cluster.mongodb.net/bartofinance?retryWrites=true&w=majority
```

### 3️⃣ Configure as Variáveis de Ambiente (Opcional)

```bash
export JWT_SECRET=sua_chave_secreta_muito_segura_aqui
```

Ou mantenha a chave padrão no `application.yml` (apenas para desenvolvimento).

### 4️⃣ Execute a Aplicação

```bash
mvn clean install
mvn spring-boot:run
```

Ou execute diretamente pela IDE.

### 5️⃣ Acesse a Aplicação

- **API Base:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/health

---

## 📡 Endpoints da API

### 🔐 Autenticação

#### **POST** `/auth/register`
Registra um novo assessor no sistema.

**Request Body:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Response (201 Created):**
```json
{
  "sucesso": true,
  "mensagem": "Assessor registrado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "assessorId": "6748f3a2b1d5e8a3f4c2d1b0",
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "mensagem": "Assessor registrado com sucesso!"
  },
  "timestamp": "2024-10-20T14:30:00"
}
```

#### **POST** `/auth/login`
Realiza login de um assessor existente.

**Request Body:**
```json
{
  "email": "joao.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Response (200 OK):**
```json
{
  "sucesso": true,
  "mensagem": "Login realizado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "assessorId": "6748f3a2b1d5e8a3f4c2d1b0",
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "mensagem": "Login realizado com sucesso!"
  },
  "timestamp": "2024-10-20T14:32:00"
}
```

### ❤️ Health Check

#### **GET** `/health`
Verifica o status da aplicação.

**Response (200 OK):**
```json
{
  "sucesso": true,
  "mensagem": "Sistema operacional",
  "data": {
    "status": "UP",
    "application": "BartoFinance Backend",
    "version": "1.0.0",
    "timestamp": "2024-10-20T14:35:00"
  },
  "timestamp": "2024-10-20T14:35:00"
}
```

---

## 🔑 Autenticação

Após o login, você receberá um token JWT. Use-o em todas as requisições protegidas:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo com cURL:**
```bash
curl -X GET http://localhost:8080/api/investidores \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 📊 Entidades do Sistema

### 👤 Assessor
Usuário do sistema responsável por gerenciar investidores.

```java
{
  "id": "ObjectId",
  "nome": "String",
  "email": "String (único)",
  "senha": "String (criptografada)",
  "dataCadastro": "DateTime",
  "ultimoLogin": "DateTime",
  "ativo": "Boolean"
}
```

### 💼 Investidor
Cliente do assessor com perfil e patrimônio.

```java
{
  "id": "ObjectId",
  "nome": "String",
  "cpf": "String (único)",
  "email": "String",
  "telefone": "String",
  "perfilInvestidor": "Enum [CONSERVADOR, MODERADO, AGRESSIVO]",
  "patrimonio": "BigDecimal",
  "rendaMensal": "BigDecimal",
  "dataCadastro": "DateTime",
  "assessorId": "ObjectId"
}
```

### 💰 Aplicação
Investimento realizado em produtos financeiros.

```java
{
  "id": "ObjectId",
  "investidorId": "ObjectId",
  "tipoProduto": "Enum [CDB, TESOURO_DIRETO, ACOES, FUNDOS, CRIPTOMOEDAS, OUTROS]",
  "valorAplicado": "BigDecimal",
  "rentabilidadeEsperada": "BigDecimal",
  "dataAplicacao": "DateTime",
  "dataResgate": "DateTime (opcional)",
  "status": "Enum [ATIVA, RESGATADA, ENCERRADA]",
  "notas": "String"
}
```

### 📋 Relatório
Consolidação de aplicações e rendimentos.

### 💡 Insight
Análises e sugestões geradas por IA.

### 📝 Log
Auditoria de todas as ações do sistema.

---

## 🧪 Testando com Postman

1. **Importe a collection** (em breve disponível)
2. **Configure as variáveis:**
   - `base_url`: http://localhost:8080
   - `token`: (será preenchido automaticamente após login)

3. **Fluxo de teste:**
   1. Registre um assessor (`POST /auth/register`)
   2. Faça login (`POST /auth/login`)
   3. Copie o token retornado
   4. Use o token nas próximas requisições

---

## 📝 Logs e Auditoria

Todas as ações são registradas no MongoDB na collection `logs`:

```json
{
  "assessorId": "6748f3a2b1d5e8a3f4c2d1b0",
  "acao": "LOGIN_SUCCESS",
  "descricao": "Login realizado com sucesso",
  "endpoint": "/auth/login",
  "timestamp": "2024-10-20T14:32:00",
  "ip": "192.168.1.100",
  "sucesso": true
}
```

---

## 🔜 Próximos Passos

- [ ] Implementar CRUD completo de Investidores
- [ ] Implementar CRUD de Aplicações
- [ ] Sistema de geração de Relatórios (PDF, CSV, Excel)
- [ ] Integração com IA Gemini para Insights
- [ ] Dashboard com métricas agregadas
- [ ] Gráficos de evolução patrimonial
- [ ] Notificações por email
- [ ] Frontend em React

---

## 🛡️ Segurança

- ✅ Senhas criptografadas com BCrypt
- ✅ Autenticação JWT com expiração de 24h
- ✅ CORS configurado para frontend
- ✅ Validação de inputs com Bean Validation
- ✅ Tratamento global de exceções
- ✅ Logs de auditoria completos

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**BartoFinance Team**

- Email: contato@bartofinance.com
- LinkedIn: [linkedin.com/in/bartofinance](https://linkedin.com)

---

## 📞 Suporte

Para dúvidas e suporte, entre em contato:
- Email: suporte@bartofinance.com
- Issues: [GitHub Issues](https://github.com/seu-usuario/bartofinance/issues)

---

<p align="center">
  Feito com ❤️ para assessores de investimentos
</p>

