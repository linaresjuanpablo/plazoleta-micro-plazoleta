package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.exception.*;
import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ITraceabilityClientPort traceabilityClientPort;

    @Mock
    private IMessagingClientPort messagingClientPort;

    @Mock
    private IUserClientPort userClientPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel orderModel;
    private OrderDishModel orderDishModel;
    private DishModel dish;
    private RestaurantModel restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 20L);

        dish = new DishModel(10L, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);

        orderDishModel = new OrderDishModel(null, 10L, 2);

        orderModel = new OrderModel();
        orderModel.setRestaurantId(1L);
        orderModel.setDishes(List.of(orderDishModel));
    }

    // ---- createOrder ----

    @Test
    void createOrder_whenClientHasActiveOrders_throwsClientHasActiveOrderException() {
        OrderModel active = new OrderModel();
        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(List.of(active));

        assertThrows(ClientHasActiveOrderException.class,
                () -> orderUseCase.createOrder(orderModel, 5L, "token"));

        verifyNoInteractions(dishPersistencePort, restaurantPersistencePort, traceabilityClientPort);
    }

    @Test
    void createOrder_whenDishNotFound_throwsDishNotFoundException() {
        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(Collections.emptyList());
        when(dishPersistencePort.findById(10L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> orderUseCase.createOrder(orderModel, 5L, "token"));
    }

    @Test
    void createOrder_whenDishBelongsToDifferentRestaurant_throwsDishesNotFromSameRestaurantException() {
        dish.setRestaurantId(99L);
        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(Collections.emptyList());
        when(dishPersistencePort.findById(10L)).thenReturn(dish);

        assertThrows(DishesNotFromSameRestaurantException.class,
                () -> orderUseCase.createOrder(orderModel, 5L, "token"));
    }

    @Test
    void createOrder_happyPath_withRestaurantFound_setsFieldsAndRegistersLog() {
        OrderModel saved = buildSavedOrder(1L, 1L, "PENDIENTE");
        saved.setDishes(new ArrayList<>(List.of(new OrderDishModel(1L, 10L, 2))));

        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(Collections.emptyList());
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(saved);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        OrderModel result = orderUseCase.createOrder(orderModel, 5L, "token");

        assertEquals("PENDIENTE", result.getStatus());
        assertEquals("Burger House", result.getRestaurantName());
        verify(traceabilityClientPort).registerLog(
                eq(saved.getId()), eq(5L), isNull(), isNull(), eq("PENDIENTE"), any(LocalDateTime.class), eq("token")
        );
    }

    @Test
    void createOrder_happyPath_withRestaurantNull_setsRestaurantNameToNull() {
        OrderModel saved = buildSavedOrder(1L, 1L, "PENDIENTE");
        saved.setDishes(new ArrayList<>(List.of(new OrderDishModel(1L, 10L, 2))));

        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(Collections.emptyList());
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(saved);
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        OrderModel result = orderUseCase.createOrder(orderModel, 5L, "token");

        assertNull(result.getRestaurantName());
    }

    @Test
    void createOrder_happyPath_whenSavedDishesIsNull_doesNotThrow() {
        OrderModel saved = buildSavedOrder(1L, 1L, "PENDIENTE");
        saved.setDishes(null);

        when(orderPersistencePort.findActiveOrdersByClientId(eq(5L), anyList()))
                .thenReturn(Collections.emptyList());
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(saved);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        OrderModel result = orderUseCase.createOrder(orderModel, 5L, "token");

        assertNotNull(result);
        assertNull(result.getDishes());
    }

    // ---- listOrders ----

    @Test
    void listOrders_whenRestaurantIdNullForEmployee_throwsRestaurantNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> orderUseCase.listOrders(7L, "PENDIENTE", 0, 10, "token"));
    }

    @Test
    void listOrders_withStatus_callsFindByRestaurantIdAndStatus_andSetsClientName() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        order.setClientId(5L);
        Page<OrderModel> page = new PageImpl<>(List.of(order));

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantIdAndStatus(1L, "PENDIENTE", 0, 10)).thenReturn(page);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");

        Page<OrderModel> result = orderUseCase.listOrders(7L, "PENDIENTE", 0, 10, "token");

        assertEquals(1, result.getContent().size());
        assertEquals("Juan Perez", result.getContent().get(0).getClientName());
        verify(orderPersistencePort).findByRestaurantIdAndStatus(1L, "PENDIENTE", 0, 10);
        verify(orderPersistencePort, never()).findByRestaurantId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void listOrders_withNullStatus_callsFindByRestaurantId() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        order.setClientId(5L);
        Page<OrderModel> page = new PageImpl<>(List.of(order));

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantId(1L, 0, 10)).thenReturn(page);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");

        Page<OrderModel> result = orderUseCase.listOrders(7L, null, 0, 10, "token");

        verify(orderPersistencePort).findByRestaurantId(1L, 0, 10);
        verify(orderPersistencePort, never()).findByRestaurantIdAndStatus(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void listOrders_withBlankStatus_callsFindByRestaurantId() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        order.setClientId(5L);
        Page<OrderModel> page = new PageImpl<>(List.of(order));

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantId(1L, 0, 10)).thenReturn(page);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Ana Lopez");

        Page<OrderModel> result = orderUseCase.listOrders(7L, "   ", 0, 10, "token");

        verify(orderPersistencePort).findByRestaurantId(1L, 0, 10);
        assertEquals("Ana Lopez", result.getContent().get(0).getClientName());
    }

    // ---- assignOrder ----

    @Test
    void assignOrder_whenRestaurantNullForEmployee_throwsRestaurantNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> orderUseCase.assignOrder(1L, 7L, "token"));
    }

    @Test
    void assignOrder_whenOrderNotFound_throwsOrderNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(OrderNotFoundException.class,
                () -> orderUseCase.assignOrder(1L, 7L, "token"));
    }

    @Test
    void assignOrder_whenOrderBelongsToDifferentRestaurant_throwsNotRestaurantOwnerException() {
        OrderModel order = buildSavedOrder(1L, 99L, "PENDIENTE");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(NotRestaurantOwnerException.class,
                () -> orderUseCase.assignOrder(1L, 7L, "token"));
    }

    @Test
    void assignOrder_whenOrderNotPendiente_throwsInvalidOrderStateException() {
        OrderModel order = buildSavedOrder(1L, 1L, "EN_PREPARACION");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidOrderStateException.class,
                () -> orderUseCase.assignOrder(1L, 7L, "token"));
    }

    @Test
    void assignOrder_happyPath_setsStatusAndEmployeeAndRegistersLog() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        order.setClientId(5L);
        OrderModel saved = buildSavedOrder(1L, 1L, "EN_PREPARACION");
        saved.setClientId(5L);
        saved.setEmployeeId(7L);

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(saved);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");

        OrderModel result = orderUseCase.assignOrder(1L, 7L, "token");

        assertEquals("EN_PREPARACION", order.getStatus());
        assertEquals(7L, order.getEmployeeId());
        assertEquals("Juan Perez", result.getClientName());
        verify(traceabilityClientPort).registerLog(
                eq(1L), eq(5L), eq(7L), eq("PENDIENTE"), eq("EN_PREPARACION"), any(LocalDateTime.class), eq("token")
        );
    }

    // ---- markOrderReady ----

    @Test
    void markOrderReady_whenRestaurantNullForEmployee_throwsRestaurantNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> orderUseCase.markOrderReady(1L, 7L, "token"));
    }

    @Test
    void markOrderReady_whenOrderNotFound_throwsOrderNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(OrderNotFoundException.class,
                () -> orderUseCase.markOrderReady(1L, 7L, "token"));
    }

    @Test
    void markOrderReady_whenOrderBelongsToDifferentRestaurant_throwsNotRestaurantOwnerException() {
        OrderModel order = buildSavedOrder(1L, 99L, "EN_PREPARACION");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(NotRestaurantOwnerException.class,
                () -> orderUseCase.markOrderReady(1L, 7L, "token"));
    }

    @Test
    void markOrderReady_whenOrderNotEnPreparacion_throwsInvalidOrderStateException() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidOrderStateException.class,
                () -> orderUseCase.markOrderReady(1L, 7L, "token"));
    }

    @Test
    void markOrderReady_happyPath_setsPinAndSendsSmS() {
        OrderModel order = buildSavedOrder(1L, 1L, "EN_PREPARACION");
        order.setClientId(5L);
        OrderModel saved = buildSavedOrder(1L, 1L, "LISTO");
        saved.setClientId(5L);
        saved.setSecurityPin("1234");

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(saved);
        when(userClientPort.getUserPhoneById(5L, "token")).thenReturn("+573001234567");
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");

        OrderModel result = orderUseCase.markOrderReady(1L, 7L, "token");

        assertEquals("LISTO", order.getStatus());
        assertNotNull(order.getSecurityPin());
        assertEquals("Juan Perez", result.getClientName());
        verify(messagingClientPort).sendSms(eq("+573001234567"), contains("PIN de recogida"), eq("token"));
        verify(traceabilityClientPort).registerLog(
                eq(1L), eq(5L), eq(7L), eq("EN_PREPARACION"), eq("LISTO"), any(LocalDateTime.class), eq("token")
        );
    }

    // ---- deliverOrder ----

    @Test
    void deliverOrder_whenRestaurantNullForEmployee_throwsRestaurantNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> orderUseCase.deliverOrder(1L, 7L, "1234", "token"));
    }

    @Test
    void deliverOrder_whenOrderNotFound_throwsOrderNotFoundException() {
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(OrderNotFoundException.class,
                () -> orderUseCase.deliverOrder(1L, 7L, "1234", "token"));
    }

    @Test
    void deliverOrder_whenOrderBelongsToDifferentRestaurant_throwsNotRestaurantOwnerException() {
        OrderModel order = buildSavedOrder(1L, 99L, "LISTO");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(NotRestaurantOwnerException.class,
                () -> orderUseCase.deliverOrder(1L, 7L, "1234", "token"));
    }

    @Test
    void deliverOrder_whenOrderNotListo_throwsInvalidOrderStateException() {
        OrderModel order = buildSavedOrder(1L, 1L, "EN_PREPARACION");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidOrderStateException.class,
                () -> orderUseCase.deliverOrder(1L, 7L, "1234", "token"));
    }

    @Test
    void deliverOrder_whenPinDoesNotMatch_throwsInvalidPinException() {
        OrderModel order = buildSavedOrder(1L, 1L, "LISTO");
        order.setSecurityPin("9999");
        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidPinException.class,
                () -> orderUseCase.deliverOrder(1L, 7L, "1234", "token"));
    }

    @Test
    void deliverOrder_happyPath_withRestaurantFound_setsEntregadoAndRegistersLog() {
        OrderModel order = buildSavedOrder(1L, 1L, "LISTO");
        order.setSecurityPin("1234");
        order.setClientId(5L);
        OrderModel saved = buildSavedOrder(1L, 1L, "ENTREGADO");
        saved.setClientId(5L);
        saved.setRestaurantId(1L);

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(saved);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        OrderModel result = orderUseCase.deliverOrder(1L, 7L, "1234", "token");

        assertEquals("ENTREGADO", order.getStatus());
        assertEquals("Juan Perez", result.getClientName());
        assertEquals("Burger House", result.getRestaurantName());
        verify(traceabilityClientPort).registerLog(
                eq(1L), eq(5L), eq(7L), eq("LISTO"), eq("ENTREGADO"), any(LocalDateTime.class), eq("token")
        );
    }

    @Test
    void deliverOrder_happyPath_whenRestaurantNull_setsRestaurantNameNull() {
        OrderModel order = buildSavedOrder(1L, 1L, "LISTO");
        order.setSecurityPin("1234");
        order.setClientId(5L);
        OrderModel saved = buildSavedOrder(1L, 1L, "ENTREGADO");
        saved.setClientId(5L);
        saved.setRestaurantId(1L);

        when(orderPersistencePort.findRestaurantIdByEmployeeId(7L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(saved);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        OrderModel result = orderUseCase.deliverOrder(1L, 7L, "1234", "token");

        assertNull(result.getRestaurantName());
    }

    // ---- cancelOrder ----

    @Test
    void cancelOrder_whenOrderNotFound_throwsOrderNotFoundException() {
        when(orderPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(OrderNotFoundException.class,
                () -> orderUseCase.cancelOrder(1L, 5L, "token"));
    }

    @Test
    void cancelOrder_whenOrderNotPendiente_throwsInvalidOrderStateException() {
        OrderModel order = buildSavedOrder(1L, 1L, "EN_PREPARACION");
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidOrderStateException.class,
                () -> orderUseCase.cancelOrder(1L, 5L, "token"));
    }

    @Test
    void cancelOrder_happyPath_setsStatusCanceladoAndRegistersLog() {
        OrderModel order = buildSavedOrder(1L, 1L, "PENDIENTE");
        order.setSecurityPin("9999");
        OrderModel saved = buildSavedOrder(1L, 1L, "CANCELADO");
        saved.setSecurityPin("9999");

        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(saved);
        when(userClientPort.getUserNameById(5L, "token")).thenReturn("Juan Perez");

        OrderModel result = orderUseCase.cancelOrder(1L, 5L, "token");

        assertEquals("CANCELADO", order.getStatus());
        assertNull(result.getSecurityPin());
        assertEquals("Juan Perez", result.getClientName());
        verify(traceabilityClientPort).registerLog(
                eq(1L), eq(5L), isNull(), eq("PENDIENTE"), eq("CANCELADO"), any(LocalDateTime.class), eq("token")
        );
    }

    // ---- helpers ----

    private OrderModel buildSavedOrder(Long id, Long restaurantId, String status) {
        OrderModel o = new OrderModel();
        o.setId(id);
        o.setRestaurantId(restaurantId);
        o.setStatus(status);
        return o;
    }
}
