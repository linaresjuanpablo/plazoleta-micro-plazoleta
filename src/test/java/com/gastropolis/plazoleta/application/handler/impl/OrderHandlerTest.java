package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.DeliverOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.application.mapper.IOrderRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IOrderResponseMapper;
import com.gastropolis.plazoleta.domain.api.IOrderServicePort;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper orderRequestMapper;

    @Mock
    private IOrderResponseMapper orderResponseMapper;

    @InjectMocks
    private OrderHandler orderHandler;

    private OrderModel orderModel;
    private OrderResponseDto orderResponseDto;

    @BeforeEach
    void setUp() {
        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setRestaurantId(1L);
        orderModel.setClientId(5L);
        orderModel.setStatus("PENDIENTE");
        orderModel.setDate(LocalDateTime.now());

        orderResponseDto = new OrderResponseDto(
                1L, LocalDateTime.now(), "PENDIENTE", 5L, "Juan Perez", 1L, "Burger House", null, null
        );
    }

    // ---- createOrder ----

    @Test
    void createOrder_mapsRequestAndDelegatesAndReturnsResponseDto() {
        CreateOrderRequestDto dto = new CreateOrderRequestDto();
        dto.setRestaurantId(1L);

        when(orderRequestMapper.toOrderModel(dto)).thenReturn(orderModel);
        when(orderServicePort.createOrder(orderModel, 5L, "token")).thenReturn(orderModel);
        when(orderResponseMapper.toOrderResponseDto(orderModel)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderHandler.createOrder(dto, 5L, "token");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRequestMapper).toOrderModel(dto);
        verify(orderServicePort).createOrder(orderModel, 5L, "token");
        verify(orderResponseMapper).toOrderResponseDto(orderModel);
    }

    // ---- listOrders ----

    @Test
    void listOrders_withStatus_delegatesAndMapPage() {
        Page<OrderModel> modelPage = new PageImpl<>(List.of(orderModel));
        when(orderServicePort.listOrders(7L, "PENDIENTE", 0, 10, "token")).thenReturn(modelPage);
        when(orderResponseMapper.toOrderResponseDto(orderModel)).thenReturn(orderResponseDto);

        Page<OrderResponseDto> result = orderHandler.listOrders(7L, "PENDIENTE", 0, 10, "token");

        assertEquals(1, result.getContent().size());
        assertEquals("PENDIENTE", result.getContent().get(0).getStatus());
        verify(orderServicePort).listOrders(7L, "PENDIENTE", 0, 10, "token");
    }

    @Test
    void listOrders_withNullStatus_delegatesAndMapPage() {
        Page<OrderModel> modelPage = new PageImpl<>(List.of(orderModel));
        when(orderServicePort.listOrders(7L, null, 0, 10, "token")).thenReturn(modelPage);
        when(orderResponseMapper.toOrderResponseDto(orderModel)).thenReturn(orderResponseDto);

        Page<OrderResponseDto> result = orderHandler.listOrders(7L, null, 0, 10, "token");

        assertEquals(1, result.getContent().size());
        verify(orderServicePort).listOrders(7L, null, 0, 10, "token");
    }

    // ---- assignOrder ----

    @Test
    void assignOrder_delegatesAndReturnsResponseDto() {
        OrderModel assigned = new OrderModel();
        assigned.setId(1L);
        assigned.setStatus("EN_PREPARACION");
        OrderResponseDto assignedDto = new OrderResponseDto(
                1L, LocalDateTime.now(), "EN_PREPARACION", 5L, "Juan Perez", 1L, "Burger House", null, null
        );

        when(orderServicePort.assignOrder(1L, 7L, "token")).thenReturn(assigned);
        when(orderResponseMapper.toOrderResponseDto(assigned)).thenReturn(assignedDto);

        OrderResponseDto result = orderHandler.assignOrder(1L, 7L, "token");

        assertNotNull(result);
        assertEquals("EN_PREPARACION", result.getStatus());
        verify(orderServicePort).assignOrder(1L, 7L, "token");
    }

    // ---- markOrderReady ----

    @Test
    void markOrderReady_delegatesAndReturnsResponseDto() {
        OrderModel ready = new OrderModel();
        ready.setId(1L);
        ready.setStatus("LISTO");
        OrderResponseDto readyDto = new OrderResponseDto(
                1L, LocalDateTime.now(), "LISTO", 5L, "Juan Perez", 1L, "Burger House", "1234", null
        );

        when(orderServicePort.markOrderReady(1L, 7L, "token")).thenReturn(ready);
        when(orderResponseMapper.toOrderResponseDto(ready)).thenReturn(readyDto);

        OrderResponseDto result = orderHandler.markOrderReady(1L, 7L, "token");

        assertNotNull(result);
        assertEquals("LISTO", result.getStatus());
        assertEquals("1234", result.getSecurityPin());
        verify(orderServicePort).markOrderReady(1L, 7L, "token");
    }

    // ---- deliverOrder ----

    @Test
    void deliverOrder_extractsPinFromDtoAndDelegatesAndReturnsResponseDto() {
        DeliverOrderRequestDto dto = new DeliverOrderRequestDto();
        dto.setPin("1234");

        OrderModel delivered = new OrderModel();
        delivered.setId(1L);
        delivered.setStatus("ENTREGADO");
        OrderResponseDto deliveredDto = new OrderResponseDto(
                1L, LocalDateTime.now(), "ENTREGADO", 5L, "Juan Perez", 1L, "Burger House", null, null
        );

        when(orderServicePort.deliverOrder(1L, 7L, "1234", "token")).thenReturn(delivered);
        when(orderResponseMapper.toOrderResponseDto(delivered)).thenReturn(deliveredDto);

        OrderResponseDto result = orderHandler.deliverOrder(1L, 7L, dto, "token");

        assertNotNull(result);
        assertEquals("ENTREGADO", result.getStatus());
        verify(orderServicePort).deliverOrder(1L, 7L, "1234", "token");
    }

    // ---- cancelOrder ----

    @Test
    void cancelOrder_delegatesAndReturnsResponseDto() {
        OrderModel cancelled = new OrderModel();
        cancelled.setId(1L);
        cancelled.setStatus("CANCELADO");
        OrderResponseDto cancelledDto = new OrderResponseDto(
                1L, LocalDateTime.now(), "CANCELADO", 5L, "Juan Perez", 1L, "Burger House", null, null
        );

        when(orderServicePort.cancelOrder(1L, 5L, "token")).thenReturn(cancelled);
        when(orderResponseMapper.toOrderResponseDto(cancelled)).thenReturn(cancelledDto);

        OrderResponseDto result = orderHandler.cancelOrder(1L, 5L, "token");

        assertNotNull(result);
        assertEquals("CANCELADO", result.getStatus());
        verify(orderServicePort).cancelOrder(1L, 5L, "token");
    }
}
