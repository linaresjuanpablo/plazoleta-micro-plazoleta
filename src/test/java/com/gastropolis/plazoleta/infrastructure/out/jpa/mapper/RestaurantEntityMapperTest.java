package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IRestaurantEntityMapperImpl.class})
class RestaurantEntityMapperTest {

    @Autowired
    private IRestaurantEntityMapper mapper;

    private RestaurantModel restaurantModel;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantModel = new RestaurantModel(1L, "Burger House", "123456789",
                "Calle 10", "+573001234567", "http://logo.png", 20L);

        restaurantEntity = new RestaurantEntity(1L, "Burger House", "123456789",
                "Calle 10", "+573001234567", "http://logo.png", 20L);
    }

    @Test
    void toEntity_mapsAllFieldsFromModel() {
        RestaurantEntity result = mapper.toEntity(restaurantModel);

        assertNotNull(result);
        assertEquals("Burger House", result.getName());
        assertEquals("123456789", result.getNit());
        assertEquals("Calle 10", result.getAddress());
        assertEquals("+573001234567", result.getPhone());
        assertEquals("http://logo.png", result.getLogoUrl());
        assertEquals(20L, result.getOwnerId());
    }

    @Test
    void toEntity_withNullModel_returnsNull() {
        RestaurantEntity result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toModel_mapsAllFieldsFromEntity() {
        RestaurantModel result = mapper.toModel(restaurantEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Burger House", result.getName());
        assertEquals("123456789", result.getNit());
        assertEquals("Calle 10", result.getAddress());
        assertEquals("+573001234567", result.getPhone());
        assertEquals("http://logo.png", result.getLogoUrl());
        assertEquals(20L, result.getOwnerId());
    }

    @Test
    void toModel_withNullEntity_returnsNull() {
        RestaurantModel result = mapper.toModel(null);
        assertNull(result);
    }

    @Test
    void toModelList_mapsListOfEntities() {
        RestaurantEntity entity2 = new RestaurantEntity(2L, "Pizza Place", "987654321",
                "Calle 20", "+573009876543", "http://logo2.png", 30L);

        List<RestaurantModel> result = mapper.toModelList(List.of(restaurantEntity, entity2));

        assertEquals(2, result.size());
        assertEquals("Burger House", result.get(0).getName());
        assertEquals("Pizza Place", result.get(1).getName());
    }

    @Test
    void toModelList_withNullList_returnsNull() {
        List<RestaurantModel> result = mapper.toModelList(null);
        assertNull(result);
    }
}
