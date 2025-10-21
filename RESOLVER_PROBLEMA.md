# 🔥 Resolver Problema - BartoFinance

## ❌ Problema: Maven não instalado + Docker com erro de rede

## ✅ Soluções (escolha UMA):

---

## 🏆 SOLUÇÃO 1: IntelliJ IDEA Community (RECOMENDADA)

### Por que esta é a melhor?
- ✅ **Gratuita** e profissional
- ✅ **Maven já incluído** - não precisa instalar
- ✅ **Mais fácil** de usar
- ✅ Debug integrado
- ✅ Autocomplete inteligente

### Passos:

#### 1. Baixar IntelliJ IDEA
```
Link: https://www.jetbrains.com/idea/download/?section=windows
Versão: Community (FREE)
Tamanho: ~700 MB
```

#### 2. Instalar
- Execute o instalador
- Next → Next → Install
- Aguarde (~5 minutos)

#### 3. Abrir Projeto
1. Abra IntelliJ IDEA
2. **"Open"** (não New Project)
3. Navegue até: `C:\Users\caiohrm\Desktop\TCC BartoFinance`
4. Clique em **OK**

#### 4. Aguardar Sincronização
- IntelliJ vai detectar que é Maven
- Barra de progresso aparecerá embaixo
- Aguarde terminar (5-10 min primeira vez)
- Verá mensagem: "All files are up to date"

#### 5. Executar
1. No painel esquerdo, expanda:
   ```
   src → main → java → com.bartofinance
   ```
2. Clique direito em **BartoFinanceApplication**
3. Selecione: **"Run 'BartoFinanceApplication'"**

#### 6. Ver Resultado
Console mostrará:
```
╔══════════════════════════════════════════════════════════╗
║   BartoFinance Backend Started Successfully! 🚀         ║
╚══════════════════════════════════════════════════════════╝
```

✅ **Pronto! Acesse:** http://localhost:8080/swagger-ui.html

---

## 🥈 SOLUÇÃO 2: Instalar Maven

### Via Chocolatey (Recomendado)

#### 1. Abrir PowerShell como ADMINISTRADOR
- Win + X
- "Windows PowerShell (Admin)"

#### 2. Instalar Chocolatey
```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

#### 3. Instalar Maven
```powershell
choco install maven -y
```

Aguarde (~5 minutos)

#### 4. FECHAR e REABRIR PowerShell

#### 5. Verificar
```powershell
mvn -version
```

Deve mostrar:
```
Apache Maven 3.9.X
Java version: 17.0.X
```

#### 6. Executar Projeto
```powershell
cd "C:\Users\caiohrm\Desktop\TCC BartoFinance"
mvn spring-boot:run
```

---

## 🥉 SOLUÇÃO 3: Eclipse IDE (Alternativa)

Se não quiser IntelliJ:

#### 1. Baixar Eclipse
```
Link: https://www.eclipse.org/downloads/
Versão: Eclipse IDE for Enterprise Java and Web Developers
```

#### 2. Importar Projeto
1. `File` → `Import`
2. `Maven` → `Existing Maven Projects`
3. Selecione a pasta do projeto
4. `Finish`

#### 3. Executar
- Clique direito no projeto
- `Run As` → `Spring Boot App`

---

## ⚡ Comparação das Soluções

| Solução | Facilidade | Tempo Setup | Precisa Instalar |
|---------|------------|-------------|------------------|
| **IntelliJ IDEA** | ⭐⭐⭐⭐⭐ | 10 min | Apenas a IDE |
| **Maven CLI** | ⭐⭐⭐ | 5 min | Maven |
| **Eclipse** | ⭐⭐⭐⭐ | 10 min | Apenas a IDE |
| **Docker** | ⭐⭐ | ❌ Erro rede | Docker |

---

## 🎯 Minha Recomendação Pessoal

### Se você é:

**Iniciante em Java:**
→ Use **IntelliJ IDEA Community** (gratuito)
- Interface amigável
- Maven integrado
- Tudo em um lugar

**Já tem experiência:**
→ Instale **Maven via Chocolatey**
- Mais controle
- Mais rápido

**Quer aprender DevOps:**
→ Resolva problema do **Docker** (mais complexo)

---

## 🚀 Próximos Passos (Após Resolver)

### 1. Iniciar MongoDB
```powershell
docker-compose up -d
```

Se Docker não funcionar, use **MongoDB Compass** ou **MongoDB Atlas** (cloud gratuito)

### 2. Executar Aplicação
- Via IntelliJ: Botão Run
- Via Maven: `mvn spring-boot:run`

### 3. Testar
```
http://localhost:8080/swagger-ui.html
```

---

## 🆘 Ainda com Problema?

Me avise qual solução você escolheu e qual erro apareceu!

**Opções:**
1. "Vou instalar IntelliJ" → Te guio passo a passo
2. "Instalei Maven mas..." → Te ajudo com o erro
3. "Quero resolver o Docker" → Te ajudo com rede

---

## 💡 Dica Final

A maioria dos desenvolvedores Java usa **IntelliJ IDEA** ou **Eclipse**.

Para este projeto, **IntelliJ IDEA Community** é perfeito porque:
- É o que profissionais usam
- Gratuito para sempre
- Facilita MUITO o desenvolvimento
- Você vai precisar dele para as próximas etapas do projeto

🎓 **Vale a pena instalar!**


