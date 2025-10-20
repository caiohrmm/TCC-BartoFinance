package com.bartofinance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta genérica da API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private Boolean sucesso;
    
    private String mensagem;
    
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Cria uma resposta de sucesso
     */
    public static <T> ApiResponse<T> success(String mensagem, T data) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .mensagem(mensagem)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Cria uma resposta de sucesso sem dados
     */
    public static <T> ApiResponse<T> success(String mensagem) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Cria uma resposta de erro
     */
    public static <T> ApiResponse<T> error(String mensagem) {
        return ApiResponse.<T>builder()
                .sucesso(false)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

