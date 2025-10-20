# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

---

## [1.0.0] - 2024-10-20

### ✨ Adicionado

#### Infraestrutura
- Projeto Spring Boot 3.2.0 com Java 17
- Integração com MongoDB para persistência
- Sistema de logs com SLF4J
- Documentação automática com Swagger/OpenAPI 3.0
- Configuração de CORS para integração com frontend
- Sistema de build com Maven

#### Autenticação e Segurança
- Autenticação JWT completa (geração e validação de tokens)
- Registro de novos assessores com validação de dados
- Login de assessores com verificação de credenciais
- Encriptação de senhas com BCrypt
- Filtro JWT para proteção de rotas
- Spring Security configurado
- Validação de tokens com expiração de 24 horas

#### Modelos de Domínio
- Entidade Assessor (usuário do sistema)
- Entidade Investidor (clientes dos assessores)
- Entidade Aplicacao (investimentos financeiros)
- Entidade Relatorio (consolidação de dados)
- Entidade Insight (análises de IA)
- Entidade Log (auditoria completa)

#### Enumerações
- PerfilInvestidor (Conservador, Moderado, Agressivo)
- TipoProduto (CDB, Tesouro Direto, Ações, Fundos, Criptomoedas, Outros)
- StatusAplicacao (Ativa, Resgatada, Encerrada)
- TipoInsight (Risco, Oportunidade, Resumo, Sugestão)

#### Repositories
- AssessorRepository com busca por email
- InvestidorRepository com busca por CPF e assessor
- AplicacaoRepository com filtros por status e tipo
- RelatorioRepository com busca por investidor
- InsightRepository com filtros por tipo
- LogRepository com busca por período e ação

#### Services
- AuthService (registro e login de assessores)
- LogService (registro de auditoria de todas as ações)

#### Controllers
- AuthController (endpoints de autenticação)
- HealthController (verificação de status)

#### DTOs
- LoginRequest e RegisterRequest para autenticação
- AuthResponse para resposta de login/registro
- ApiResponse genérico para respostas padronizadas
- ErrorResponse para tratamento de erros

#### Exception Handling
- GlobalExceptionHandler para tratamento centralizado
- ResourceNotFoundException para recursos não encontrados
- BadRequestException para requisições inválidas
- UnauthorizedException para falhas de autenticação
- Tratamento de erros de validação de campos

#### Documentação
- README.md completo com instruções de uso
- POSTMAN_EXAMPLES.md com exemplos de requisições
- CHANGELOG.md para rastreamento de mudanças
- JavaDoc em todas as classes principais
- Swagger UI acessível em /swagger-ui.html

#### Logs e Auditoria
- Log de registro de assessores
- Log de login (sucesso e falha)
- Log de tentativas de acesso não autorizado
- Log de validação de tokens JWT
- Armazenamento de logs no MongoDB
- Registro de IP e timestamp em todas as ações

### 🔧 Configurado
- application.yml com configurações de MongoDB
- application.yml com configurações de JWT
- application.yml com configurações de logging
- SecurityConfig com proteção de rotas
- CORS configurado para React (portas 3000 e 5173)
- .gitignore para arquivos do projeto

---

## [Próximas Versões]

### 🚀 [1.1.0] - Planejado

#### CRUD Completo
- [ ] CRUD de Investidores
- [ ] CRUD de Aplicações
- [ ] CRUD de Relatórios
- [ ] Dashboard com métricas agregadas

#### Relatórios
- [ ] Geração de relatórios em PDF
- [ ] Exportação para CSV
- [ ] Exportação para Excel
- [ ] Gráficos de evolução patrimonial

### 🤖 [1.2.0] - Planejado

#### Integração com IA
- [ ] Integração com Gemini AI
- [ ] Geração automática de insights
- [ ] Análise de risco personalizada
- [ ] Sugestões de rebalanceamento de carteira
- [ ] Chatbot para assessores

### 🎨 [2.0.0] - Planejado

#### Frontend
- [ ] Interface React completa
- [ ] Dashboard interativo
- [ ] Gráficos e visualizações
- [ ] Filtros avançados
- [ ] Responsividade mobile

---

## Tipos de Mudanças

- `✨ Adicionado` - para novas funcionalidades
- `🔧 Modificado` - para mudanças em funcionalidades existentes
- `🗑️ Depreciado` - para funcionalidades que serão removidas
- `❌ Removido` - para funcionalidades removidas
- `🐛 Corrigido` - para correções de bugs
- `🔒 Segurança` - para correções de vulnerabilidades

