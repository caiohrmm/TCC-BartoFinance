# 🔄 Comandos Git para o Projeto BartoFinance

Este arquivo contém os comandos Git para versionar o projeto seguindo as melhores práticas.

---

## 📋 Primeiro Commit (Estrutura Inicial)

### 1. Inicializar Git
```bash
git init
```

### 2. Adicionar arquivos
```bash
# Adicionar todos os arquivos
git add .

# Verificar status
git status
```

### 3. Primeiro Commit
```bash
git commit -m "chore: initial project setup

- Adiciona estrutura base do projeto Spring Boot
- Configura Maven com todas as dependências necessárias
- Adiciona .gitignore e arquivos de configuração"
```

---

## 📦 Commits Organizados por Feature

### Backend Setup
```bash
git add pom.xml src/main/resources/application.yml
git commit -m "feat(config): adiciona configuração Maven e Spring Boot

- Spring Boot 3.2.0
- Spring Data MongoDB
- Spring Security
- JWT (JJWT 0.12.3)
- Lombok
- Swagger/OpenAPI
- Configuração MongoDB
- Configuração JWT"
```

### Modelos de Domínio
```bash
git add src/main/java/com/bartofinance/model/
git commit -m "feat(models): adiciona entidades do domínio

- Assessor: usuário do sistema
- Investidor: cliente do assessor
- Aplicacao: investimentos financeiros
- Relatorio: consolidação de dados
- Insight: análises de IA
- Log: auditoria completa
- Enums: PerfilInvestidor, TipoProduto, StatusAplicacao, TipoInsight"
```

### Repositories
```bash
git add src/main/java/com/bartofinance/repository/
git commit -m "feat(repositories): adiciona repositories do Spring Data MongoDB

- AssessorRepository com busca por email
- InvestidorRepository com busca por CPF
- AplicacaoRepository com filtros
- RelatorioRepository
- InsightRepository
- LogRepository"
```

### DTOs
```bash
git add src/main/java/com/bartofinance/dto/
git commit -m "feat(dto): adiciona DTOs para requests e responses

- LoginRequest e RegisterRequest
- AuthResponse com token JWT
- ApiResponse genérico
- ErrorResponse para erros padronizados"
```

### Exceptions
```bash
git add src/main/java/com/bartofinance/exception/
git commit -m "feat(exceptions): adiciona tratamento de exceções

- GlobalExceptionHandler centralizado
- ResourceNotFoundException
- BadRequestException
- UnauthorizedException
- Tratamento de erros de validação"
```

### Segurança e JWT
```bash
git add src/main/java/com/bartofinance/security/
git add src/main/java/com/bartofinance/config/SecurityConfig.java
git commit -m "feat(security): implementa autenticação JWT

- JwtUtil para geração e validação de tokens
- JwtAuthenticationFilter para interceptar requisições
- CustomUserDetailsService para carregar usuários
- SecurityConfig com Spring Security
- CORS configurado para frontend"
```

### Services
```bash
git add src/main/java/com/bartofinance/service/
git commit -m "feat(services): adiciona services de autenticação e logs

- AuthService: registro e login de assessores
- LogService: sistema de auditoria completo
- Validação de credenciais
- Geração de tokens JWT
- Registro de todas as ações"
```

### Controllers
```bash
git add src/main/java/com/bartofinance/controller/
git commit -m "feat(controllers): adiciona controllers REST

- AuthController: endpoints de autenticação
  - POST /auth/register
  - POST /auth/login
  - GET /auth/health
- HealthController: verificação de status
  - GET /health
  - GET /health/ping"
```

### OpenAPI Config
```bash
git add src/main/java/com/bartofinance/config/OpenApiConfig.java
git commit -m "feat(docs): adiciona configuração Swagger/OpenAPI

- Documentação completa da API
- Informações de contato e licença
- Configuração de segurança JWT
- Servidores local e produção"
```

### Main Application
```bash
git add src/main/java/com/bartofinance/BartoFinanceApplication.java
git add src/main/resources/banner.txt
git commit -m "feat(app): adiciona classe principal e banner

- BartoFinanceApplication com @EnableMongoAuditing
- Banner ASCII personalizado
- Mensagem de inicialização"
```

### Testes
```bash
git add src/test/
git commit -m "test: adiciona estrutura de testes

- BartoFinanceApplicationTests
- Configuração para testes futuros"
```

### Docker
```bash
git add docker-compose.yml
git commit -m "feat(docker): adiciona Docker Compose para MongoDB

- MongoDB containerizado
- Mongo Express (UI)
- Volumes persistentes
- Network configurada"
```

