package com.gastropolis.plazoleta.application.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDishRequestDtoTest {

    @Test
    void settersAndGetters_workCorrectly() {
        OrderDishRequestDto dto = new OrderDishRequestDto();
        dto.setDishId(10L);
        dto.setQuantity(3);

        assertEquals(10L, dto.getDishId());
        assertEquals(3, dto.getQuantity());
    }

    @Test
    void defaultValues_areNull() {
        OrderDishRequestDto dto = new OrderDishRequestDto();

        assertNull(dto.getDishId());
        assertNull(dto.getQuantity());
    }

    @Test
    void setDishId_updatesValue() {
        OrderDishRequestDto dto = new OrderDishRequestDto();
        dto.setDishId(99L);
        assertEquals(99L, dto.getDishId());
    }

    @Test
    void setQuantity_updatesValue() {
        OrderDishRequestDto dto = new OrderDishRequestDto();
        dto.setQuantity(5);
        assertEquals(5, dto.getQuantity());
    }
}
