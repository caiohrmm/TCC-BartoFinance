# 🤖 Chatbot de IA - BartoFinance

## Visão Geral

O BartoFinance agora possui um **Assistente Virtual com Inteligência Artificial** alimentado pelo **Google Gemini AI**. O chatbot está disponível em todas as páginas do sistema e foi especialmente treinado para ajudar assessores de investimento.

---

## 🎯 Funcionalidades

### 1. **Sempre Disponível**
- Botão flutuante no canto inferior direito
- Acessível de qualquer página do sistema
- Interface moderna e responsiva

### 2. **Especializado em Assessoria Financeira**
O chatbot foi treinado para ajudar com:

#### 📊 **Perfis de Investidores**
- Explicar diferenças entre Conservador, Moderado e Agressivo
- Sugerir produtos adequados para cada perfil
- Orientar sobre mudança de perfil

#### 💼 **Gestão de Carteiras**
- Estratégias de diversificação
- Balanceamento de portfólio
- Metas de rentabilidade
- Análise de risco

#### 💰 **Produtos Financeiros**
- CDB (Certificado de Depósito Bancário)
- Tesouro Direto
- Ações
- Fundos de Investimento
- Criptomoedas

#### 📈 **Análise e Performance**
- Interpretação de rentabilidade
- Comparação com benchmarks
- Quando rebalancear carteiras
- Como encerrar aplicações

#### 🎓 **Uso do Sistema**
- Como cadastrar investidores
- Diferença entre carteiras MODELO e PERSONALIZADA
- Como adicionar aplicações
- Funcionalidades disponíveis

---

## 💡 Como Usar

### Abrir o Chatbot
1. Clique no botão **🤖** flutuante no canto inferior direito
2. O chat se expandirá em uma janela elegante

### Fazer Perguntas
Digite sua pergunta e pressione Enter ou clique em **📨**

**Exemplos de perguntas:**
```
"Como diversificar uma carteira conservadora?"
"Qual a diferença entre CDB e Tesouro Direto?"
"Como definir a meta de rentabilidade?"
"Quando devo rebalancear uma carteira?"
"O que é uma carteira MODELO?"
```

### Controles Disponíveis
- **🔄** Limpar chat (novo começo)
- **➖** Minimizar (fica compacto no canto)
- **✕** Fechar completamente

---

## 🎨 Interface

### Estados do Chatbot

#### 1. **Fechado (Botão Flutuante)**
```
┌─────────┐
│   🤖    │  ← Botão com animação bounce
│  (ping) │  ← Indicador "online"
└─────────┘
```

#### 2. **Minimizado**
```
┌─────────────────────┐
│ 🤖 Assistente IA    │
│ Clique para expandir│
└─────────────────────┘
```

#### 3. **Aberto (Janela de Chat)**
```
┌─────────────────────────────┐
│ 🤖 Assistente IA    🔄➖✕  │ ← Header azul
├─────────────────────────────┤
│                             │
│  👋 Mensagem de boas-vindas │
│                             │
│     Sua pergunta aqui    ▶  │ ← Suas mensagens (direita)
│                             │
│  ◀ Resposta da IA           │ ← Respostas da IA (esquerda)
│                             │
├─────────────────────────────┤
│ [Digite sua pergunta...]    │ ← Input
│                       📨    │ ← Botão enviar
└─────────────────────────────┘
```

---

## 🔧 Recursos Técnicos

### Componente: `AiChatbotComponent`
```
bartofinance-frontend/src/app/shared/components/ai-chatbot/
├── ai-chatbot.component.ts    (Lógica)
├── ai-chatbot.component.html  (Template)
└── ai-chatbot.component.scss  (Estilos)
```

### Integração
- Importado globalmente em `app.component.ts`
- Disponível em **todas as páginas**
- Usa `AIService` para comunicação com backend

### Backend
- Endpoint: `POST /ai/gerar-insight`
- Modelo: `gemini-2.0-flash-exp`
- Limite: 150 palavras por resposta

---

## 📝 Prompt Especializado

O chatbot usa um prompt especializado:

```
Você é um assistente especializado em assessoria de investimentos
e gestão financeira para o sistema BartoFinance.

Seu papel é ajudar assessores de investimento com:
- Orientações sobre perfis de investidores
- Sugestões de estratégias de diversificação
- Explicações sobre tipos de produtos financeiros
- Dicas de gestão de carteiras
- Interpretação de rentabilidade e metas
- Melhores práticas de assessoria financeira
- Funcionalidades do sistema BartoFinance

Sempre responda de forma profissional, clara e objetiva.
Use no máximo 150 palavras por resposta.
```

