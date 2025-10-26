# 🚀 Como Iniciar o BartoFinance

## Passo a Passo Completo

### 1️⃣ MongoDB (Banco de Dados)
```bash
# Verificar se está rodando
mongosh

# Se não estiver, iniciar (Windows como administrador):
net start MongoDB
```

---

### 2️⃣ Backend (API Spring Boot)

**Opção 1 - Linha de Comando:**
```bash
cd "C:\Users\caiohrm\Desktop\TCC BartoFinance"
mvn spring-boot:run -DskipTests
```

**Opção 2 - Script PowerShell:**
```powershell
# Criar arquivo start-backend.ps1:
cd "C:\Users\caiohrm\Desktop\TCC BartoFinance"
mvn clean compile -DskipTests
mvn spring-boot:run -DskipTests
```

**Aguardar mensagem:**
```
Started BartoFinanceApplication in X seconds
```

**Verificar:**
- http://localhost:8080/swagger-ui.html (Documentação da API)
- http://localhost:8080/actuator/health (Status)

---

### 3️⃣ Frontend (Angular)

```bash
cd "C:\Users\caiohrm\Desktop\TCC BartoFinance\bartofinance-frontend"
npm start
```

**Aguardar:**
```
✔ Browser application bundle generation complete.
Initial Chunk Files   | Names         |  Raw Size
...
Application bundle generation complete. [X.XXX seconds]

** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ **
```

**Acessar:**
- http://localhost:4200

---

## 📝 Primeira Vez? Cadastre-se!

1. Abrir http://localhost:4200
2. Clicar em "Criar Conta"
3. Preencher:
   - Nome: Seu nome
   - Email: seu@email.com
   - Senha: suasenha123
4. Fazer login
5. Você será o primeiro assessor! 🎉

---

## 🧪 Testar com Dados Reais

### Via Interface (recomendado):
1. **Dashboard** → Ver estatísticas
2. **Investidores** → Criar novo investidor
3. **Carteiras** → Criar carteira para o investidor
4. **Detalhes da Carteira** → Adicionar aplicações
5. **Dashboard** → Dados atualizados! ✨

### Via Postman:
1. Importar `BartoFinance.postman_collection.json`
2. Importar `BartoFinance.postman_environment.json`
3. Executar "Auth → Register" ou "Auth → Login"
4. Token será capturado automaticamente!
5. Testar outros endpoints

---

## 🤖 Testar IA (Gemini)

### Pré-requisito:
✅ API Key configurada em `src/main/resources/application.yml`

### Via Postman:
```http
POST http://localhost:8080/ai/analisar-perfil
Authorization: Bearer {token_automatico}
Content-Type: application/json

{
  "nome": "João Silva",
  "perfil": "CONSERVADOR",
  "rendaMensal": 5000.00,
  "patrimonioAtual": 50000.00
}
```

### Resposta:
```json
{
  "sucesso": true,
  "data": {
    "analise": "Análise detalhada do Gemini AI..."
  }
}
```

---

## ⚡ Scripts Úteis

### Reiniciar Tudo (PowerShell como Admin):
```powershell
# Parar processos
Get-Process | Where-Object {$_.ProcessName -like "*java*"} | Stop-Process -Force
Get-Process | Where-Object {$_.ProcessName -like "*node*"} | Stop-Process -Force

# Reiniciar MongoDB
net stop MongoDB
net start MongoDB

# Limpar e recompilar
cd "C:\Users\caiohrm\Desktop\TCC BartoFinance"
mvn clean compile -DskipTests

cd bartofinance-frontend
npm install

# Iniciar backend (nova janela)
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'C:\Users\caiohrm\Desktop\TCC BartoFinance'; mvn spring-boot:run -DskipTests"

# Aguardar 30 segundos
Start-Sleep -Seconds 30

# Iniciar frontend (nova janela)
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'C:\Users\caiohrm\Desktop\TCC BartoFinance\bartofinance-frontend'; npm start"
```

---

## 🐛 Problemas?

Consulte `TROUBLESHOOTING.md` para soluções detalhadas!

**Problemas comuns:**
- ❌ Porta 8080 ocupada → Matar processo Java
- ❌ Porta 4200 ocupada → Matar processo Node
- ❌ MongoDB não conecta → Verificar se está rodando
- ❌ Token expirou → Fazer login novamente
- ❌ Erro de compilação → `mvn clean install -DskipTests`

---

## 📊 Portas Usadas

| Serviço | Porta | URL |
|---------|-------|-----|
| Backend (Spring) | 8080 | http://localhost:8080 |
| Frontend (Angular) | 4200 | http://localhost:4200 |
| MongoDB | 27017 | mongodb://localhost:27017 |
| Swagger UI | 8080 | http://localhost:8080/swagger-ui.html |

---

## ✅ Checklist de Inicialização

- [ ] MongoDB rodando
- [ ] Backend compilou sem erros
- [ ] Backend iniciou (mensagem "Started BartoFinanceApplication")
- [ ] Swagger acessível em http://localhost:8080/swagger-ui.html
- [ ] Frontend compilou sem erros
- [ ] Frontend acessível em http://localhost:4200
- [ ] Login/Register funcionando
- [ ] Dashboard mostrando dados

---

**Pronto para usar!** 🎉

Se tudo estiver ✅, você pode começar a usar o sistema normalmente.

