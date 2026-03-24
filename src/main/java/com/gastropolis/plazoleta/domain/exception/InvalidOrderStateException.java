package com.gastropolis.plazoleta.domain.exception;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
    public InvalidOrderStateException() {
        super("El estado del pedido no permite esta operación");
    }
}
