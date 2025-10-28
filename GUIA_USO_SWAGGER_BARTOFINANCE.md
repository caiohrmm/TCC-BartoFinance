# 🚀 Guia de Uso da API BartoFinance - Swagger

## 📋 Como Acessar e Usar a Documentação Swagger

### 1. 🌐 Acessar Swagger UI

**URLs Disponíveis:**
- **Local**: `http://localhost:8080/swagger-ui.html`
- **Desenvolvimento**: `https://api-dev.bartofinance.com/swagger-ui.html`
- **Produção**: `https://api.bartofinance.com/swagger-ui.html`

### 2. 🔐 Processo de Autenticação

#### Passo 1: Registrar um Assessor
```bash
POST /auth/register
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@bartofinance.com",
  "senha": "minhasenha123"
}
```

**Resposta:**
```json
{
  "sucesso": true,
  "mensagem": "Assessor registrado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "assessorId": "64f8a1b2c3d4e5f6a7b8c9d0",
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "mensagem": "Assessor registrado com sucesso!"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Passo 2: Fazer Login (se já tiver conta)
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "joao.silva@bartofinance.com",
  "senha": "minhasenha123"
}
```

#### Passo 3: Autorizar no Swagger
1. Clique no botão **"Authorize"** 🔒 no topo da página
2. Cole o token JWT no campo **"Value"**
3. Clique em **"Authorize"**
4. Clique em **"Close"**

### 3. 👥 Gerenciar Investidores

#### Criar um Investidor
```bash
POST /investors
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nome": "Maria Santos",
  "cpf": "12345678901",
  "email": "maria.santos@email.com",
  "telefone": "11999999999",
  "perfilInvestidor": "MODERADO",
  "patrimonioAtual": 50000.00,
  "rendaMensal": 8000.00,
  "objetivos": "Aposentadoria e reserva de emergência"
}
```

#### Listar Investidores
```bash
GET /investors
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Buscar Investidor por ID
```bash
GET /investors/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 4. 💼 Gerenciar Carteiras

#### Criar uma Carteira
```bash
POST /portfolios
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nome": "Carteira Conservadora",
  "descricao": "Carteira focada em segurança",
  "tipo": "PERSONALIZADA",
  "risco": "BAIXO",
  "metaRentabilidade": 8.5,
  "investidorId": "64f8a1b2c3d4e5f6a7b8c9d1"
}
```

#### Listar Carteiras
```bash
GET /portfolios
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Listar Carteiras Modelo
```bash
GET /portfolios/models
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 5. 💰 Gerenciar Aplicações

#### Criar uma Aplicação
```bash
POST /applications
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

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

#### Listar Aplicações
```bash
GET /applications?portfolioId={portfolioId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Encerrar Aplicação
```bash
PATCH /applications/{id}/encerrar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "dataVenda": "2024-01-20T15:30:00",
  "rentabilidadeAtual": 15.2
}
```

### 6. 🤖 Usar Inteligência Artificial

#### Analisar Perfil de Investidor
```bash
POST /ai/analisar-perfil
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nome": "Maria Santos",
  "perfil": "MODERADO",
  "rendaMensal": 8000.0,
  "patrimonioAtual": 50000.0
}
```

#### Analisar Carteira
```bash
POST /ai/analisar-carteira
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nomeCarteira": "Carteira Conservadora",
  "tipoCarteira": "PERSONALIZADA",
  "riscoCarteira": "BAIXO",
  "valorTotal": 50000.0,
  "rentabilidadeAtual": 8.5,
  "metaRentabilidade": 10.0
}
```

#### Sugerir Diversificação
```bash
POST /ai/sugerir-diversificacao
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "perfilInvestidor": "MODERADO",
  "valorDisponivel": 10000.0
}
```

#### Gerar Insight Genérico
```bash
POST /ai/gerar-insight
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "prompt": "Como diversificar uma carteira conservadora?"
}
```

### 7. 📊 Consultar Relatórios

#### Gerar Relatório de Investidor
```bash
POST /reports/investor
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "investidorId": "64f8a1b2c3d4e5f6a7b8c9d1",
  "tipoRelatorio": "INVESTIDOR",
  "periodoInicio": "2024-01-01",
  "periodoFim": "2024-01-31"
}
```

### 8. 🔍 Consultar Logs

#### Listar Logs de Auditoria
```bash
GET /logs?usuario={email}&dataInicio={data}&dataFim={data}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 9. ❤️ Health Check

#### Verificar Status da API
```bash
GET /auth/health
```

---

## 🎯 Dicas para Usar o Swagger

### ✅ Boas Práticas

1. **🔐 Sempre Autorize**: Lembre-se de autorizar com o token JWT antes de testar endpoints protegidos
2. **📝 Use Exemplos**: Os exemplos fornecidos são funcionais e podem ser usados diretamente
3. **🔍 Teste Incremental**: Comece com endpoints simples e vá aumentando a complexidade
4. **📊 Monitore Respostas**: Observe os códigos de status e mensagens de erro
5. **🔄 Teste Cenários**: Teste tanto casos de sucesso quanto de erro

### ⚠️ Cuidados Importantes

1. **🔒 Token Seguro**: Não compartilhe seu token JWT
2. **⏰ Expiração**: Tokens expiram em 24 horas
3. **📝 Dados Reais**: Use dados de teste, não informações pessoais reais
4. **🔄 Limpeza**: Limpe dados de teste após os testes
5. **📊 Monitoramento**: Monitore os logs para verificar as ações

### 🚀 Funcionalidades Avançadas

1. **🔍 Filtros**: Use os filtros para encontrar endpoints específicos
2. **📱 Responsivo**: A interface funciona bem em dispositivos móveis
3. **💾 Download**: Baixe a especificação OpenAPI para usar em outras ferramentas
4. **🔗 Deep Linking**: URLs específicas para cada endpoint
5. **📊 Métricas**: Veja tempo de resposta e status codes

---

## 📚 Recursos Adicionais

### 📖 Documentação Completa
- **Backend**: `DOCUMENTACAO_BACKEND_BARTOFINANCE.md`
- **Swagger**: `DOCUMENTACAO_SWAGGER_BARTOFINANCE.md`

### 🔗 URLs Importantes
- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI JSON**: `/api-docs`
- **OpenAPI YAML**: `/api-docs.yaml`
- **Health Check**: `/auth/health`

### 🛠️ Ferramentas Compatíveis
- **Postman**: Importe a coleção via OpenAPI
- **Insomnia**: Use a especificação OpenAPI
- **VS Code**: Extensões para OpenAPI
- **IntelliJ**: Plugins para Swagger

---

**🎉 Agora você está pronto para usar completamente a API BartoFinance através do Swagger!**

*Todas as funcionalidades estão documentadas com exemplos práticos e podem ser testadas diretamente na interface.*
