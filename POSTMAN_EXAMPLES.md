# 📮 Exemplos Postman - BartoFinance API

Este documento contém exemplos práticos de como testar a API usando Postman ou cURL.

---

## 🔧 Configuração Inicial

### Variáveis de Ambiente no Postman

Crie as seguintes variáveis no Postman:

- `base_url`: `http://localhost:8080`
- `token`: (será preenchido automaticamente após login)

---

## 📋 Endpoints

### 1. Health Check

Verifica se a API está funcionando.

**GET** `/health`

```bash
curl -X GET http://localhost:8080/health
```

**Resposta:**
```json
{
  "sucesso": true,
  "mensagem": "Sistema operacional",
  "data": {
    "status": "UP",
    "application": "BartoFinance Backend",
    "version": "1.0.0",
    "timestamp": "2024-10-20T14:35:00"
  }
}
```

---

### 2. Registrar Novo Assessor

**POST** `/auth/register`

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "senha": "senha123"
  }'
```

**Body JSON:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Resposta (201 Created):**
```json
{
  "sucesso": true,
  "mensagem": "Assessor registrado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhc3Nlc3NvcklkIjoiNjc0OGYzYTJiMWQ1ZThhM2Y0YzJkMWIwIiwic3ViIjoiam9hby5zaWx2YUBiYXJ0b2ZpbmFuY2UuY29tIiwiaWF0IjoxNzI5NDM1ODAwLCJleHAiOjE3Mjk1MjIyMDB9.abc123",
    "tipo": "Bearer",
    "assessorId": "6748f3a2b1d5e8a3f4c2d1b0",
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "mensagem": "Assessor registrado com sucesso!"
  },
  "timestamp": "2024-10-20T14:30:00"
}
```

---

### 3. Login de Assessor

**POST** `/auth/login`

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.silva@bartofinance.com",
    "senha": "senha123"
  }'
```

**Body JSON:**
```json
{
  "email": "joao.silva@bartofinance.com",
  "senha": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "sucesso": true,
  "mensagem": "Login realizado com sucesso!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhc3Nlc3NvcklkIjoiNjc0OGYzYTJiMWQ1ZThhM2Y0YzJkMWIwIiwic3ViIjoiam9hby5zaWx2YUBiYXJ0b2ZpbmFuY2UuY29tIiwiaWF0IjoxNzI5NDM1OTIwLCJleHAiOjE3Mjk1MjIzMjB9.xyz789",
    "tipo": "Bearer",
    "assessorId": "6748f3a2b1d5e8a3f4c2d1b0",
    "nome": "João Silva",
    "email": "joao.silva@bartofinance.com",
    "mensagem": "Login realizado com sucesso!"
  },
  "timestamp": "2024-10-20T14:32:00"
}
```

---

## 🔒 Usando o Token JWT

Após fazer login, copie o token retornado e use-o no header `Authorization` de todas as requisições protegidas:

```
Authorization: Bearer SEU_TOKEN_AQUI
```

### Exemplo com cURL:

```bash
curl -X GET http://localhost:8080/api/investidores \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Exemplo no Postman:

1. Vá em **Headers**
2. Adicione uma nova linha:
   - **Key:** `Authorization`
   - **Value:** `Bearer {{token}}`

---

## ❌ Exemplos de Erros

### Email já cadastrado

**Request:**
```json
{
  "nome": "Maria Santos",
  "email": "joao.silva@bartofinance.com",
  "senha": "senha456"
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Email já cadastrado no sistema",
  "path": "/auth/register",
  "timestamp": "2024-10-20T14:33:00"
}
```

---

### Credenciais inválidas

**Request:**
```json
{
  "email": "joao.silva@bartofinance.com",
  "senha": "senhaErrada"
}
```

**Response (401 Unauthorized):**
```json
{
  "status": 401,
  "erro": "Unauthorized",
  "mensagem": "Email ou senha inválidos",
  "path": "/auth/login",
  "timestamp": "2024-10-20T14:34:00"
}
```

---

### Validação de campos

**Request:**
```json
{
  "nome": "AB",
  "email": "email-invalido",
  "senha": "123"
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "erro": "Validation Error",
  "mensagem": "Erro de validação nos campos da requisição",
  "detalhes": [
    "nome: Nome deve ter entre 3 e 100 caracteres",
    "email: Email deve ser válido",
    "senha: Senha deve ter no mínimo 6 caracteres"
  ],
  "path": "/auth/register",
  "timestamp": "2024-10-20T14:35:00"
}
```

---

## 🧪 Fluxo de Teste Completo

### Passo 1: Registrar novo assessor
```bash
POST /auth/register
```

### Passo 2: Fazer login
```bash
POST /auth/login
```

### Passo 3: Copiar o token retornado

### Passo 4: Usar o token em requisições protegidas
```bash
GET /api/investidores
Authorization: Bearer SEU_TOKEN
```

---

## 📊 Status Codes

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Token inválido ou ausente |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

---

## 💡 Dicas

1. **Salve o token** após o login em uma variável de ambiente no Postman
2. **Use Collections** para organizar suas requisições
3. **Configure testes automatizados** no Postman para validar respostas
4. **Monitore os logs** da aplicação para debug

---

<p align="center">
  Happy Testing! 🚀
</p>

