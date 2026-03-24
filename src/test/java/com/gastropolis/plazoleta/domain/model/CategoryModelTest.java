package com.gastropolis.plazoleta.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    void noArgsConstructor_createsInstanceWithNullFields() {
        CategoryModel category = new CategoryModel();

        assertNull(category.getId());
        assertNull(category.getName());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        CategoryModel category = new CategoryModel(1L, "Entradas");

        assertEquals(1L, category.getId());
        assertEquals("Entradas", category.getName());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        CategoryModel category = new CategoryModel();
        category.setId(2L);
        category.setName("Postres");

        assertEquals(2L, category.getId());
        assertEquals("Postres", category.getName());
    }

    @Test
    void getters_returnCorrectValues() {
        CategoryModel category = new CategoryModel(5L, "Bebidas");

        assertEquals(5L, category.getId());
        assertEquals("Bebidas", category.getName());
    }
}
