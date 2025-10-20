package com.bartofinance.exception;

/**
 * Exceção lançada quando há falha de autenticação
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}

