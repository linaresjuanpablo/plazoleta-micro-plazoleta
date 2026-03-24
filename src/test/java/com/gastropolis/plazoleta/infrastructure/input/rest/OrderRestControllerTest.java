package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.DeliverOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.application.handler.IOrderHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    private IOrderHandler orderHandler;

    @InjectMocks
    private OrderRestController orderRestController;

    private OrderResponseDto orderResponseDto;
    private final Long userId = 10L;
    private final String token = "Bearer mytoken";

    @BeforeEach
    void setUp() {
        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setStatus("PENDIENTE");

        Authentication auth = mock(Authentication.class);
        when(auth.getDetails()).thenReturn(userId);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createOrder_delegatesToHandlerWithClientIdAndToken_andReturnsCreated() {
        CreateOrderRequestDto dto = new CreateOrderRequestDto();
        when(orderHandler.createOrder(dto, userId, token)).thenReturn(orderResponseDto);

        ResponseEntity<ApiResponse<OrderResponseDto>> response =
                orderRestController.createOrder(dto, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido creado exitosamente", response.getBody().getMessage());
        assertEquals(orderResponseDto, response.getBody().getData());
        verify(orderHandler).createOrder(dto, userId, token);
    }

    @Test
    void listOrders_withStatus_delegatesToHandlerWithEmployeeIdAndToken_andReturnsOk() {
        Page<OrderResponseDto> page = new PageImpl<>(List.of(orderResponseDto));
        when(orderHandler.listOrders(userId, "PENDIENTE", 0, 10, token)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<OrderResponseDto>>> response =
                orderRestController.listOrders("PENDIENTE", 0, 10, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedidos obtenidos exitosamente", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().getContent().size());
        verify(orderHandler).listOrders(userId, "PENDIENTE", 0, 10, token);
    }

    @Test
    void listOrders_withNullStatus_delegatesNullStatus() {
        Page<OrderResponseDto> page = new PageImpl<>(List.of());
        when(orderHandler.listOrders(userId, null, 0, 10, token)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<OrderResponseDto>>> response =
                orderRestController.listOrders(null, 0, 10, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderHandler).listOrders(userId, null, 0, 10, token);
    }

    @Test
    void assignOrder_delegatesToHandlerWithEmployeeIdAndToken_andReturnsOk() {
        when(orderHandler.assignOrder(1L, userId, token)).thenReturn(orderResponseDto);

        ResponseEntity<ApiResponse<OrderResponseDto>> response =
                orderRestController.assignOrder(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido asignado exitosamente", response.getBody().getMessage());
        assertEquals(orderResponseDto, response.getBody().getData());
        verify(orderHandler).assignOrder(1L, userId, token);
    }

    @Test
    void markOrderReady_delegatesToHandlerWithEmployeeIdAndToken_andReturnsOk() {
        when(orderHandler.markOrderReady(1L, userId, token)).thenReturn(orderResponseDto);

        ResponseEntity<ApiResponse<OrderResponseDto>> response =
                orderRestController.markOrderReady(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido marcado como listo exitosamente", response.getBody().getMessage());
        assertEquals(orderResponseDto, response.getBody().getData());
        verify(orderHandler).markOrderReady(1L, userId, token);
    }

    @Test
    void deliverOrder_delegatesToHandlerWithEmployeeIdPinAndToken_andReturnsOk() {
        DeliverOrderRequestDto dto = new DeliverOrderRequestDto();
        dto.setPin("1234");
        when(orderHandler.deliverOrder(1L, userId, dto, token)).thenReturn(orderResponseDto);

        ResponseEntity<ApiResponse<OrderResponseDto>> response =
                orderRestController.deliverOrder(1L, dto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido entregado exitosamente", response.getBody().getMessage());
        assertEquals(orderResponseDto, response.getBody().getData());
        verify(orderHandler).deliverOrder(1L, userId, dto, token);
    }

    @Test
    void cancelOrder_delegatesToHandlerWithClientIdAndToken_andReturnsOk() {
        when(orderHandler.cancelOrder(1L, userId, token)).thenReturn(orderResponseDto);

        ResponseEntity<ApiResponse<OrderResponseDto>> response =
                orderRestController.cancelOrder(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido cancelado exitosamente", response.getBody().getMessage());
        assertEquals(orderResponseDto, response.getBody().getData());
        verify(orderHandler).cancelOrder(1L, userId, token);
    }
}
