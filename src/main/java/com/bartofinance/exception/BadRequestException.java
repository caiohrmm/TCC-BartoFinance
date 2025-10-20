package com.bartofinance.exception;

/**
 * Exceção lançada quando a requisição é inválida
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}

