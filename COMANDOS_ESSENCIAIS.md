# ⚡ Comandos Essenciais - BartoFinance

Referência rápida dos comandos mais usados no projeto.

---

## 🚀 Inicialização

### Iniciar MongoDB (Docker)
```bash
docker-compose up -d
```

### Iniciar Aplicação
```bash
mvn spring-boot:run
```

### Ou usar o Maven Wrapper (se disponível)
```bash
./mvnw spring-boot:run          # Linux/Mac
.\mvnw.cmd spring-boot:run       # Windows
```

---

## 🛑 Parar Serviços

### Parar Aplicação
```
Ctrl + C
```

### Parar MongoDB
```bash
docker-compose down
```

### Parar e Remover Volumes
```bash
docker-compose down -v
```

---

## 🔨 Build e Compilação

### Compilar Projeto
```bash
mvn clean install
```

### Compilar Sem Testes
```bash
mvn clean install -DskipTests
```

### Limpar Build
```bash
mvn clean
```

### Gerar JAR
```bash
mvn package
```

### Executar JAR
```bash
java -jar target/bartofinance-backend-1.0.0.jar
```

---

## 🧪 Testes

### Executar Todos os Testes
```bash
mvn test
```

### Executar Teste Específico
```bash
mvn test -Dtest=BartoFinanceApplicationTests
```

### Ver Cobertura de Testes (se configurado)
```bash
mvn jacoco:report
```

---

## 🐳 Docker

### Ver Containers Rodando
```bash
docker ps
```

### Ver Todos os Containers
```bash
docker ps -a
```

### Ver Logs do MongoDB
```bash
docker logs bartofinance-mongodb
```

### Ver Logs em Tempo Real
```bash
docker logs -f bartofinance-mongodb
```

### Entrar no Container MongoDB
```bash
docker exec -it bartofinance-mongodb mongosh
```

### Comandos MongoDB no Container
```javascript
// Listar databases
show dbs

// Usar database
use bartofinance

// Listar collections
show collections

// Ver assessores
db.assessores.find().pretty()

// Ver logs
db.logs.find().sort({timestamp: -1}).limit(10).pretty()

// Limpar collection
db.logs.deleteMany({})
```

---

## 🔍 Verificação de Status

### Health Check
```bash
curl http://localhost:8080/health
```

### Verificar se MongoDB está rodando
```bash
docker exec bartofinance-mongodb mongosh --eval "db.runCommand({ ping: 1 })"
```

### Ver Informações da JVM
```bash
jps -l
```

---

## 📡 Testes de API (cURL)

### Health Check
```bash
curl http://localhost:8080/health
```

### Registrar Assessor
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","email":"teste@bartofinance.com","senha":"senha123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@bartofinance.com","senha":"senha123"}'
```

### Requisição Autenticada (exemplo)
```bash
curl -X GET http://localhost:8080/api/investidores \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 📝 Logs

### Ver Logs da Aplicação (se configurado arquivo)
```bash
tail -f logs/bartofinance.log
```

### Grep nos Logs
```bash
grep "ERROR" logs/bartofinance.log
grep "JWT" logs/bartofinance.log
```

---

## 🔄 Git

### Status
```bash
git status
```

### Adicionar Arquivos
```bash
git add .
```

### Commit
```bash
git commit -m "feat: adiciona nova feature"
```

### Push
```bash
git push origin main
```

### Ver Histórico
```bash
git log --oneline --graph
```

### Ver Mudanças
```bash
git diff
```

---

## 🔧 Maven Úteis

### Ver Dependências
```bash
mvn dependency:tree
```

### Atualizar Dependências
```bash
mvn versions:display-dependency-updates
```

### Ver Plugins
```bash
mvn help:effective-pom
```

---

## 🌐 URLs Importantes

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| API Base | http://localhost:8080 | - |
| Swagger UI | http://localhost:8080/swagger-ui.html | - |
| API Docs JSON | http://localhost:8080/api-docs | - |
| Health Check | http://localhost:8080/health | - |
| Mongo Express | http://localhost:8081 | admin / admin123 |

---

## 🐛 Troubleshooting

### Porta 8080 ocupada
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### MongoDB não conecta
```bash
# Restart MongoDB
docker-compose restart mongodb

# Ver logs
docker logs bartofinance-mongodb

# Recriar container
docker-compose down
docker-compose up -d
```

### Limpar cache do Maven
```bash
mvn clean install -U
```

### Erro de memória JVM
```bash
# Aumentar memória
export MAVEN_OPTS="-Xmx2048m"
mvn clean install
```

---

## 📦 Backup e Restore

### Backup MongoDB (Docker)
```bash
docker exec bartofinance-mongodb mongodump --out /backup
docker cp bartofinance-mongodb:/backup ./backup-$(date +%Y%m%d)
```

### Restore MongoDB
```bash
docker cp ./backup bartofinance-mongodb:/backup
docker exec bartofinance-mongodb mongorestore /backup
```

---

## 🔐 Variáveis de Ambiente

### Definir Variável (Linux/Mac)
```bash
export JWT_SECRET=sua_chave_secreta_aqui
export MONGODB_URI=mongodb://localhost:27017/bartofinance
```

### Definir Variável (Windows)
```powershell
$env:JWT_SECRET="sua_chave_secreta_aqui"
$env:MONGODB_URI="mongodb://localhost:27017/bartofinance"
```

---

## 📊 Monitoramento

### Ver Uso de Memória
```bash
# Docker
docker stats

# Processos Java
jps -lvm
```

### Ver Conexões MongoDB
```bash
docker exec bartofinance-mongodb mongosh --eval "db.serverStatus().connections"
```

---

## 🎯 Comandos do Dia a Dia

### Desenvolvimento
```bash
# 1. Iniciar MongoDB
docker-compose up -d

# 2. Executar aplicação
mvn spring-boot:run

# 3. Testar endpoint
curl http://localhost:8080/health
```

### Deploy Local
```bash
# 1. Build
mvn clean package -DskipTests

# 2. Executar JAR
java -jar target/bartofinance-backend-1.0.0.jar
```

### Atualizar Código
```bash
# 1. Pull mudanças
git pull origin main

# 2. Recompilar
mvn clean install

# 3. Restart
mvn spring-boot:run
```

---

## ⚡ Atalhos do IntelliJ IDEA

| Ação | Windows/Linux | Mac |
|------|---------------|-----|
| Executar Aplicação | Shift+F10 | Ctrl+R |
| Parar Aplicação | Ctrl+F2 | Cmd+F2 |
| Recompilar | Ctrl+Shift+F9 | Cmd+Shift+F9 |
| Buscar Classe | Ctrl+N | Cmd+O |
| Buscar Arquivo | Ctrl+Shift+N | Cmd+Shift+O |
| Formatar Código | Ctrl+Alt+L | Cmd+Alt+L |

---

<p align="center">
  <strong>💡 Dica: Adicione este arquivo aos favoritos para acesso rápido!</strong>
</p>

