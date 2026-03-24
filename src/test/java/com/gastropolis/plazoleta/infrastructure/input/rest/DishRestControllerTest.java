package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.application.handler.IDishHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishRestControllerTest {

    @Mock
    private IDishHandler dishHandler;

    @InjectMocks
    private DishRestController dishRestController;

    private DishResponseDto dishResponseDto;
    private final Long userId = 5L;

    @BeforeEach
    void setUp() {
        dishResponseDto = new DishResponseDto(1L, "Hamburguesa", "Desc", 15000, "http://img.png", true);

        Authentication auth = mock(Authentication.class);
        when(auth.getDetails()).thenReturn(userId);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createDish_delegatesToHandlerWithAuthenticatedUserId_andReturnsCreated() {
        CreateDishRequestDto dto = new CreateDishRequestDto();
        when(dishHandler.createDish(dto, userId)).thenReturn(dishResponseDto);

        ResponseEntity<ApiResponse<DishResponseDto>> response = dishRestController.createDish(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Plato creado exitosamente", response.getBody().getMessage());
        assertEquals(dishResponseDto, response.getBody().getData());
        verify(dishHandler).createDish(dto, userId);
    }

    @Test
    void updateDish_delegatesToHandlerWithIdAndAuthenticatedUserId_andReturnsOk() {
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        when(dishHandler.updateDish(1L, dto, userId)).thenReturn(dishResponseDto);

        ResponseEntity<ApiResponse<DishResponseDto>> response = dishRestController.updateDish(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Plato actualizado exitosamente", response.getBody().getMessage());
        assertEquals(dishResponseDto, response.getBody().getData());
        verify(dishHandler).updateDish(1L, dto, userId);
    }

    @Test
    void toggleDishActive_delegatesToHandlerWithIdAndAuthenticatedUserId_andReturnsOk() {
        when(dishHandler.toggleDishActive(1L, userId)).thenReturn(dishResponseDto);

        ResponseEntity<ApiResponse<DishResponseDto>> response = dishRestController.toggleDishActive(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estado del plato actualizado exitosamente", response.getBody().getMessage());
        assertEquals(dishResponseDto, response.getBody().getData());
        verify(dishHandler).toggleDishActive(1L, userId);
    }

    /*@Test
    void listDishes_withCategoryId_delegatesToHandler_andReturnsOk() {
        Page<DishResponseDto> page = new PageImpl<>(List.of(dishResponseDto));
        when(dishHandler.listDishesByRestaurant(1L, 2L, 0, 10)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<DishResponseDto>>> response =
                dishRestController.listDishes(1L, 2L, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Platos obtenidos exitosamente", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().getContent().size());
        verify(dishHandler).listDishesByRestaurant(1L, 2L, 0, 10);
    }

    @Test
    void listDishes_withoutCategoryId_delegatesToHandlerWithNullCategory_andReturnsOk() {
        Page<DishResponseDto> page = new PageImpl<>(List.of());
        when(dishHandler.listDishesByRestaurant(1L, null, 0, 5)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<DishResponseDto>>> response =
                dishRestController.listDishes(1L, null, 0, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dishHandler).listDishesByRestaurant(1L, null, 0, 5);
    }*/
}
