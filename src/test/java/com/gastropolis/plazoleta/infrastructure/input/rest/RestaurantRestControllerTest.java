package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateEmployeeRequestDto;
import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.application.handler.IRestaurantHandler;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRestControllerTest {

    @Mock
    private IRestaurantHandler restaurantHandler;

    @InjectMocks
    private RestaurantRestController restaurantRestController;

    private CreateRestaurantRequestDto createRestaurantDto;
    private RestaurantResponseDto restaurantResponseDto;

    @BeforeEach
    void setUp() {
        createRestaurantDto = new CreateRestaurantRequestDto();
        createRestaurantDto.setName("Burger House");
        createRestaurantDto.setNit("123456789");
        createRestaurantDto.setAddress("Calle 10");
        createRestaurantDto.setPhone("+573001234567");
        createRestaurantDto.setLogoUrl("http://logo.png");
        createRestaurantDto.setOwnerId(20L);

        restaurantResponseDto = new RestaurantResponseDto("Burger House", "http://logo.png");
    }

    @Test
    void createRestaurant_delegatesToHandlerAndReturnsCreated() {
        String token = "Bearer mytoken";
        when(restaurantHandler.createRestaurant(createRestaurantDto, token)).thenReturn(restaurantResponseDto);

        ResponseEntity<ApiResponse<RestaurantResponseDto>> response =
                restaurantRestController.createRestaurant(createRestaurantDto, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurante creado exitosamente", response.getBody().getMessage());
        assertEquals(restaurantResponseDto, response.getBody().getData());
        verify(restaurantHandler).createRestaurant(createRestaurantDto, token);
    }

    @Test
    void listRestaurants_delegatesToHandlerAndReturnsOk() {
        Page<RestaurantResponseDto> page = new PageImpl<>(List.of(restaurantResponseDto));
        when(restaurantHandler.listRestaurants(0, 10)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<RestaurantResponseDto>>> response =
                restaurantRestController.listRestaurants(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurantes obtenidos exitosamente", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().getContent().size());
        verify(restaurantHandler).listRestaurants(0, 10);
    }

    @Test
    void listRestaurants_withCustomPageAndSize_delegatesCorrectly() {
        Page<RestaurantResponseDto> page = new PageImpl<>(List.of());
        when(restaurantHandler.listRestaurants(2, 5)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<RestaurantResponseDto>>> response =
                restaurantRestController.listRestaurants(2, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(restaurantHandler).listRestaurants(2, 5);
    }

    @Test
    void createEmployee_delegatesToHandlerAndReturnsCreated() {
        String token = "Bearer mytoken";
        CreateEmployeeRequestDto dto = new CreateEmployeeRequestDto(
                "Juan", "Perez", "123456", "+573001111111",
                LocalDate.of(1990, 1, 1), "juan@mail.com", "secret123"
        );

        doNothing().when(restaurantHandler).createEmployee(dto, token);

        ResponseEntity<ApiResponse<String>> response =
                restaurantRestController.createEmployee(dto, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Empleado creado y asignado al restaurante exitosamente", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(restaurantHandler).createEmployee(dto, token);
    }
}
