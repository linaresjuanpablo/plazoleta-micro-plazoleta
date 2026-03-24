package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
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
        IRestaurantRequestMapperImpl.class,
        IRestaurantResponseMapperImpl.class
})
class RestaurantMapperTest {

    @Autowired
    private IRestaurantRequestMapper requestMapper;

    @Autowired
    private IRestaurantResponseMapper responseMapper;

    private CreateRestaurantRequestDto requestDto;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        requestDto = new CreateRestaurantRequestDto();
        requestDto.setName("Burger House");
        requestDto.setNit("123456789");
        requestDto.setAddress("Calle 10");
        requestDto.setPhone("+573001234567");
        requestDto.setLogoUrl("http://logo.png");
        requestDto.setOwnerId(20L);

        restaurantModel = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10",
                "+573001234567", "http://logo.png", 20L);
    }

    // ---- IRestaurantRequestMapper ----

    @Test
    void toRestaurantModel_mapsAllFieldsFromDto() {
        RestaurantModel result = requestMapper.toRestaurantModel(requestDto);

        assertNotNull(result);
        assertEquals("Burger House", result.getName());
        assertEquals("123456789", result.getNit());
        assertEquals("Calle 10", result.getAddress());
        assertEquals("+573001234567", result.getPhone());
        assertEquals("http://logo.png", result.getLogoUrl());
        assertEquals(20L, result.getOwnerId());
    }

    @Test
    void toRestaurantModel_withNullDto_returnsNull() {
        RestaurantModel result = requestMapper.toRestaurantModel(null);
        assertNull(result);
    }

    // ---- IRestaurantResponseMapper ----

    @Test
    void toRestaurantResponseDto_mapsNameAndLogoUrl() {
        RestaurantResponseDto result = responseMapper.toRestaurantResponseDto(restaurantModel);

        assertNotNull(result);
        assertEquals("Burger House", result.getName());
        assertEquals("http://logo.png", result.getLogoUrl());
    }

    @Test
    void toRestaurantResponseDto_withNullModel_returnsNull() {
        RestaurantResponseDto result = responseMapper.toRestaurantResponseDto(null);
        assertNull(result);
    }

    @Test
    void toRestaurantResponseDtoList_mapsList() {
        RestaurantModel model2 = new RestaurantModel(2L, "Pizza Place", "987654321", "Calle 20",
                "+573009876543", "http://logo2.png", 30L);

        List<RestaurantResponseDto> result = responseMapper.toRestaurantResponseDtoList(
                List.of(restaurantModel, model2));

        assertEquals(2, result.size());
        assertEquals("Burger House", result.get(0).getName());
        assertEquals("Pizza Place", result.get(1).getName());
    }

    @Test
    void toRestaurantResponseDtoList_withNullList_returnsNull() {
        List<RestaurantResponseDto> result = responseMapper.toRestaurantResponseDtoList(null);
        assertNull(result);
    }
}
