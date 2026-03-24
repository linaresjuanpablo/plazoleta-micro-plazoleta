package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.domain.model.DishModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        IDishRequestMapperImpl.class,
        IDishResponseMapperImpl.class
})
class DishMapperTest {

    @Autowired
    private IDishRequestMapper requestMapper;

    @Autowired
    private IDishResponseMapper responseMapper;

    private CreateDishRequestDto requestDto;
    private DishModel dishModel;

    @BeforeEach
    void setUp() {
        requestDto = new CreateDishRequestDto();
        requestDto.setName("Hamburguesa");
        requestDto.setDescription("Con papas fritas");
        requestDto.setPrice(15000);
        requestDto.setImageUrl("http://img.png");
        requestDto.setCategoryId(2L);
        requestDto.setRestaurantId(1L);

        dishModel = new DishModel(1L, "Hamburguesa", "Con papas fritas", 15000,
                "http://img.png", true, 2L, 1L);
    }

    // ---- IDishRequestMapper ----

    @Test
    void toDishModel_mapsAllFieldsFromDto() {
        DishModel result = requestMapper.toDishModel(requestDto);

        assertNotNull(result);
        assertEquals("Hamburguesa", result.getName());
        assertEquals("Con papas fritas", result.getDescription());
        assertEquals(15000, result.getPrice());
        assertEquals("http://img.png", result.getImageUrl());
        assertEquals(2L, result.getCategoryId());
        assertEquals(1L, result.getRestaurantId());
    }

    @Test
    void toDishModel_withNullDto_returnsNull() {
        DishModel result = requestMapper.toDishModel(null);
        assertNull(result);
    }

    // ---- IDishResponseMapper ----

    @Test
    void toDishResponseDto_mapsAllFields() {
        DishResponseDto result = responseMapper.toDishResponseDto(dishModel);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hamburguesa", result.getName());
        assertEquals("Con papas fritas", result.getDescription());
        assertEquals(15000, result.getPrice());
        assertEquals("http://img.png", result.getImageUrl());
        assertTrue(result.getActive());
    }

    @Test
    void toDishResponseDto_withNullModel_returnsNull() {
        DishResponseDto result = responseMapper.toDishResponseDto(null);
        assertNull(result);
    }

    @Test
    void toDishResponseDtoList_mapsList() {
        DishModel model2 = new DishModel(2L, "Pizza", "Italiana", 20000, "http://pizza.png", true, 3L, 1L);

        List<DishResponseDto> result = responseMapper.toDishResponseDtoList(List.of(dishModel, model2));

        assertEquals(2, result.size());
        assertEquals("Hamburguesa", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
    }

    @Test
    void toDishResponseDtoList_withNullList_returnsNull() {
        List<DishResponseDto> result = responseMapper.toDishResponseDtoList(null);
        assertNull(result);
    }
}