---

## 🎯 Exemplos de Interação

### Exemplo 1: Perfil de Investidor
```
👤 Usuário: "Qual a diferença entre perfil conservador e agressivo?"

🤖 IA: "O perfil conservador prioriza segurança e baixo risco, 
preferindo renda fixa (CDB, Tesouro Direto) com retornos 
previsíveis. Ideal para quem não tolera perdas.

Já o perfil agressivo aceita alto risco em busca de maiores 
retornos, investindo mais em ações e criptomoedas. Suporta 
volatilidade e tem horizonte de longo prazo.

No BartoFinance, você define o perfil ao cadastrar o investidor, 
e isso ajuda a sugerir produtos adequados para cada cliente."
```

### Exemplo 2: Diversificação
```
👤 Usuário: "Como diversificar R$ 50.000 para um cliente moderado?"

🤖 IA: "Para um perfil moderado com R$ 50.000, sugiro:

• 40% Renda Fixa (R$ 20.000): CDB/Tesouro para estabilidade
• 30% Fundos Multimercado (R$ 15.000): Diversificação gerenciada
• 20% Ações (R$ 10.000): Potencial de crescimento
• 10% Reserva (R$ 5.000): Liquidez imediata

Essa alocação equilibra segurança e rentabilidade, adequada 
ao perfil moderado. Revise semestralmente!"
```

### Exemplo 3: Sistema
```
👤 Usuário: "O que é uma carteira MODELO?"

🤖 IA: "Carteira MODELO é um template no BartoFinance que você 
cria sem vincular a um investidor específico. Serve como 
referência para replicar em múltiplos clientes.

Exemplo: Você cria 'Carteira Conservadora Padrão' com 
alocações ideais. Depois, ao vincular a um investidor, define 
os valores reais.

Já a carteira PERSONALIZADA é única para cada cliente desde 
o início. Use MODELO para otimizar seu trabalho!"
```

---

## 🚀 Benefícios

### Para Assessores:
✅ Suporte 24/7 para dúvidas
✅ Respostas rápidas e precisas
✅ Treinamento contínuo
✅ Aumento de produtividade

### Para o Sistema:
✅ Reduz chamados de suporte
✅ Melhora experiência do usuário
✅ Diferencial competitivo
✅ Escalabilidade

---

## 🔒 Segurança e Privacidade

- ✅ Não armazena dados sensíveis
- ✅ Histórico local (não persistido)
- ✅ Comunicação criptografada (HTTPS)
- ✅ Autenticação por JWT

---

## 🎨 Personalização

### Cores
- **Primário**: Azul `primary-500` (Tema BartoFinance)
- **Secundário**: Branco com sombras
- **Ênfase**: Verde (indicador online)

### Animações
- Bounce no botão
- Slide-up ao abrir
- Fade-in nas mensagens
- Ping no indicador online
- Dots pulsando no loading

---

## 📊 Métricas (Futuro)

Possíveis melhorias:
- Contador de perguntas por usuário
- Tópicos mais consultados
- Taxa de satisfação
- Tempo médio de resposta
- Exportar conversas

---

## 🔮 Roadmap Futuro

### Curto Prazo:
- [ ] Perguntas sugeridas (quick replies)
- [ ] Histórico persistente
- [ ] Busca no histórico

### Médio Prazo:
- [ ] Anexar capturas de tela
- [ ] Integração com docs do sistema
- [ ] Modo escuro

### Longo Prazo:
- [ ] Voz (speech-to-text)
- [ ] Análise contextual (saber página atual)
- [ ] Sugestões proativas

---

## 🐛 Troubleshooting

### Chatbot não aparece
✅ Verifique se está logado
✅ Limpe cache do navegador
✅ Recarregue a página

### IA não responde
✅ Verifique conexão com backend
✅ Confirme API Key do Gemini
✅ Veja logs no console (F12)

### Respostas lentas
✅ Normal para primeira requisição
✅ Gemini AI pode demorar 2-5s
✅ Verifique conexão de internet

---

**Desenvolvido com 💙 por BartoFinance**
**Powered by Google Gemini AI 🤖**

