# ⚡ Início Rápido - BartoFinance

Guia de 5 minutos para começar a usar o BartoFinance.

---

## 🚀 Passos Rápidos

### 1. Iniciar MongoDB (Docker)

```bash
docker-compose up -d
```

### 2. Executar a Aplicação

```bash
mvn spring-boot:run
```

Aguarde ver a mensagem:
```
╔══════════════════════════════════════════════════════════╗
║   BartoFinance Backend Started Successfully! 🚀         ║
║   API Documentation: http://localhost:8080/swagger-ui.html
╚══════════════════════════════════════════════════════════╝
```

### 3. Testar a API

Abra o navegador em: **http://localhost:8080/swagger-ui.html**

---

## 🧪 Teste Rápido com cURL

### 1️⃣ Verificar se está funcionando

```bash
curl http://localhost:8080/health
```

### 2️⃣ Registrar um assessor

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"João Silva\",\"email\":\"joao@bartofinance.com\",\"senha\":\"senha123\"}"
```

**Resposta esperada:**
```json
{
  "sucesso": true,
  "mensagem": "Assessor registrado com sucesso!",
  "data": {
    "token": "eyJhbGc...",
    "tipo": "Bearer",
    "assessorId": "...",
    "nome": "João Silva",
    "email": "joao@bartofinance.com"
  }
}
```

### 3️⃣ Fazer login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"joao@bartofinance.com\",\"senha\":\"senha123\"}"
```

### 4️⃣ Copiar o token

Copie o valor do campo `token` da resposta.

### 5️⃣ Usar o token em requisições protegidas

```bash
curl -X GET http://localhost:8080/api/investidores \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🌐 Acessos Importantes

| Recurso | URL | Credenciais |
|---------|-----|-------------|
| API Base | http://localhost:8080 | - |
| Swagger UI | http://localhost:8080/swagger-ui.html | - |
| Health Check | http://localhost:8080/health | - |
| Mongo Express | http://localhost:8081 | admin / admin123 |

---

## 📱 Usando Postman

1. Abra o Postman
2. Crie uma nova requisição
3. Configure:
   - **Method:** POST
   - **URL:** `http://localhost:8080/auth/register`
   - **Body:** Raw → JSON
   ```json
   {
     "nome": "Maria Santos",
     "email": "maria@bartofinance.com",
     "senha": "senha123"
   }
   ```
4. Clique em **Send**
5. Copie o token retornado

Para próximas requisições:
- Vá em **Headers**
- Adicione: `Authorization: Bearer SEU_TOKEN`

---

## 🛑 Parar Tudo

```bash
# Parar a aplicação
Ctrl + C

# Parar o MongoDB (Docker)
docker-compose down
```

---

## 📚 Próximos Passos

- 📖 Leia o [README completo](README.md)
- 🔧 Veja a [instalação detalhada](INSTALACAO.md)
- 📮 Explore os [exemplos Postman](POSTMAN_EXAMPLES.md)
- 🤝 Contribua: [CONTRIBUINDO.md](CONTRIBUINDO.md)

---

## ❓ Problemas?

### MongoDB não conecta
```bash
# Verifique se está rodando
docker ps

# Reinicie se necessário
docker-compose restart mongodb
```

### Porta 8080 ocupada
Mude no `application.yml`:
```yaml
server:
  port: 8081
```

### Erro de compilação
```bash
mvn clean install -U
```

---

<p align="center">
  <strong>🎉 Pronto! Você já está rodando o BartoFinance!</strong>
</p>

<p align="center">
  Explore a API no Swagger: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a>
</p>

