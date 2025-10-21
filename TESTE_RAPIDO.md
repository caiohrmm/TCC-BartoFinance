# 🚀 BartoFinance - Teste Rápido da API

## 📋 Pré-requisitos

1. MongoDB rodando (porta 27017)
2. Aplicação rodando (porta 8080)

```bash
# Inicie o MongoDB
docker-compose up -d

# Execute a aplicação
mvn spring-boot:run
```

---

## ✅ Fluxo de Teste Completo

### 1️⃣ **Registrar Assessor**

```bash
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "nome": "Carlos Alberto Silva",
  "email": "carlos.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Resposta esperada:** 201 Created
```json
{
  "timestamp": "2024-01-20T10:00:00",
  "status": 201,
  "message": "Assessor registrado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "assessor": {
      "id": "65a1b2c3d4e5f6g7h8i9j0",
      "nome": "Carlos Alberto Silva",
      "email": "carlos.silva@bartofinance.com"
    }
  }
}
```

---

### 2️⃣ **Login (Obter Token)**

```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "carlos.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Resposta esperada:** 200 OK
```json
{
  "timestamp": "2024-01-20T10:01:00",
  "status": 200,
  "message": "Login realizado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsb3Muc2lsdmFAYmFydG9maW5hbmNlLmNvbSIsImFzc2Vzc29ySWQiOiI2NWExYjJjM2Q0ZTVmNmc3aDhpOWowIiwiaWF0IjoxNzA1NzQ0ODYwLCJleHAiOjE3MDU4MzEyNjB9.xyz123",
    "assessor": {
      "id": "65a1b2c3d4e5f6g7h8i9j0",
      "nome": "Carlos Alberto Silva",
      "email": "carlos.silva@bartofinance.com"
    }
  }
}
```

**⚠️ IMPORTANTE:** Copie o `token` para usar nas próximas requisições!

---

### 3️⃣ **Criar Investidor**

```bash
POST http://localhost:8080/investors
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9... (SEU TOKEN AQUI)
Content-Type: application/json

{
  "nome": "João Pedro Santos",
  "cpf": "12345678901",
  "email": "joao.santos@email.com",
  "telefone": "11987654321",
  "perfilInvestidor": "MODERADO",
  "patrimonioAtual": 150000.00,
  "rendaMensal": 12000.00,
  "objetivos": "Aposentadoria confortável e compra de imóvel"
}
```

**Resposta esperada:** 201 Created
```json
{
  "timestamp": "2024-01-20T10:05:00",
  "status": 201,
  "message": "Investidor criado com sucesso",
  "data": {
    "id": "65a1b2c3d4e5f6g7h8i9j1",
    "nome": "João Pedro Santos",
    "cpf": "12345678901",
    "email": "joao.santos@email.com",
    "telefone": "11987654321",
    "perfilInvestidor": "MODERADO",
    "patrimonioAtual": 150000.00,
    "rendaMensal": 12000.00,
    "objetivos": "Aposentadoria confortável e compra de imóvel",
    "assessorId": "65a1b2c3d4e5f6g7h8i9j0",
    "createdAt": "2024-01-20T10:05:00",
    "updatedAt": null
  }
}
```

**💾 GUARDE O ID DO INVESTIDOR:** `65a1b2c3d4e5f6g7h8i9j1`

---

### 4️⃣ **Listar Investidores**

```bash
GET http://localhost:8080/investors
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Resposta esperada:** 200 OK
```json
{
  "timestamp": "2024-01-20T10:06:00",
  "status": 200,
  "message": "Investidores listados com sucesso",
  "data": [
    {
      "id": "65a1b2c3d4e5f6g7h8i9j1",
      "nome": "João Pedro Santos",
      "perfilInvestidor": "MODERADO",
      "patrimonioAtual": 150000.00,
      ...
    }
  ]
}
```

---

### 5️⃣ **Criar Carteira de Investimento**

```bash
POST http://localhost:8080/portfolios
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "nome": "Carteira Equilibrada 2024",
  "descricao": "Mix balanceado de renda fixa (50%) e variável (50%)",
  "tipo": "PERSONALIZADA",
  "risco": "MODERADO",
  "metaRentabilidade": 15.50,
  "investidorId": "65a1b2c3d4e5f6g7h8i9j1"
}
```

**Resposta esperada:** 201 Created
```json
{
  "timestamp": "2024-01-20T10:10:00",
  "status": 201,
  "message": "Carteira criada com sucesso",
  "data": {
    "id": "65a1b2c3d4e5f6g7h8i9j2",
    "nome": "Carteira Equilibrada 2024",
    "descricao": "Mix balanceado de renda fixa (50%) e variável (50%)",
    "tipo": "PERSONALIZADA",
    "risco": "MODERADO",
    "metaRentabilidade": 15.50,
    "rentabilidadeAtual": 0.00,
    "valorTotal": 0.00,
    "investidorId": "65a1b2c3d4e5f6g7h8i9j1",
    "assessorId": "65a1b2c3d4e5f6g7h8i9j0",
    "createdAt": "2024-01-20T10:10:00",
    "updatedAt": null
  }
}
```

---

### 6️⃣ **Criar Carteira Modelo (Template)**

```bash
POST http://localhost:8080/portfolios
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "nome": "Carteira Conservadora Padrão",
  "descricao": "80% renda fixa, 20% fundos conservadores",
  "tipo": "MODELO",
  "risco": "BAIXO",
  "metaRentabilidade": 10.00
}
```

---

### 7️⃣ **Listar Carteiras do Assessor**

```bash
GET http://localhost:8080/portfolios
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 8️⃣ **Listar Apenas Carteiras Modelo**

