package com.gastropolis.plazoleta.infrastructure.out.feign.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FeignDtoTest {

    // ---- UserFeignResponseDto ----

    @Test
    void userFeignResponseDto_noArgsConstructorAndSetters() {
        UserFeignResponseDto dto = new UserFeignResponseDto();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setDni("123456");
        dto.setPhone("+573001234567");
        dto.setEmail("juan@email.com");
        dto.setRoleName("CLIENTE");

        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getName());
        assertEquals("Perez", dto.getLastName());
        assertEquals("123456", dto.getDni());
        assertEquals("+573001234567", dto.getPhone());
        assertEquals("juan@email.com", dto.getEmail());
        assertEquals("CLIENTE", dto.getRoleName());
    }

    @Test
    void userFeignResponseDto_allArgsConstructor() {
        UserFeignResponseDto dto = new UserFeignResponseDto(
                2L, "Ana", "Lopez", "654321", "+573002222222", "ana@email.com", "EMPLEADO");

        assertEquals(2L, dto.getId());
        assertEquals("Ana", dto.getName());
        assertEquals("EMPLEADO", dto.getRoleName());
    }

    // ---- CreateEmployeeFeignRequestDto ----

    @Test
    void createEmployeeFeignRequestDto_noArgsConstructorAndSetters() {
        CreateEmployeeFeignRequestDto dto = new CreateEmployeeFeignRequestDto();
        dto.setName("Carlos");
        dto.setLastName("Gomez");
        dto.setDni("789012");
        dto.setPhone("+573003333333");
        dto.setBirthDate(LocalDate.of(1990, 1, 15));
        dto.setEmail("carlos@email.com");
        dto.setPassword("pass123");

        assertEquals("Carlos", dto.getName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals("789012", dto.getDni());
        assertEquals("+573003333333", dto.getPhone());
        assertEquals(LocalDate.of(1990, 1, 15), dto.getBirthDate());
        assertEquals("carlos@email.com", dto.getEmail());
        assertEquals("pass123", dto.getPassword());
    }

    @Test
    void createEmployeeFeignRequestDto_allArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1985, 6, 20);
        CreateEmployeeFeignRequestDto dto = new CreateEmployeeFeignRequestDto(
                "Maria", "Torres", "111222", "+573004444444", birthDate, "maria@email.com", "secret");

        assertEquals("Maria", dto.getName());
        assertEquals(birthDate, dto.getBirthDate());
    }

    // ---- CreateOrderLogFeignDto ----

    @Test
    void createOrderLogFeignDto_noArgsConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        CreateOrderLogFeignDto dto = new CreateOrderLogFeignDto();
        dto.setOrderId(1L);
        dto.setClientId(5L);
        dto.setEmployeeId(7L);
        dto.setPreviousStatus("PENDIENTE");
        dto.setNewStatus("EN_PREPARACION");
        dto.setTimestamp(now);

        assertEquals(1L, dto.getOrderId());
        assertEquals(5L, dto.getClientId());
        assertEquals(7L, dto.getEmployeeId());
        assertEquals("PENDIENTE", dto.getPreviousStatus());
        assertEquals("EN_PREPARACION", dto.getNewStatus());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void createOrderLogFeignDto_allArgsConstructor() {
        LocalDateTime ts = LocalDateTime.now();
        CreateOrderLogFeignDto dto = new CreateOrderLogFeignDto(1L, 5L, 7L, "PENDIENTE", "EN_PREPARACION", ts);

        assertEquals(1L, dto.getOrderId());
        assertEquals(ts, dto.getTimestamp());
    }

    // ---- SendSmsFeignDto ----

    @Test
    void sendSmsFeignDto_noArgsConstructorAndSetters() {
        SendSmsFeignDto dto = new SendSmsFeignDto();
        dto.setPhoneNumber("+573001234567");
        dto.setMessage("Mensaje de prueba");

        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("Mensaje de prueba", dto.getMessage());
    }

    @Test
    void sendSmsFeignDto_allArgsConstructor() {
        SendSmsFeignDto dto = new SendSmsFeignDto("+573001234567", "PIN: 1234");

        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("PIN: 1234", dto.getMessage());
    }
}
