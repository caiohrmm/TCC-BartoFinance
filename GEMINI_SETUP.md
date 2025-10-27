# 🤖 Configuração do Assistente IA - BartoFinance

## 📋 Pré-requisitos

Para que o assistente IA funcione completamente, você precisa de uma API key válida do Google Gemini.

## 🔑 Como Obter a API Key

1. Acesse: https://makersuite.google.com/app/apikey
2. Faça login com sua conta Google
3. Clique em "Create API Key"
4. Copie a chave gerada

## ⚙️ Configuração

### Opção 1: Variável de Ambiente (Recomendado)
```bash
# Windows (PowerShell)
$env:GOOGLE_API_KEY="sua_api_key_aqui"

# Linux/Mac
export GOOGLE_API_KEY="sua_api_key_aqui"
```

### Opção 2: Arquivo application.yml
Edite o arquivo `src/main/resources/application.yml`:
```yaml
gemini:
  api:
    key: sua_api_key_aqui
    model: gemini-1.5-flash
```

## 🚀 Funcionamento

- **Com API Key válida**: Usa Google Gemini AI para respostas inteligentes
- **Sem API Key**: Usa sistema de fallback com respostas pré-definidas

## 🧪 Testando

1. Inicie o backend: `mvn spring-boot:run`
2. Inicie o frontend: `npm start`
3. Faça login no sistema
4. Clique no ícone do chatbot
5. Digite uma pergunta sobre investimentos

## 💡 Exemplos de Perguntas

- "Como diversificar uma carteira?"
- "Qual perfil de investidor escolher?"
- "Quais são as melhores opções de renda fixa?"
- "Como calcular metas de rentabilidade?"

## 🔧 Troubleshooting

**Erro**: "API key must either be provided..."
**Solução**: Verifique se a API key está configurada corretamente

**Erro**: "Erro ao se comunicar com Gemini AI"
**Solução**: O sistema usará automaticamente o fallback com respostas inteligentes
