package com.gastropolis.plazoleta.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException() {
        super("Pedido no encontrado");
    }
}
