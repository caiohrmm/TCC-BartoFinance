package com.bartofinance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para resposta de erro detalhada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;
    
    private String erro;
    
    private String mensagem;
    
    private List<String> detalhes;
    
    private String path;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

