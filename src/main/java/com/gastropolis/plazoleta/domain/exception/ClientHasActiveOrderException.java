package com.gastropolis.plazoleta.domain.exception;

public class ClientHasActiveOrderException extends RuntimeException {
    public ClientHasActiveOrderException(String message) {
        super(message);
    }
    public ClientHasActiveOrderException() {
        super("El cliente ya tiene un pedido activo");
    }
}
