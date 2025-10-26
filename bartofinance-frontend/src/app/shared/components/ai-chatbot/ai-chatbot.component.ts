import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AIService } from '../../../core/services/ai.service';
import { ToastService } from '../../../core/services/toast.service';

export interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

@Component({
  selector: 'app-ai-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ai-chatbot.component.html',
  styleUrl: './ai-chatbot.component.scss'
})
export class AiChatbotComponent {
  isOpen = signal(false);
  isMinimized = signal(false);
  messages = signal<ChatMessage[]>([]);
  userInput = signal('');
  isLoading = signal(false);

  constructor(
    private aiService: AIService,
    private toastService: ToastService
  ) {
    // Mensagem de boas-vindas
    this.messages.set([{
      role: 'assistant',
      content: 'Olá! Sou o assistente virtual BartoFinance. Estou aqui para ajudar com dúvidas sobre assessoria de investimentos, gestão de carteiras, perfis de investidores e estratégias financeiras. Como posso auxiliá-lo hoje?',
      timestamp: new Date()
    }]);
  }

  toggleChat(): void {
    if (this.isMinimized()) {
      this.isMinimized.set(false);
    } else {
      this.isOpen.set(!this.isOpen());
    }
  }

  minimizeChat(): void {
    this.isMinimized.set(true);
  }

  closeChat(): void {
    this.isOpen.set(false);
    this.isMinimized.set(false);
  }

  sendMessage(): void {
    const userMessage = this.userInput().trim();
    if (!userMessage || this.isLoading()) {
      return;
    }

    // Adiciona mensagem do usuário
    this.messages.update(msgs => [...msgs, {
      role: 'user',
      content: userMessage,
      timestamp: new Date()
    }]);

    this.userInput.set('');
    this.isLoading.set(true);

    // Cria prompt especializado
    const systemPrompt = `Você é um assistente especializado em assessoria de investimentos e gestão financeira para o sistema BartoFinance. 

Seu papel é ajudar assessores de investimento com:
- Orientações sobre perfis de investidores (Conservador, Moderado, Agressivo)
- Sugestões de estratégias de diversificação
- Explicações sobre tipos de produtos financeiros (CDB, Tesouro Direto, Ações, Fundos, Criptomoedas)
- Dicas de gestão de carteiras
- Interpretação de rentabilidade e metas
- Melhores práticas de assessoria financeira
- Funcionalidades do sistema BartoFinance

Sempre responda de forma profissional, clara e objetiva. Use no máximo 150 palavras por resposta.

Pergunta do assessor: ${userMessage}`;

    // Envia para a IA
    this.aiService.gerarInsight({ prompt: systemPrompt }).subscribe({
      next: (response) => {
        if (response.sucesso && response.data?.insight) {
          const insightText = response.data.insight || 'Desculpe, não consegui gerar uma resposta.';
          this.messages.update(msgs => [...msgs, {
            role: 'assistant',
            content: insightText,
            timestamp: new Date()
          }]);
        } else {
          this.messages.update(msgs => [...msgs, {
            role: 'assistant',
            content: '⚠️ Não foi possível gerar uma resposta. Por favor, tente reformular sua pergunta.',
            timestamp: new Date()
          }]);
        }
        this.isLoading.set(false);
        // Scroll para o final
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (error) => {
        console.error('Erro ao chamar IA:', error);
        this.messages.update(msgs => [...msgs, {
          role: 'assistant',
          content: '⚠️ Desculpe, ocorreu um erro ao processar sua pergunta. Por favor, tente novamente.',
          timestamp: new Date()
        }]);
        this.isLoading.set(false);
        setTimeout(() => this.scrollToBottom(), 100);
      }
    });

    // Scroll para o final
    setTimeout(() => this.scrollToBottom(), 100);
  }

  private scrollToBottom(): void {
    const chatMessages = document.querySelector('.chat-messages');
    if (chatMessages) {
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  }

  clearChat(): void {
    this.messages.set([{
      role: 'assistant',
      content: 'Conversa limpa. Como posso ajudá-lo?',
      timestamp: new Date()
    }]);
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }
}

