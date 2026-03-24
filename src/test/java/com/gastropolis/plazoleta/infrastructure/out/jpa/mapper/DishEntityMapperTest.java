package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IDishEntityMapperImpl.class})
class DishEntityMapperTest {

    @Autowired
    private IDishEntityMapper mapper;

    private DishModel dishModel;
    private DishEntity dishEntity;

    @BeforeEach
    void setUp() {
        dishModel = new DishModel(1L, "Hamburguesa", "Con papas", 15000, "http://img.png", true, 2L, 1L);

        dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Hamburguesa");
        dishEntity.setDescription("Con papas");
        dishEntity.setPrice(15000);
        dishEntity.setImageUrl("http://img.png");
        dishEntity.setActive(true);
        dishEntity.setCategoryId(2L);
        dishEntity.setRestaurantId(1L);
    }

    @Test
    void toEntity_mapsFieldsIgnoringCategoryAndRestaurant() {
        DishEntity result = mapper.toEntity(dishModel);

        assertNotNull(result);
        assertEquals("Hamburguesa", result.getName());
        assertEquals("Con papas", result.getDescription());
        assertEquals(15000, result.getPrice());
        assertEquals("http://img.png", result.getImageUrl());
        assertTrue(result.getActive());
        assertNull(result.getCategory());
        assertNull(result.getRestaurant());
    }

    @Test
    void toEntity_withNullModel_returnsNull() {
        DishEntity result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toModel_mapsAllFieldsFromEntity() {
        DishModel result = mapper.toModel(dishEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hamburguesa", result.getName());
        assertEquals("Con papas", result.getDescription());
        assertEquals(15000, result.getPrice());
        assertEquals("http://img.png", result.getImageUrl());
        assertTrue(result.getActive());
        assertEquals(2L, result.getCategoryId());
        assertEquals(1L, result.getRestaurantId());
    }

    @Test
    void toModel_withNullEntity_returnsNull() {
        DishModel result = mapper.toModel(null);
        assertNull(result);
    }

    @Test
    void toModelList_mapsListOfEntities() {
        DishEntity entity2 = new DishEntity();
        entity2.setId(2L);
        entity2.setName("Pizza");
        entity2.setPrice(20000);
        entity2.setActive(true);

        List<DishModel> result = mapper.toModelList(List.of(dishEntity, entity2));

        assertEquals(2, result.size());
        assertEquals("Hamburguesa", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
    }

    @Test
    void toModelList_withNullList_returnsNull() {
        List<DishModel> result = mapper.toModelList(null);
        assertNull(result);
    }
}
