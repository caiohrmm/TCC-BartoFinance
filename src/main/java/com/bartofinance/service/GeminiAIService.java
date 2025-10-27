package com.bartofinance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Serviço de integração com Google Gemini AI usando REST API
 */
@Service
@Slf4j
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-1.5-flash}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    /**
     * Gera conteúdo usando Gemini AI via REST API
     */
    public String generateContent(String prompt) {
        try {
            log.info("Enviando requisição para Gemini AI via REST...");
            
            // Verificar se a API key é válida
            if (apiKey == null || apiKey.isEmpty()) {
                log.warn("API key não configurada, usando fallback");
                return generateFallbackResponse(prompt);
            }
            
            // Construir URL com API key
            String url = GEMINI_API_URL + model + ":generateContent?key=" + apiKey;
            
            // Construir body da requisição
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);
            
            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(part));
            
            requestBody.put("contents", Collections.singletonList(content));
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Fazer requisição
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                
                // Extrair texto da resposta
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> candidate = candidates.get(0);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> contentResponse = (Map<String, Object>) candidate.get("content");
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentResponse.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        log.info("Resposta recebida do Gemini AI com sucesso");
                        return text;
                    }
                }
            }
            
            log.warn("Resposta inválida do Gemini, usando fallback");
            return generateFallbackResponse(prompt);
            
        } catch (Exception e) {
            log.error("Erro ao se comunicar com Gemini AI: {}", e.getMessage());
            log.info("Fallback automático ativado devido ao erro");
            return generateFallbackResponse(prompt);
        }
    }

    /**
     * Gera resposta de fallback quando a IA não está disponível
     */
    private String generateFallbackResponse(String prompt) {
        log.info("Usando resposta de fallback para: {}", prompt);
        
        String lowerPrompt = prompt.toLowerCase().trim();
        
        // Saudações e cumprimentos
        if (lowerPrompt.matches("^(oi|olá|ola|hello|hi|bom dia|boa tarde|boa noite)$") ||
            lowerPrompt.contains("como vai") || lowerPrompt.contains("tudo bem")) {
            return "👋 **Olá! Bem-vindo ao BartoFinance!**\n\n" +
                   "Sou seu assistente virtual especializado em assessoria de investimentos.\n\n" +
                   "Posso ajudar com:\n" +
                   "• 📊 Análise de perfis de investidores\n" +
                   "• 🎯 Estratégias de diversificação\n" +
                   "• 📈 Definição de metas de rentabilidade\n" +
                   "• 🏦 Interpretação de produtos financeiros\n" +
                   "• 💼 Gestão de carteiras\n\n" +
                   "💡 **Como posso auxiliá-lo hoje?**";
        }
        
        if (lowerPrompt.contains("perfil") || lowerPrompt.contains("investidor")) {
            return "📊 **Análise de Perfil de Investidor**\n\n" +
                   "**Conservador**: Prioriza segurança, prefere renda fixa (CDB, Tesouro Direto, LCI/LCA)\n" +
                   "**Moderado**: Equilibra segurança e rentabilidade, diversifica entre renda fixa e variável\n" +
                   "**Agressivo**: Aceita maior risco por maior potencial de retorno, foca em ações e fundos\n\n" +
                   "💡 **Dica**: Analise a tolerância ao risco, objetivos e horizonte de investimento do cliente.";
        }
        
        if (lowerPrompt.contains("carteira") || lowerPrompt.contains("diversificação")) {
            return "🎯 **Estratégia de Diversificação**\n\n" +
                   "**Renda Fixa**: 40-60% (CDB, LCI, LCA, Tesouro Direto)\n" +
                   "**Ações**: 20-40% (empresas sólidas, dividendos)\n" +
                   "**Fundos**: 10-20% (multimercado, imobiliário)\n" +
                   "**Reserva**: 5-10% (emergência)\n\n" +
                   "💡 **Dica**: Ajuste conforme o perfil e objetivos do investidor.";
        }
        
        if (lowerPrompt.contains("rentabilidade") || lowerPrompt.contains("meta")) {
            return "📈 **Metas de Rentabilidade Realistas**\n\n" +
                   "**Inflação + 3-5%**: Objetivo conservador\n" +
                   "**CDI + 2-4%**: Renda fixa\n" +
                   "**IPCA + 4-6%**: Tesouro Direto\n" +
                   "**10-15% ao ano**: Ações (longo prazo)\n\n" +
                   "💡 **Dica**: Considere sempre o risco e horizonte de investimento.";
        }
        
        if (lowerPrompt.contains("cdb") || lowerPrompt.contains("tesouro")) {
            return "🏦 **Renda Fixa - Opções Seguras**\n\n" +
                   "**CDB**: Certificado de Depósito Bancário, liquidez diária\n" +
                   "**Tesouro Direto**: Títulos públicos, mais seguro\n" +
                   "**LCI/LCA**: Isentos de IR, boa para perfil conservador\n\n" +
                   "💡 **Dica**: Compare sempre a rentabilidade líquida após impostos.";
        }
        
        if (lowerPrompt.contains("ação") || lowerPrompt.contains("ações")) {
            return "📊 **Investimento em Ações**\n\n" +
                   "**Blue Chips**: Empresas grandes e estáveis (Vale, Petrobras)\n" +
                   "**Small Caps**: Empresas menores, maior potencial de crescimento\n" +
                   "**Dividendos**: Ações que pagam proventos regularmente\n\n" +
                   "💡 **Dica**: Diversifique por setores e analise fundamentos.";
        }
        
        if (lowerPrompt.contains("fundo") || lowerPrompt.contains("fundos")) {
            return "🏛️ **Fundos de Investimento**\n\n" +
                   "**Multimercado**: Diversificam entre renda fixa e variável\n" +
                   "**Imobiliário**: Investem em imóveis e recebem aluguéis\n" +
                   "**Ações**: Focam em carteira de ações\n" +
                   "**Renda Fixa**: Investem em títulos de renda fixa\n\n" +
                   "💡 **Dica**: Verifique a taxa de administração e histórico de performance.";
        }
        
        if (lowerPrompt.contains("risco") || lowerPrompt.contains("segurança")) {
            return "⚖️ **Gestão de Risco**\n\n" +
                   "**Baixo Risco**: CDB, Tesouro Direto, LCI/LCA\n" +
                   "**Médio Risco**: Fundos multimercado, ações blue chips\n" +
                   "**Alto Risco**: Ações small caps, fundos de ações\n\n" +
                   "💡 **Dica**: Diversifique para reduzir o risco total da carteira.";
        }
        
        if (lowerPrompt.contains("ajuda") || lowerPrompt.contains("help")) {
            return "🆘 **Como Posso Ajudar?**\n\n" +
                   "**Para Investidores:**\n" +
                   "• Como definir meu perfil de investidor?\n" +
                   "• Quais produtos são adequados para mim?\n" +
                   "• Como diversificar minha carteira?\n\n" +
                   "**Para Assessores:**\n" +
                   "• Como analisar o perfil do cliente?\n" +
                   "• Estratégias de diversificação\n" +
                   "• Definição de metas realistas\n\n" +
                   "💡 **Faça uma pergunta específica!**";
        }
        
        // Resposta padrão para perguntas não reconhecidas
        return "🤖 **Assistente BartoFinance**\n\n" +
               "Entendi sua pergunta! Posso ajudar com:\n\n" +
               "• 📊 **Perfis de investidor** (Conservador, Moderado, Agressivo)\n" +
               "• 🎯 **Diversificação** de carteiras\n" +
               "• 📈 **Metas de rentabilidade** realistas\n" +
               "• 🏦 **Produtos financeiros** (CDB, Tesouro, Ações, Fundos)\n" +
               "• ⚖️ **Gestão de risco**\n\n" +
               "💡 **Seja mais específico na sua pergunta para uma resposta mais detalhada!**";
    }

    /**
     * Gera análise de perfil de investidor
     */
    public String analisarPerfilInvestidor(String nome, String perfil, Double rendaMensal, Double patrimonioAtual) {
        String prompt = String.format(
            "Você é um assessor de investimentos experiente. Analise o seguinte perfil de investidor e forneça uma breve recomendação (máximo 150 palavras):\n\n" +
            "Nome: %s\n" +
            "Perfil de Investidor: %s\n" +
            "Renda Mensal: R$ %.2f\n" +
            "Patrimônio Atual: R$ %.2f\n\n" +
            "Forneça recomendações práticas e relevantes para este perfil.",
            nome, perfil, rendaMensal, patrimonioAtual
        );

        return generateContent(prompt);
    }

    /**
     * Gera análise de carteira de investimentos
     */
    public String analisarCarteira(String nomeCarteira, String tipoCarteira, String riscoCarteira, 
                                   Double valorTotal, Double rentabilidadeAtual, Double metaRentabilidade) {
        String prompt = String.format(
            "Você é um assessor de investimentos experiente. Analise a seguinte carteira e forneça insights (máximo 150 palavras):\n\n" +
            "Carteira: %s\n" +
            "Tipo: %s\n" +
            "Risco: %s\n" +
            "Valor Total: R$ %.2f\n" +
            "Rentabilidade Atual: %.2f%%\n" +
            "Meta de Rentabilidade: %.2f%%\n\n" +
            "Analise se a carteira está performando bem e sugira melhorias se necessário.",
            nomeCarteira, tipoCarteira, riscoCarteira, valorTotal, rentabilidadeAtual, metaRentabilidade
        );

        return generateContent(prompt);
    }

    /**
     * Gera insights sobre uma aplicação específica
     */
    public String analisarAplicacao(String tipoProduto, String codigoAtivo, Double valorAplicado, 
                                    Double rentabilidadeAtual, String status) {
        String prompt = String.format(
            "Você é um assessor de investimentos. Analise esta aplicação financeira (máximo 100 palavras):\n\n" +
            "Tipo: %s\n" +
            "Ativo: %s\n" +
            "Valor Aplicado: R$ %.2f\n" +
            "Rentabilidade: %.2f%%\n" +
            "Status: %s\n\n" +
            "Forneça uma breve análise sobre esta aplicação.",
            tipoProduto, codigoAtivo, valorAplicado, rentabilidadeAtual, status
        );

        return generateContent(prompt);
    }

    /**
     * Gera sugestão de diversificação de carteira
     */
    public String sugerirDiversificacao(String perfilInvestidor, Double valorDisponivel) {
        String prompt = String.format(
            "Como assessor de investimentos, sugira uma estratégia de diversificação (máximo 150 palavras) para:\n\n" +
            "Perfil do Investidor: %s\n" +
            "Valor Disponível para Investir: R$ %.2f\n\n" +
            "Sugira uma alocação percentual entre diferentes tipos de ativos (Renda Fixa, Ações, Fundos, etc).",
            perfilInvestidor, valorDisponivel
        );

        return generateContent(prompt);
    }
}
