package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IOrderEntityMapperImpl.class})
class OrderEntityMapperTest {

    @Autowired
    private IOrderEntityMapper mapper;

    private OrderEntity orderEntity;
    private OrderDishEntity orderDishEntity;

    @BeforeEach
    void setUp() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Burger House");

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(10L);
        dishEntity.setName("Hamburguesa");

        orderDishEntity = new OrderDishEntity();
        OrderDishId dishId = new OrderDishId(1L, 10L);
        orderDishEntity.setId(dishId);
        orderDishEntity.setDish(dishEntity);
        orderDishEntity.setQuantity(2);

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setDate(LocalDateTime.now());
        orderEntity.setStatus("PENDIENTE");
        orderEntity.setClientId(5L);
        orderEntity.setEmployeeId(7L);
        orderEntity.setRestaurantId(1L);
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setSecurityPin("1234");
        orderEntity.setOrderDishes(new ArrayList<>(List.of(orderDishEntity)));
    }

    @Test
    void toModel_mapsAllFieldsIncludingRestaurantName() {
        OrderModel result = mapper.toModel(orderEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDIENTE", result.getStatus());
        assertEquals(5L, result.getClientId());
        assertEquals(7L, result.getEmployeeId());
        assertEquals("1234", result.getSecurityPin());
        assertEquals("Burger House", result.getRestaurantName());
    }

    @Test
    void toModel_mapsOrderDishes() {
        OrderModel result = mapper.toModel(orderEntity);

        assertNotNull(result.getDishes());
        assertEquals(1, result.getDishes().size());
        assertEquals(10L, result.getDishes().get(0).getDishId());
        assertEquals(2, result.getDishes().get(0).getQuantity());
        assertEquals("Hamburguesa", result.getDishes().get(0).getDishName());
    }

    @Test
    void toModel_withNullRestaurant_setsNullRestaurantName() {
        orderEntity.setRestaurant(null);

        OrderModel result = mapper.toModel(orderEntity);

        assertNotNull(result);
        assertNull(result.getRestaurantName());
    }

    @Test
    void toModel_withNullEntity_returnsNull() {
        OrderModel result = mapper.toModel(null);
        assertNull(result);
    }

    @Test
    void toModel_withEmptyDishes_returnEmptyList() {
        orderEntity.setOrderDishes(new ArrayList<>());

        OrderModel result = mapper.toModel(orderEntity);

        assertNotNull(result);
        assertNotNull(result.getDishes());
        assertTrue(result.getDishes().isEmpty());
    }

    @Test
    void orderDishEntityToModel_mapsOrderIdDishIdAndDishName() {
        OrderDishModel result = mapper.orderDishEntityToModel(orderDishEntity);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(10L, result.getDishId());
        assertEquals("Hamburguesa", result.getDishName());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void orderDishEntityToModel_withNullEntity_returnsNull() {
        OrderDishModel result = mapper.orderDishEntityToModel(null);
        assertNull(result);
    }

    @Test
    void toModelList_mapsListOfEntities() {
        List<OrderModel> result = mapper.toModelList(List.of(orderEntity));

        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getStatus());
    }

    @Test
    void toModelList_withNullList_returnsNull() {
        List<OrderModel> result = mapper.toModelList(null);
        assertNull(result);
    }

    /*@Test
    void toEntity_mapsBasicFieldsIgnoringRestaurantAndDishes() {
        OrderModel model = new OrderModel();
        model.setId(1L);
        model.setStatus("PENDIENTE");
        model.setClientId(5L);
        model.setEmployeeId(7L);

        OrderEntity result = mapper.toEntity(model);

        assertNotNull(result);
        assertEquals("PENDIENTE", result.getStatus());
        assertEquals(5L, result.getClientId());
        assertNull(result.getRestaurant());
        assertNull(result.getOrderDishes());
    }*/

    @Test
    void toEntity_withNullModel_returnsNull() {
        OrderEntity result = mapper.toEntity(null);
        assertNull(result);
    }
}