### Documentação
```bash
git add README.md INICIO_RAPIDO.md INSTALACAO.md POSTMAN_EXAMPLES.md
git add CONTRIBUINDO.md CHANGELOG.md PROJETO_COMPLETO.md LICENSE
git commit -m "docs: adiciona documentação completa do projeto

- README.md: documentação principal
- INICIO_RAPIDO.md: guia de 5 minutos
- INSTALACAO.md: guia detalhado
- POSTMAN_EXAMPLES.md: exemplos de requisições
- CONTRIBUINDO.md: guia de contribuição
- CHANGELOG.md: histórico de mudanças
- PROJETO_COMPLETO.md: resumo do projeto
- LICENSE: MIT License"
```

### .gitignore
```bash
git add .gitignore
git commit -m "chore: adiciona .gitignore

- Arquivos compilados
- IDEs
- Logs
- Secrets"
```

---

## 🌿 Branches Recomendadas

### Branch Main (Principal)
```bash
# Branch principal - código estável
git checkout -b main
```

### Branch Develop (Desenvolvimento)
```bash
# Branch de desenvolvimento
git checkout -b develop
```

### Feature Branches
```bash
# Para novas features
git checkout -b feature/crud-investidores
git checkout -b feature/relatorios-pdf
git checkout -b feature/integracao-gemini
```

### Hotfix Branches
```bash
# Para correções urgentes
git checkout -b hotfix/correcao-jwt
```

---

## 🔗 Conectar ao GitHub

### 1. Criar Repositório no GitHub
Acesse: https://github.com/new

### 2. Conectar Repositório Local
```bash
git remote add origin https://github.com/seu-usuario/bartofinance.git
```

### 3. Verificar Remote
```bash
git remote -v
```

### 4. Push Inicial
```bash
# Push da branch main
git branch -M main
git push -u origin main

# Push da branch develop (se criada)
git push -u origin develop
```

---

## 📝 Padrão de Commits

### Tipos de Commit
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação (não afeta código)
- `refactor`: Refatoração
- `test`: Testes
- `chore`: Tarefas de manutenção
- `perf`: Melhorias de performance
- `ci`: Integração contínua

### Formato
```
tipo(escopo): descrição curta

Descrição detalhada (opcional)

- Detalhe 1
- Detalhe 2
```

### Exemplos
```bash
git commit -m "feat(investidores): adiciona CRUD completo de investidores"
git commit -m "fix(auth): corrige validação de token expirado"
git commit -m "docs(readme): atualiza instruções de instalação"
git commit -m "refactor(services): melhora organização do código"
git commit -m "test(auth): adiciona testes de autenticação"
```

---

## 🏷️ Tags (Versionamento)

### Criar Tag
```bash
# Tag simples
git tag v1.0.0

# Tag anotada (recomendado)
git tag -a v1.0.0 -m "Release 1.0.0 - Backend Setup Completo"
```

### Listar Tags
```bash
git tag
```

### Push Tags
```bash
# Push tag específica
git push origin v1.0.0

# Push todas as tags
git push origin --tags
```

---

## 🔍 Comandos Úteis

### Ver Histórico
```bash
# Histórico resumido
git log --oneline --graph --decorate --all

# Últimos 10 commits
git log -10 --pretty=format:"%h - %an, %ar : %s"
```

### Ver Mudanças
```bash
# Mudanças não staged
git diff

# Mudanças staged
git diff --staged

# Mudanças em arquivo específico
git diff src/main/java/com/bartofinance/service/AuthService.java
```

### Desfazer Mudanças
```bash
# Desfazer mudanças não staged
git checkout -- arquivo.java

# Desfazer último commit (mantém mudanças)
git reset --soft HEAD~1

# Desfazer último commit (descarta mudanças)
git reset --hard HEAD~1
```

---

## 🎯 Workflow Recomendado

### 1. Feature Nova
```bash
# Criar branch
git checkout -b feature/nova-feature

# Desenvolver...
git add .
git commit -m "feat: adiciona nova feature"

# Push
git push origin feature/nova-feature

# Criar Pull Request no GitHub
```

### 2. Mesclar Feature
```bash
# Voltar para develop
git checkout develop

# Mesclar feature
git merge feature/nova-feature

# Deletar branch (opcional)
git branch -d feature/nova-feature
```

### 3. Release
```bash
# Mesclar develop em main
git checkout main
git merge develop

# Criar tag
git tag -a v1.1.0 -m "Release 1.1.0"

# Push
git push origin main
git push origin --tags
```

---

## ✅ Checklist Antes do Commit

- [ ] Código compila sem erros
- [ ] Testes passam (quando houver)
- [ ] Código formatado
- [ ] Sem console.log ou comentários desnecessários
- [ ] Mensagem de commit descritiva
- [ ] .gitignore atualizado

---

<p align="center">
  <strong>🎉 Boas práticas de versionamento! 🎉</strong>
</p>

