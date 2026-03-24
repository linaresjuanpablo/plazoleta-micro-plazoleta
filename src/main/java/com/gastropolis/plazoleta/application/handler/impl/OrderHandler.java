package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.DeliverOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.application.handler.IOrderHandler;
import com.gastropolis.plazoleta.application.mapper.IOrderRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IOrderResponseMapper;
import com.gastropolis.plazoleta.domain.api.IOrderServicePort;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto dto, Long clientId, String token) {
        OrderModel model = orderRequestMapper.toOrderModel(dto);
        OrderModel saved = orderServicePort.createOrder(model, clientId, token);
        return orderResponseMapper.toOrderResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> listOrders(Long employeeId, String status, int page, int size, String token) {
        Page<OrderModel> orderPage = orderServicePort.listOrders(employeeId, status, page, size, token);
        return orderPage.map(orderResponseMapper::toOrderResponseDto);
    }

    @Override
    public OrderResponseDto assignOrder(Long orderId, Long employeeId, String token) {
        OrderModel updated = orderServicePort.assignOrder(orderId, employeeId, token);
        return orderResponseMapper.toOrderResponseDto(updated);
    }

    @Override
    public OrderResponseDto markOrderReady(Long orderId, Long employeeId, String token) {
        OrderModel updated = orderServicePort.markOrderReady(orderId, employeeId, token);
        return orderResponseMapper.toOrderResponseDto(updated);
    }

    @Override
    public OrderResponseDto deliverOrder(Long orderId, Long employeeId, DeliverOrderRequestDto dto, String token) {
        OrderModel updated = orderServicePort.deliverOrder(orderId, employeeId, dto.getPin(), token);
        return orderResponseMapper.toOrderResponseDto(updated);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId, Long clientId, String token) {
        OrderModel updated = orderServicePort.cancelOrder(orderId, clientId, token);
        return orderResponseMapper.toOrderResponseDto(updated);
    }
}
