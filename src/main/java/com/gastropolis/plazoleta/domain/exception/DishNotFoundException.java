package com.gastropolis.plazoleta.domain.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super(message);
    }
    public DishNotFoundException() {
        super("Plato no encontrado");
    }
}
