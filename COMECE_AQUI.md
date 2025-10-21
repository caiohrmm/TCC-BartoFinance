# 🚀 BartoFinance - COMECE AQUI!

## 📌 Início Rápido (5 minutos)

### ✅ Pré-requisitos
- ✅ Java 17 instalado
- ✅ Maven instalado
- ✅ MongoDB rodando (porta 27017)

---

## 🎯 PASSO A PASSO

### 1️⃣ MongoDB está rodando?

```bash
# Verifique se está rodando
docker ps

# Se não estiver, inicie:
docker-compose up -d
```

### 2️⃣ Aplicação está rodando?

```bash
# Execute se ainda não estiver rodando
mvn spring-boot:run
```

**Aguarde a mensagem:**
```
Started BartoFinanceApplication in X.XXX seconds
```

### 3️⃣ Acesse o Swagger

Abra no navegador:
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 TESTE COMPLETO EM 10 PASSOS

### Passo 1: Registrar Assessor

No Swagger, vá em **"Auth"** → **POST /auth/register**

Click em **"Try it out"** e cole:

```json
{
  "nome": "Carlos Silva",
  "email": "carlos@bartofinance.com",
  "senha": "senha123"
}
```

Click em **"Execute"**

✅ **Resposta esperada:** Status 201 Created com token

### Passo 2: Copiar o Token

Na resposta, copie o valor de `data.token`:

```json
{
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..." ← COPIE ISSO!
  }
}
```

### Passo 3: Autorizar no Swagger

1. No topo da página do Swagger, clique em **"Authorize" 🔓**
2. Cole o token no campo
3. Click em **"Authorize"**
4. Click em **"Close"**

🎉 **Agora você está autenticado!**

### Passo 4: Criar um Investidor

Vá em **"Investidores"** → **POST /investors**

```json
{
  "nome": "João Pedro Santos",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "perfilInvestidor": "MODERADO",
  "patrimonioAtual": 100000,
  "rendaMensal": 8000,
  "objetivos": "Aposentadoria e compra de imóvel"
}
```

✅ **Resposta:** Status 201 Created com os dados do investidor

**💾 GUARDE O `id` DO INVESTIDOR!**

### Passo 5: Listar Investidores

**GET /investors** → Execute

Você verá todos os investidores cadastrados.

### Passo 6: Criar Carteira Personalizada

**POST /portfolios**

```json
{
  "nome": "Carteira Equilibrada 2024",
  "descricao": "50% renda fixa e 50% variável",
  "tipo": "PERSONALIZADA",
  "risco": "MODERADO",
  "metaRentabilidade": 15.5,
  "investidorId": "COLE_O_ID_DO_INVESTIDOR_AQUI"
}
```

✅ **Resposta:** Carteira criada com sucesso!

### Passo 7: Criar Carteira Modelo

**POST /portfolios**

```json
{
  "nome": "Carteira Conservadora Padrão",
  "descricao": "80% renda fixa, 20% fundos conservadores",
  "tipo": "MODELO",
  "risco": "BAIXO",
  "metaRentabilidade": 10.0
}
```

✅ **Resposta:** Template criado!

### Passo 8: Listar Apenas Carteiras Modelo

**GET /portfolios/models** → Execute

Você verá apenas as carteiras do tipo MODELO.

### Passo 9: Gerar Insight com IA

**POST /insights/generate**

```json
{
  "investidorId": "COLE_O_ID_DO_INVESTIDOR_AQUI"
}
```

✅ **Resposta:** Insight personalizado gerado!

Exemplo de resposta:
```json
{
  "texto": "João Pedro Santos, com perfil moderado, você pode equilibrar entre renda fixa (50%) e renda variável (50%). Considere fundos multimercado, ações de empresas consolidadas (blue chips) e alguns fundos imobiliários.",
  "geradoPor": "Gemini AI (Mock)",
  "tipo": "SUGESTAO"
}
```

### Passo 10: Listar Insights

**GET /insights?investorId=ID_DO_INVESTIDOR** → Execute

