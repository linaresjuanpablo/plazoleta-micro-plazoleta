package com.gastropolis.plazoleta.domain.api;

import com.gastropolis.plazoleta.domain.model.OrderModel;
import org.springframework.data.domain.Page;

public interface IOrderServicePort {
    OrderModel createOrder(OrderModel orderModel, Long clientId, String token);
    Page<OrderModel> listOrders(Long employeeId, String status, int page, int size, String token);
    OrderModel assignOrder(Long orderId, Long employeeId, String token);
    OrderModel markOrderReady(Long orderId, Long employeeId, String token);
    OrderModel deliverOrder(Long orderId, Long employeeId, String pin, String token);
    OrderModel cancelOrder(Long orderId, Long clientId, String token);
}
