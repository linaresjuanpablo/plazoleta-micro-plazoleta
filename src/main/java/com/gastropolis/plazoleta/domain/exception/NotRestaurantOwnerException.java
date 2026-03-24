package com.gastropolis.plazoleta.domain.exception;

public class NotRestaurantOwnerException extends RuntimeException {
    public NotRestaurantOwnerException(String message) {
        super(message);
    }
    public NotRestaurantOwnerException() {
        super("No eres el propietario de este restaurante");
    }
}
