package com.gastropolis.plazoleta.domain.spi;

import com.gastropolis.plazoleta.domain.model.OrderModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderPersistencePort {
    OrderModel saveOrder(OrderModel orderModel);
    OrderModel findById(Long id);
    Page<OrderModel> findByRestaurantId(Long restaurantId, int page, int size);
    Page<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, String status, int page, int size);
    List<OrderModel> findActiveOrdersByClientId(Long clientId, List<String> statuses);
    Long findRestaurantIdByEmployeeId(Long employeeId);
}
