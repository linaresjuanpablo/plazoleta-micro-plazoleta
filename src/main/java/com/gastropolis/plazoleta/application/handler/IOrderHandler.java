package com.gastropolis.plazoleta.application.handler;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.DeliverOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    OrderResponseDto createOrder(CreateOrderRequestDto dto, Long clientId, String token);
    Page<OrderResponseDto> listOrders(Long employeeId, String status, int page, int size, String token);
    OrderResponseDto assignOrder(Long orderId, Long employeeId, String token);
    OrderResponseDto markOrderReady(Long orderId, Long employeeId, String token);
    OrderResponseDto deliverOrder(Long orderId, Long employeeId, DeliverOrderRequestDto dto, String token);
    OrderResponseDto cancelOrder(Long orderId, Long clientId, String token);
}
