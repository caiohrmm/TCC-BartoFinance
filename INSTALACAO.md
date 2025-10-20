# 📦 Guia de Instalação - BartoFinance

Este guia fornece instruções detalhadas para configurar e executar o BartoFinance em sua máquina.

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ✅ **Java 17** ou superior
- ✅ **Maven 3.8+**
- ✅ **MongoDB** (local ou Atlas)
- ✅ **Git**
- ✅ **IDE** (IntelliJ IDEA recomendado)

---

## 🚀 Método 1: Instalação Manual

### 1. Instalar Java 17

#### Windows
```powershell
# Baixe e instale o Java 17 do site oficial
# https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

# Verifique a instalação
java -version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

#### macOS
```bash
brew install openjdk@17
java -version
```

### 2. Instalar Maven

#### Windows
```powershell
# Baixe do site oficial: https://maven.apache.org/download.cgi
# Configure a variável de ambiente MAVEN_HOME

# Verifique
mvn -version
```

#### Linux
```bash
sudo apt install maven
mvn -version
```

#### macOS
```bash
brew install maven
mvn -version
```

### 3. Instalar MongoDB

#### Windows
```powershell
# Baixe do site oficial: https://www.mongodb.com/try/download/community
# Instale e inicie o serviço

# Verifique
mongosh
```

#### Linux
```bash
# Ubuntu/Debian
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt-get update
sudo apt-get install -y mongodb-org

# Inicie o MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod
```

#### macOS
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

### 4. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/bartofinance.git
cd bartofinance
```

### 5. Configurar application.yml

Edite `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/bartofinance
      # Para MongoDB com autenticação:
      # uri: mongodb://admin:admin123@localhost:27017/bartofinance?authSource=admin
```

### 6. Compilar o Projeto

```bash
mvn clean install
```

### 7. Executar a Aplicação

```bash
mvn spring-boot:run
```

Ou execute pela IDE:
- Abra o projeto no IntelliJ IDEA
- Execute a classe `BartoFinanceApplication.java`

### 8. Verificar Instalação

Acesse no navegador:
- **API:** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/health

---

## 🐳 Método 2: Docker (Recomendado)

### 1. Instalar Docker

#### Windows
```powershell
# Baixe e instale Docker Desktop
# https://www.docker.com/products/docker-desktop
```

#### Linux
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

#### macOS
```bash
brew install --cask docker
```

### 2. Iniciar MongoDB com Docker Compose

```bash
# Na raiz do projeto
docker-compose up -d
```

Isso iniciará:
- **MongoDB** na porta `27017`
- **Mongo Express** (UI) na porta `8081`

Acesse o Mongo Express: http://localhost:8081
- Usuário: `admin`
- Senha: `admin123`

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

### 4. Parar os Containers

```bash
docker-compose down
```

---

## ☁️ Método 3: MongoDB Atlas (Cloud)

### 1. Criar Conta no MongoDB Atlas

Acesse: https://www.mongodb.com/cloud/atlas/register

### 2. Criar um Cluster

1. Clique em "Build a Database"
2. Escolha "FREE" (M0)
3. Selecione uma região próxima
4. Clique em "Create"

### 3. Configurar Acesso

1. Database Access → Add New Database User
   - Username: `bartofinance`
   - Password: (gere uma senha segura)
   - User Privileges: Read and write to any database

2. Network Access → Add IP Address
   - Clique em "Allow Access from Anywhere" (apenas para desenvolvimento)
   - IP: `0.0.0.0/0`

### 4. Obter String de Conexão

1. Clique em "Connect"
2. Escolha "Connect your application"
3. Copie a string de conexão

### 5. Configurar application.yml

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://bartofinance:<password>@cluster0.xxxxx.mongodb.net/bartofinance?retryWrites=true&w=majority
```

Substitua `<password>` pela senha do usuário.

---

## 🔧 Configurações Adicionais

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (não commitar!):

```env
JWT_SECRET=sua_chave_secreta_muito_longa_e_segura
MONGODB_URI=mongodb://localhost:27017/bartofinance
```

### Configurar JWT Secret

Recomendado gerar uma chave segura:

```bash
# Linux/Mac
openssl rand -base64 64

# Windows (PowerShell)
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
```

Adicione ao `application.yml`:

```yaml
jwt:
  secret: ${JWT_SECRET:sua_chave_aqui}
  expiration: 86400000 # 24 horas
```

---

## 🧪 Testar a Instalação

### 1. Via cURL

```bash
# Health Check
curl http://localhost:8080/health

# Registrar Assessor
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste",
    "email": "teste@bartofinance.com",
    "senha": "senha123"
  }'
```

### 2. Via Postman

1. Importe a collection (em breve)
2. Configure `base_url`: `http://localhost:8080`
3. Execute as requisições de teste

### 3. Via Swagger

1. Acesse: http://localhost:8080/swagger-ui.html
2. Teste os endpoints diretamente na interface

---

## 🐛 Problemas Comuns

### Erro: "Cannot connect to MongoDB"

**Solução:**
```bash
# Verifique se o MongoDB está rodando
sudo systemctl status mongod  # Linux
brew services list | grep mongodb  # macOS

# Inicie se necessário
sudo systemctl start mongod  # Linux
brew services start mongodb-community  # macOS
```

### Erro: "Port 8080 already in use"

**Solução:**
```yaml
# Mude a porta no application.yml
server:
  port: 8081
```

### Erro: "Java version not supported"

**Solução:**
```bash
# Verifique a versão do Java
java -version

# Instale Java 17 se necessário
sudo apt install openjdk-17-jdk  # Linux
brew install openjdk@17  # macOS
```

### Erro: "JWT secret key too short"

**Solução:**
```yaml
# Use uma chave de pelo menos 256 bits (32 caracteres)
jwt:
  secret: sua_chave_muito_longa_e_segura_com_pelo_menos_32_caracteres
```

---

## 📚 Próximos Passos

Após a instalação bem-sucedida:

1. ✅ Leia o [README.md](README.md) para entender a arquitetura
2. ✅ Teste os endpoints com [POSTMAN_EXAMPLES.md](POSTMAN_EXAMPLES.md)
3. ✅ Explore o [Swagger UI](http://localhost:8080/swagger-ui.html)
4. ✅ Veja como contribuir em [CONTRIBUINDO.md](CONTRIBUINDO.md)

---

## 📞 Suporte

Problemas? Entre em contato:

- **Email:** suporte@bartofinance.com
- **Issues:** [GitHub Issues](https://github.com/seu-usuario/bartofinance/issues)

---

<p align="center">
  Boa instalação! 🚀
</p>

