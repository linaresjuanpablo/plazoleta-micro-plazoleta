package com.gastropolis.plazoleta.infrastructure.exceptionhandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionResponseTest {

    @Test
    void noArgsConstructor_createsInstanceWithNullMessage() {
        ExceptionResponse response = new ExceptionResponse();
        assertNull(response.getMessage());
    }

    @Test
    void allArgsConstructor_setsMessage() {
        ExceptionResponse response = new ExceptionResponse("Error ocurrido");
        assertEquals("Error ocurrido", response.getMessage());
    }

    @Test
    void setMessage_updatesMessage() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Nuevo mensaje");
        assertEquals("Nuevo mensaje", response.getMessage());
    }
}
