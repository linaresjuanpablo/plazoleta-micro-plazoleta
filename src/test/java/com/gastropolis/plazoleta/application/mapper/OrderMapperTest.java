package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.OrderDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.OrderDishResponseDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
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
        IOrderRequestMapperImpl.class,
        IOrderResponseMapperImpl.class
})
class OrderMapperTest {

    @Autowired
    private IOrderRequestMapper requestMapper;

    @Autowired
    private IOrderResponseMapper responseMapper;

    private CreateOrderRequestDto createOrderDto;
    private OrderModel orderModel;
    private OrderDishModel orderDishModel;

    @BeforeEach
    void setUp() {
        OrderDishRequestDto dishDto = new OrderDishRequestDto();
        dishDto.setDishId(10L);
        dishDto.setQuantity(2);

        createOrderDto = new CreateOrderRequestDto();
        createOrderDto.setRestaurantId(1L);
        createOrderDto.setDishes(List.of(dishDto));

        orderDishModel = new OrderDishModel(null, 10L, 2);
        orderDishModel.setDishName("Hamburguesa");

        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setStatus("PENDIENTE");
        orderModel.setClientId(5L);
        orderModel.setRestaurantId(1L);
        orderModel.setClientName("Juan Perez");
        orderModel.setDishes(List.of(orderDishModel));
    }

    // ---- IOrderRequestMapper ----

    @Test
    void toOrderModel_mapsRestaurantIdAndDishes() {
        OrderModel result = requestMapper.toOrderModel(createOrderDto);

        assertNotNull(result);
        assertEquals(1L, result.getRestaurantId());
        assertNotNull(result.getDishes());
        assertEquals(1, result.getDishes().size());
        assertEquals(10L, result.getDishes().get(0).getDishId());
        assertEquals(2, result.getDishes().get(0).getQuantity());
    }

    @Test
    void toOrderModel_withNullDto_returnsNull() {
        OrderModel result = requestMapper.toOrderModel(null);
        assertNull(result);
    }

    @Test
    void toOrderDishModel_mapsDishIdAndQuantity_orderIdIsNull() {
        OrderDishRequestDto dishDto = new OrderDishRequestDto();
        dishDto.setDishId(15L);
        dishDto.setQuantity(3);

        OrderDishModel result = requestMapper.toOrderDishModel(dishDto);

        assertNotNull(result);
        assertEquals(15L, result.getDishId());
        assertEquals(3, result.getQuantity());
        assertNull(result.getOrderId());
    }

    @Test
    void toOrderDishModel_withNullDto_returnsNull() {
        OrderDishModel result = requestMapper.toOrderDishModel(null);
        assertNull(result);
    }

    // ---- IOrderResponseMapper ----

    @Test
    void toOrderResponseDto_mapsAllFields() {
        OrderResponseDto result = responseMapper.toOrderResponseDto(orderModel);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDIENTE", result.getStatus());
        assertEquals(5L, result.getClientId());
        assertEquals("Juan Perez", result.getClientName());
    }

    @Test
    void toOrderResponseDto_withNullModel_returnsNull() {
        OrderResponseDto result = responseMapper.toOrderResponseDto(null);
        assertNull(result);
    }

    @Test
    void toOrderDishResponseDto_mapsDishIdAndQuantity() {
        OrderDishResponseDto result = responseMapper.toOrderDishResponseDto(orderDishModel);

        assertNotNull(result);
        assertEquals(10L, result.getDishId());
        assertEquals(2, result.getQuantity());
        assertEquals("Hamburguesa", result.getDishName());
    }

    @Test
    void toOrderDishResponseDto_withNullModel_returnsNull() {
        OrderDishResponseDto result = responseMapper.toOrderDishResponseDto(null);
        assertNull(result);
    }

    @Test
    void toOrderResponseDtoList_mapsList() {
        List<OrderResponseDto> result = responseMapper.toOrderResponseDtoList(List.of(orderModel));

        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getStatus());
    }

    @Test
    void toOrderResponseDtoList_withNullList_returnsNull() {
        List<OrderResponseDto> result = responseMapper.toOrderResponseDtoList(null);
        assertNull(result);
    }
}