Você verá todos os insights gerados para aquele investidor.

---

## 🎉 PARABÉNS!

Você testou com sucesso:
- ✅ Autenticação JWT
- ✅ Criação de investidor
- ✅ Listagem de dados
- ✅ Criação de carteira personalizada
- ✅ Criação de carteira modelo
- ✅ Geração de insight com IA
- ✅ Consulta de dados

---

## 🔍 Verificar Logs Automáticos

Todos os requests foram registrados automaticamente via AOP!

Conecte ao MongoDB:

```javascript
// Use MongoDB Compass ou shell
use bartofinance

// Liste os logs
db.logs.find().sort({timestamp: -1}).limit(10).pretty()
```

Você verá logs detalhados de TODAS as requisições! 🎯

---

## 📚 Próximos Passos

1. **Explorar Swagger** - Teste todos os endpoints disponíveis
2. **Ler TESTE_RAPIDO.md** - Mais exemplos de uso
3. **Ler BACKEND_EXPANSION_GUIDE.md** - Como expandir o sistema
4. **Ler PROJETO_STATUS_FINAL.md** - Status detalhado

---

## 🆘 Problemas Comuns

### ❌ Erro 401 Unauthorized
- **Solução:** Token expirado. Faça login novamente e pegue um novo token.

### ❌ Erro 400 Bad Request
- **Solução:** Verifique os dados enviados (CPF, email, campos obrigatórios).

### ❌ Erro "Cannot connect to MongoDB"
- **Solução:** 
  ```bash
  docker-compose down
  docker-compose up -d
  ```

### ❌ Porta 8080 já em uso
- **Solução:** 
  ```bash
  # Windows
  netstat -ano | findstr :8080
  taskkill /PID [PID] /F
  
  # Linux/Mac
  lsof -i :8080
  kill -9 [PID]
  ```

---

## 🎓 Entendendo a Estrutura

### Perfis de Risco

| Perfil | Descrição | Recomendação |
|--------|-----------|--------------|
| **CONSERVADOR** | Baixo risco | 70% renda fixa, 30% fundos |
| **MODERADO** | Risco balanceado | 50% renda fixa, 50% variável |
| **AGRESSIVO** | Alto risco | 70% ações, 20% alternativos, 10% reserva |

### Tipos de Carteira

| Tipo | Uso |
|------|-----|
| **MODELO** | Template reutilizável para vários investidores |
| **PERSONALIZADA** | Específica para um investidor |

### Níveis de Risco

| Risco | Perfil Recomendado |
|-------|--------------------|
| **BAIXO** | Conservador |
| **MODERADO** | Moderado |
| **ALTO** | Agressivo |

---

## 💡 Dica Pro

### Testar com 3 Perfis Diferentes

Crie 3 investidores:

1. **Maria Conservadora** - Perfil CONSERVADOR
2. **José Moderado** - Perfil MODERADO  
3. **Pedro Agressivo** - Perfil AGRESSIVO

Gere insights para cada um e compare as recomendações!

---

## 📊 Dashboard de Testes

### Checklist:
- [ ] Registrar assessor
- [ ] Fazer login
- [ ] Criar investidor conservador
- [ ] Criar investidor moderado
- [ ] Criar investidor agressivo
- [ ] Criar carteira modelo
- [ ] Criar carteira personalizada
- [ ] Gerar insight para cada perfil
- [ ] Listar investidores
- [ ] Listar carteiras
- [ ] Atualizar um investidor
- [ ] Verificar logs no MongoDB

---

<p align="center">
  <strong>🎉 Agora você está pronto para usar o BartoFinance! 🎉</strong>
</p>

<p align="center">
  <em>Acesse o Swagger e divirta-se testando!</em><br>
  <strong>http://localhost:8080/swagger-ui.html</strong>
</p>

---

## 🚀 Links Rápidos

- **Swagger:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/health
- **API Docs:** http://localhost:8080/v3/api-docs

---

**Desenvolvido para o TCC BartoFinance** 💙

