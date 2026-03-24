package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateEmployeeRequestDto;
import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IRestaurantResponseMapper;
import com.gastropolis.plazoleta.domain.api.IRestaurantServicePort;
import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private CreateRestaurantRequestDto createRestaurantDto;
    private RestaurantModel restaurantModel;
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

        restaurantModel = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 20L);
        restaurantResponseDto = new RestaurantResponseDto("Burger House", "http://logo.png");
    }

    // ---- createRestaurant ----

    @Test
    void createRestaurant_mapsAndDelegatesAndReturnsResponseDto() {
        when(restaurantRequestMapper.toRestaurantModel(createRestaurantDto)).thenReturn(restaurantModel);
        when(restaurantServicePort.createRestaurant(restaurantModel, "Bearer token")).thenReturn(restaurantModel);
        when(restaurantResponseMapper.toRestaurantResponseDto(restaurantModel)).thenReturn(restaurantResponseDto);

        RestaurantResponseDto result = restaurantHandler.createRestaurant(createRestaurantDto, "Bearer token");

        assertNotNull(result);
        assertEquals("Burger House", result.getName());
        verify(restaurantRequestMapper).toRestaurantModel(createRestaurantDto);
        verify(restaurantServicePort).createRestaurant(restaurantModel, "Bearer token");
        verify(restaurantResponseMapper).toRestaurantResponseDto(restaurantModel);
    }

    // ---- listRestaurants ----

    @Test
    void listRestaurants_mapsPageAndReturnsResponseDtos() {
        RestaurantModel model2 = new RestaurantModel(2L, "Pizza Place", "987654321", "Calle 20", "+573009876543", "http://logo2.png", 30L);
        RestaurantResponseDto dto2 = new RestaurantResponseDto("Pizza Place", "http://logo2.png");

        Page<RestaurantModel> modelPage = new PageImpl<>(List.of(restaurantModel, model2));
        when(restaurantServicePort.listRestaurants(0, 10)).thenReturn(modelPage);
        when(restaurantResponseMapper.toRestaurantResponseDto(restaurantModel)).thenReturn(restaurantResponseDto);
        when(restaurantResponseMapper.toRestaurantResponseDto(model2)).thenReturn(dto2);

        Page<RestaurantResponseDto> result = restaurantHandler.listRestaurants(0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("Burger House", result.getContent().get(0).getName());
        assertEquals("Pizza Place", result.getContent().get(1).getName());
        verify(restaurantServicePort).listRestaurants(0, 10);
    }

    // ---- createEmployee with "Bearer " prefix ----

    @Test
    void createEmployee_withBearerPrefix_stripsPrefix_extractsOwnerIdAndDelegates() {
        String fullToken = "Bearer myRawToken";
        String rawToken = "myRawToken";

        CreateEmployeeRequestDto dto = new CreateEmployeeRequestDto(
                "Juan", "Perez", "123456", "+573001111111", LocalDate.of(1990, 1, 1), "juan@mail.com", "secret123"
        );

        when(jwtService.getUserIdFromToken(rawToken)).thenReturn(20L);

        restaurantHandler.createEmployee(dto, fullToken);

        verify(jwtService).getUserIdFromToken(rawToken);

        ArgumentCaptor<CreateEmployeeModel> modelCaptor = ArgumentCaptor.forClass(CreateEmployeeModel.class);
        verify(restaurantServicePort).createEmployee(modelCaptor.capture(), eq(20L), eq(fullToken));

        CreateEmployeeModel captured = modelCaptor.getValue();
        assertEquals("Juan", captured.getName());
        assertEquals("Perez", captured.getLastName());
        assertEquals("123456", captured.getDni());
        assertEquals("+573001111111", captured.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), captured.getBirthDate());
        assertEquals("juan@mail.com", captured.getEmail());
        assertEquals("secret123", captured.getPassword());
    }

    @Test
    void createEmployee_withoutBearerPrefix_usesTokenAsIs_extractsOwnerIdAndDelegates() {
        String rawToken = "myRawToken";

        CreateEmployeeRequestDto dto = new CreateEmployeeRequestDto(
                "Ana", "Lopez", "654321", "+573002222222", LocalDate.of(1995, 5, 15), "ana@mail.com", "pass456"
        );

        when(jwtService.getUserIdFromToken(rawToken)).thenReturn(30L);

        restaurantHandler.createEmployee(dto, rawToken);

        verify(jwtService).getUserIdFromToken(rawToken);

        ArgumentCaptor<CreateEmployeeModel> modelCaptor = ArgumentCaptor.forClass(CreateEmployeeModel.class);
        verify(restaurantServicePort).createEmployee(modelCaptor.capture(), eq(30L), eq(rawToken));

        CreateEmployeeModel captured = modelCaptor.getValue();
        assertEquals("Ana", captured.getName());
        assertEquals("Lopez", captured.getLastName());
    }
}
