package com.bartofinance.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Serviço de integração com Google Gemini AI usando SDK oficial
 */
@Service
@Slf4j
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-2.0-flash-exp}")
    private String model;

    private Client getClient() {
        // Configurar API key via variável de ambiente
        System.setProperty("GOOGLE_API_KEY", apiKey);
        return new Client();
    }

    /**
     * Gera conteúdo usando Gemini AI
     */
    public String generateContent(String prompt) {
        try {
            Client client = getClient();
            
            log.info("Enviando requisição para Gemini AI...");
            GenerateContentResponse response = client.models.generateContent(
                model,
                prompt,
                null
            );
            
            String generatedText = response.text();
            log.info("Resposta recebida do Gemini AI com sucesso");
            return generatedText;
            
        } catch (Exception e) {
            log.error("Erro ao se comunicar com Gemini AI", e);
            return "Erro ao processar resposta da IA: " + e.getMessage();
        }
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
