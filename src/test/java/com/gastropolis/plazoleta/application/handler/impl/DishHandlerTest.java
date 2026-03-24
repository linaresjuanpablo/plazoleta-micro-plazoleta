package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.application.mapper.IDishRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IDishResponseMapper;
import com.gastropolis.plazoleta.domain.api.IDishServicePort;
import com.gastropolis.plazoleta.domain.model.DishModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @Mock
    private IDishResponseMapper dishResponseMapper;

    @InjectMocks
    private DishHandler dishHandler;

    private DishModel dishModel;
    private DishResponseDto dishResponseDto;

    @BeforeEach
    void setUp() {
        dishModel = new DishModel(10L, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);
        dishResponseDto = new DishResponseDto(10L, "Hamburguesa", "Desc", 15000, "http://img.png", true);
    }

    // ---- createDish ----

    @Test
    void createDish_mapsRequestAndDelegatesAndReturnsResponseDto() {
        CreateDishRequestDto dto = new CreateDishRequestDto();
        dto.setName("Hamburguesa");
        dto.setDescription("Desc");
        dto.setPrice(15000);
        dto.setImageUrl("http://img.png");
        dto.setCategoryId(2L);
        dto.setRestaurantId(1L);

        when(dishRequestMapper.toDishModel(dto)).thenReturn(dishModel);
        when(dishServicePort.createDish(dishModel, 20L)).thenReturn(dishModel);
        when(dishResponseMapper.toDishResponseDto(dishModel)).thenReturn(dishResponseDto);

        DishResponseDto result = dishHandler.createDish(dto, 20L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Hamburguesa", result.getName());
        verify(dishRequestMapper).toDishModel(dto);
        verify(dishServicePort).createDish(dishModel, 20L);
        verify(dishResponseMapper).toDishResponseDto(dishModel);
    }

    // ---- updateDish ----

    @Test
    void updateDish_delegatesWithPriceAndDescriptionAndReturnsResponseDto() {
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setPrice(20000);
        dto.setDescription("New desc");

        DishModel updated = new DishModel(10L, "Hamburguesa", "New desc", 20000, "http://img.png", true, 2L, 1L);
        DishResponseDto updatedDto = new DishResponseDto(10L, "Hamburguesa", "New desc", 20000, "http://img.png", true);

        when(dishServicePort.updateDish(10L, 20000, "New desc", 20L)).thenReturn(updated);
        when(dishResponseMapper.toDishResponseDto(updated)).thenReturn(updatedDto);

        DishResponseDto result = dishHandler.updateDish(10L, dto, 20L);

        assertNotNull(result);
        assertEquals(20000, result.getPrice());
        assertEquals("New desc", result.getDescription());
        verify(dishServicePort).updateDish(10L, 20000, "New desc", 20L);
    }

    @Test
    void updateDish_withNullPriceAndDescription_delegatesNullValues() {
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setPrice(null);
        dto.setDescription(null);

        when(dishServicePort.updateDish(10L, null, null, 20L)).thenReturn(dishModel);
        when(dishResponseMapper.toDishResponseDto(dishModel)).thenReturn(dishResponseDto);

        DishResponseDto result = dishHandler.updateDish(10L, dto, 20L);

        assertNotNull(result);
        verify(dishServicePort).updateDish(10L, null, null, 20L);
    }

    // ---- toggleDishActive ----

    @Test
    void toggleDishActive_delegatesAndReturnsResponseDto() {
        DishModel toggled = new DishModel(10L, "Hamburguesa", "Desc", 15000, "http://img.png", false, 2L, 1L);
        DishResponseDto toggledDto = new DishResponseDto(10L, "Hamburguesa", "Desc", 15000, "http://img.png", false);

        when(dishServicePort.toggleDishActive(10L, 20L)).thenReturn(toggled);
        when(dishResponseMapper.toDishResponseDto(toggled)).thenReturn(toggledDto);

        DishResponseDto result = dishHandler.toggleDishActive(10L, 20L);

        assertNotNull(result);
        assertFalse(result.getActive());
        verify(dishServicePort).toggleDishActive(10L, 20L);
    }

    // ---- listDishesByRestaurant ----

    @Test
    void listDishesByRestaurant_withCategoryId_delegatesAndMapPage() {
        Page<DishModel> modelPage = new PageImpl<>(List.of(dishModel));
        when(dishServicePort.listDishesByRestaurant(1L, 2L, 0, 10)).thenReturn(modelPage);
        when(dishResponseMapper.toDishResponseDto(dishModel)).thenReturn(dishResponseDto);

        Page<DishResponseDto> result = dishHandler.listDishesByRestaurant(1L, 2L, 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Hamburguesa", result.getContent().get(0).getName());
        verify(dishServicePort).listDishesByRestaurant(1L, 2L, 0, 10);
    }

    @Test
    void listDishesByRestaurant_withNullCategoryId_delegatesNullAndMapPage() {
        Page<DishModel> modelPage = new PageImpl<>(List.of(dishModel));
        when(dishServicePort.listDishesByRestaurant(1L, null, 0, 10)).thenReturn(modelPage);
        when(dishResponseMapper.toDishResponseDto(dishModel)).thenReturn(dishResponseDto);

        Page<DishResponseDto> result = dishHandler.listDishesByRestaurant(1L, null, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(dishServicePort).listDishesByRestaurant(1L, null, 0, 10);
    }
}
