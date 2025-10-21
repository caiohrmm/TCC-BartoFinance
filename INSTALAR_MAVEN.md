# 🔧 Instalar Maven no Windows - Passo a Passo

## Método 1: Via Chocolatey (Recomendado)

### 1. Abrir PowerShell como Administrador
- Pressione `Win + X`
- Selecione "Windows PowerShell (Admin)"

### 2. Instalar Chocolatey
```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### 3. Instalar Maven
```powershell
choco install maven -y
```

### 4. Verificar
```powershell
# Fechar e reabrir o PowerShell
mvn -version
```

---

## Método 2: Download Manual

### 1. Baixar Maven
- Acesse: https://maven.apache.org/download.cgi
- Baixe: `apache-maven-3.9.X-bin.zip` (Binary zip archive)

### 2. Extrair
- Extraia para: `C:\Program Files\Apache\maven`
- Deve ficar: `C:\Program Files\Apache\maven\bin\mvn.cmd`

### 3. Configurar Variáveis de Ambiente

#### Via Interface Gráfica:
1. Pressione `Win + Pause` → "Configurações avançadas do sistema"
2. Clique em "Variáveis de Ambiente"
3. Em "Variáveis do sistema", clique em "Path" → "Editar"
4. Clique em "Novo" e adicione: `C:\Program Files\Apache\maven\bin`
5. Clique em "OK" em todas as janelas

#### Via PowerShell (como Admin):
```powershell
# Adicionar ao PATH do sistema
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\Apache\maven\bin", "Machine")

# Criar MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Program Files\Apache\maven", "Machine")
```

### 4. Verificar Instalação
```powershell
# Fechar e reabrir o PowerShell
mvn -version
```

Deve aparecer algo como:
```
Apache Maven 3.9.X
Maven home: C:\Program Files\Apache\maven
Java version: 17.0.X
```

---

## Método 3: Via IntelliJ IDEA (Mais Fácil)

Se você tem **IntelliJ IDEA Community** (gratuito):

### 1. Baixar IntelliJ IDEA
- Acesse: https://www.jetbrains.com/idea/download/?section=windows
- Baixe a versão **Community** (gratuita)
- Instale normalmente

### 2. Abrir o Projeto
1. Abra IntelliJ IDEA
2. `File` → `Open`
3. Selecione a pasta: `C:\Users\caiohrm\Desktop\TCC BartoFinance`
4. Clique em "OK"

### 3. Aguardar Sincronização
- O IntelliJ vai detectar que é um projeto Maven
- Vai baixar todas as dependências automaticamente
- Aguarde a barra de progresso no canto inferior

### 4. Executar o Projeto
1. Localize o arquivo: `BartoFinanceApplication.java`
2. Clique direito nele
3. Selecione: `Run 'BartoFinanceApplication'`

**PRONTO!** O IntelliJ usa seu próprio Maven interno, não precisa instalar nada!

---

## ✅ Após Instalar Maven

### 1. Iniciar MongoDB
```powershell
docker-compose up -d
```

### 2. Executar Aplicação
```powershell
mvn spring-boot:run
```

### 3. Acessar
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- Mongo Express: http://localhost:8081

---

## 🐛 Problemas Comuns

### "mvn não é reconhecido"
**Solução:**
1. Feche e reabra o PowerShell/Terminal
2. Verifique se o PATH está correto
3. Execute como Administrador

### "JAVA_HOME não definido"
**Solução:**
```powershell
# Definir JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
```

### Maven muito lento no primeiro build
**Normal!** O Maven baixa todas as dependências na primeira vez. Pode levar 5-10 minutos.

---

## 💡 Recomendação Final

**Para desenvolvedores:**
👉 **IntelliJ IDEA Community** (Opção mais fácil e profissional)

**Para aprender/testar:**
👉 **Maven via Chocolatey** (Rápido e simples)

**Para produção:**
👉 **Docker** (Depois de resolver problemas de rede)

---

## 📞 Ainda com Problemas?

Se nenhuma opção funcionar, me avise qual erro apareceu e eu te ajudo!


