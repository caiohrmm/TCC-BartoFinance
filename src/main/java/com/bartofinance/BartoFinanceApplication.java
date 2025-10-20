package com.bartofinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * BartoFinance - Sistema de Assessoria de Investimentos
 * 
 * Sistema completo para gerenciamento de investidores, aplicações e relatórios
 * com insights gerados por IA e autenticação JWT para assessores.
 * 
 * @author BartoFinance Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableMongoAuditing
public class BartoFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BartoFinanceApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   BartoFinance Backend Started Successfully! 🚀         ║");
        System.out.println("║   API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}