```bash
GET http://localhost:8080/portfolios/models
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 9️⃣ **Gerar Insight com IA (Mock Gemini)**

```bash
POST http://localhost:8080/insights/generate
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "investidorId": "65a1b2c3d4e5f6g7h8i9j1"
}
```

**Resposta esperada:** 201 Created
```json
{
  "timestamp": "2024-01-20T10:15:00",
  "status": 201,
  "message": "Insight gerado com sucesso",
  "data": {
    "id": "65a1b2c3d4e5f6g7h8i9j3",
    "investidorId": "65a1b2c3d4e5f6g7h8i9j1",
    "texto": "João Pedro Santos, com perfil moderado, você pode equilibrar entre renda fixa (50%) e renda variável (50%). Considere fundos multimercado, ações de empresas consolidadas (blue chips) e alguns fundos imobiliários.",
    "geradoPor": "Gemini AI (Mock)",
    "tipo": "SUGESTAO",
    "dataGeracao": "2024-01-20T10:15:00"
  }
}
```

---

### 🔟 **Listar Insights do Investidor**

```bash
GET http://localhost:8080/insights?investorId=65a1b2c3d4e5f6g7h8i9j1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 1️⃣1️⃣ **Atualizar Investidor**

```bash
PUT http://localhost:8080/investors/65a1b2c3d4e5f6g7h8i9j1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "nome": "João Pedro Santos",
  "cpf": "12345678901",
  "email": "joao.santos@email.com",
  "telefone": "11987654321",
  "perfilInvestidor": "MODERADO",
  "patrimonioAtual": 175000.00,
  "rendaMensal": 13500.00,
  "objetivos": "Aposentadoria confortável, compra de imóvel e viagem internacional"
}
```

---

### 1️⃣2️⃣ **Deletar Carteira**

```bash
DELETE http://localhost:8080/portfolios/65a1b2c3d4e5f6g7h8i9j2
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Resposta esperada:** 200 OK
```json
{
  "timestamp": "2024-01-20T10:20:00",
  "status": 200,
  "message": "Carteira deletada com sucesso",
  "data": null
}
```

---

## 🔍 Verificar Logs no MongoDB

Os logs estão sendo gravados automaticamente via AOP!

```javascript
// Conecte ao MongoDB
use bartofinance

// Liste os últimos 10 logs
db.logs.find().sort({timestamp: -1}).limit(10).pretty()
```

Você verá logs como:
```json
{
  "_id": ObjectId("..."),
  "usuario": "carlos.silva@bartofinance.com",
  "endpoint": "/investors",
  "metodo": "POST",
  "ip": "192.168.1.100",
  "sucesso": true,
  "mensagem": "Requisição executada com sucesso",
  "timestamp": ISODate("2024-01-20T10:05:00Z")
}
```

---

## 🌐 Testar no Swagger

Acesse: **http://localhost:8080/swagger-ui.html**

1. Clique em **"Authorize"** no topo
2. Cole o token obtido no login
3. Clique em **"Authorize"**
4. Agora pode testar todos os endpoints diretamente!

---

## ⚠️ Erros Comuns

### 401 Unauthorized
❌ **Problema:** Token inválido ou expirado  
✅ **Solução:** Faça login novamente e obtenha um novo token

### 400 Bad Request
❌ **Problema:** Dados de entrada inválidos  
✅ **Solução:** Verifique os campos obrigatórios e formatos (CPF, email, etc.)

### 404 Not Found
❌ **Problema:** Recurso não encontrado  
✅ **Solução:** Verifique se o ID informado existe

### 403 Forbidden
❌ **Problema:** Tentando acessar recurso de outro assessor  
✅ **Solução:** Só é possível acessar seus próprios investidores/carteiras

---

## 📊 Testes de Perfil de Risco

Crie 3 investidores com perfis diferentes e gere insights para cada:

### Perfil CONSERVADOR
```json
{
  "nome": "Maria Conservadora",
  "cpf": "11111111111",
  "perfilInvestidor": "CONSERVADOR",
  ...
}
```

### Perfil MODERADO
```json
{
  "nome": "José Moderado",
  "cpf": "22222222222",
  "perfilInvestidor": "MODERADO",
  ...
}
```

### Perfil AGRESSIVO
```json
{
  "nome": "Pedro Agressivo",
  "cpf": "33333333333",
  "perfilInvestidor": "AGRESSIVO",
  ...
}
```

Gere insights para cada um e compare as recomendações!

---

## ✅ Checklist de Testes

- [ ] Registrar assessor
- [ ] Fazer login
- [ ] Criar 3 investidores (um de cada perfil)
- [ ] Listar investidores
- [ ] Criar carteira personalizada
- [ ] Criar carteira modelo
- [ ] Listar carteiras
- [ ] Listar apenas carteiras modelo
- [ ] Gerar insight para cada investidor
- [ ] Listar insights
- [ ] Atualizar um investidor
- [ ] Deletar uma carteira
- [ ] Verificar logs no MongoDB

---

<p align="center">
  <strong>🎉 API Testada e Funcionando! 🎉</strong>
</p>

