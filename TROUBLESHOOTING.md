# 🔧 Guia de Troubleshooting - BartoFinance

## 🚨 Problemas Comuns e Soluções

### 1. Backend não inicia

**Problema**: Erros de compilação nos testes
```
InsightServiceTest.java: method gerarInsight cannot be applied to given types
```

**Solução**: Os testes estão desatualizados. Iniciar com `-DskipTests`:
```bash
mvn spring-boot:run -DskipTests
```

---

### 2. Erro ao criar Carteira MODELO

**Problema**: `Bad Request` ao criar carteira sem investidor

**Causa**: Frontend estava enviando string vazia `""` ao invés de `null/undefined`

**✅ Corrigido**: Agora envia `undefined` quando investidorId está vazio

**Teste**:
1. Criar carteira com tipo "MODELO"
2. Campo investidor fica desabilitado
3. Criação deve funcionar sem erros

---

### 3. Erro ao criar Aplicação

**Problema**: `DateTimeParseException: Text '2025-10-25' could not be parsed`

**Causa**: Backend espera `LocalDateTime` mas recebia apenas data

**✅ Corrigido**: Frontend agora converte `2025-10-25` → `2025-10-25T00:00:00`

**Teste**:
1. Criar aplicação em uma carteira
2. Selecionar data da compra
3. Deve salvar sem erros

---

### 4. Dashboard não carrega dados

**Problema**: Dashboard mostra zeros

**✅ Corrigido**: Agora carrega dados reais com `forkJoin`

**Como verificar**:
```typescript
// Dashboard agora faz 3 requisições em paralelo:
- GET /investors (lista investidores)
- GET /portfolios (lista carteiras) 
- GET /applications (lista aplicações)
```

---

### 5. Erro ao acessar Carteira Details

**Sintomas**: Página não carrega ou erro no console

**Possíveis causas**:
1. Carteira não existe no banco
2. Token expirado (24h)
3. Backend não está rodando

**Verificar**:
```bash
# 1. Backend está rodando?
curl http://localhost:8080/actuator/health

# 2. Token válido?
# Fazer logout e login novamente

# 3. Carteira existe?
# Verificar no MongoDB ou via Postman
```

---

## 🤖 Testando a Integração com Gemini AI

### Pré-requisitos
1. ✅ API Key configurada em `application.yml`
2. ✅ Dependências Maven instaladas
3. ✅ Backend rodando

### Testar via Postman

**1. Análise de Perfil:**
```http
POST http://localhost:8080/ai/analisar-perfil
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "nome": "João Silva",
  "perfil": "CONSERVADOR",
  "rendaMensal": 5000.00,
  "patrimonioAtual": 50000.00
}
```

**Resposta esperada:**
```json
{
  "sucesso": true,
  "mensagem": "Análise gerada com sucesso",
  "data": {
    "analise": "Com base no perfil conservador de João Silva..."
  }
}
```

**2. Análise de Carteira:**
```http
POST http://localhost:8080/ai/analisar-carteira
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "nomeCarteira": "Carteira Conservadora",
  "tipoCarteira": "PERSONALIZADA",
  "riscoCarteira": "BAIXO",
  "valorTotal": 100000.00,
  "rentabilidadeAtual": 8.5,
  "metaRentabilidade": 10.0
}
```

**3. Sugestão de Diversificação:**
```http
POST http://localhost:8080/ai/sugerir-diversificacao
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "perfilInvestidor": "MODERADO",
  "valorDisponivel": 50000.00
}
```

---

## 🐛 Logs e Debug

### Ver logs do backend:
```bash
# Se configurado para arquivo:
tail -f logs/bartofinance.log

# Ou no console onde iniciou o Maven
```

### Ver logs do frontend:
```bash
# No navegador, abrir DevTools (F12)
# Aba Console
```

### Logs importantes do Gemini AI:
```
✅ Sucesso:
"Enviando requisição para Gemini AI..."
"Resposta recebida do Gemini AI com sucesso"

❌ Erro:
"Erro na API do Gemini: 401" → API Key inválida
"Erro na API do Gemini: 429" → Limite de requisições excedido
"Erro ao se comunicar com Gemini AI" → Problema de rede
```

---

## 📊 Verificar Status do Sistema

### Backend (Spring Boot):
```bash
# Verificar se está rodando
curl http://localhost:8080/actuator/health

# Verificar endpoints
curl http://localhost:8080/swagger-ui.html
```

### Frontend (Angular):
```bash
# Iniciar
cd bartofinance-frontend
npm start

# Deve abrir em http://localhost:4200
```

### MongoDB:
```bash
# Conectar
mongosh

# Ver databases
show dbs

# Usar database
use bartofinance

# Ver coleções
show collections

# Contar documentos
db.assessores.countDocuments()
db.investidores.countDocuments()
db.portfolios.countDocuments()
db.aplicacoes.countDocuments()
```

---

## 🔑 Renovar Token

Se o token expirou (24h):
1. Fazer logout
2. Fazer login novamente
3. Novo token será gerado

---

## 🆘 Se nada funcionar

1. **Limpar e recompilar tudo:**
```bash
# Backend
mvn clean install -DskipTests

# Frontend
cd bartofinance-frontend
rm -rf node_modules package-lock.json
npm install
```

2. **Reiniciar MongoDB:**
```bash
# Windows (como administrador)
net stop MongoDB
net start MongoDB
```

3. **Verificar portas:**
```bash
# Porta 8080 (backend) livre?
netstat -ano | findstr :8080

# Porta 4200 (frontend) livre?
netstat -ano | findstr :4200

# Porta 27017 (MongoDB) livre?
netstat -ano | findstr :27017
```

---

## 📞 Contatos e Links Úteis

- **Gemini AI Docs**: https://ai.google.dev/docs
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Angular Docs**: https://angular.io/docs
- **MongoDB Docs**: https://docs.mongodb.com/

---

**Última atualização**: 25/10/2025

