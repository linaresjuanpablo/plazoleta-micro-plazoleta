package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.api.IOrderServicePort;
import com.gastropolis.plazoleta.domain.exception.*;
import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderUseCase implements IOrderServicePort {

    private static final String STATUS_PENDIENTE = "PENDIENTE";
    private static final String STATUS_EN_PREPARACION = "EN_PREPARACION";
    private static final String STATUS_LISTO = "LISTO";
    private static final String STATUS_ENTREGADO = "ENTREGADO";
    private static final String STATUS_CANCELADO = "CANCELADO";
    private static final List<String> ACTIVE_STATUSES = List.of(STATUS_PENDIENTE, STATUS_EN_PREPARACION, STATUS_LISTO);

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITraceabilityClientPort traceabilityClientPort;
    private final IMessagingClientPort messagingClientPort;
    private final IUserClientPort userClientPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        ITraceabilityClientPort traceabilityClientPort,
                        IMessagingClientPort messagingClientPort,
                        IUserClientPort userClientPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.traceabilityClientPort = traceabilityClientPort;
        this.messagingClientPort = messagingClientPort;
        this.userClientPort = userClientPort;
    }

    @Override
    public OrderModel createOrder(OrderModel orderModel, Long clientId, String token) {
        List<OrderModel> activeOrders = orderPersistencePort.findActiveOrdersByClientId(clientId, ACTIVE_STATUSES);
        if (!activeOrders.isEmpty()) {
            throw new ClientHasActiveOrderException();
        }

        Map<Long, String> dishNames = new HashMap<>();
        for (OrderDishModel orderDish : orderModel.getDishes()) {
            DishModel dish = dishPersistencePort.findById(orderDish.getDishId());
            if (dish == null) {
                throw new DishNotFoundException("Plato no encontrado con id: " + orderDish.getDishId());
            }
            if (!dish.getRestaurantId().equals(orderModel.getRestaurantId())) {
                throw new DishesNotFromSameRestaurantException();
            }
            dishNames.put(dish.getId(), dish.getName());
        }

        orderModel.setClientId(clientId);
        orderModel.setStatus(STATUS_PENDIENTE);
        orderModel.setDate(LocalDateTime.now());
        orderModel.setEmployeeId(null);
        orderModel.setSecurityPin(null);

        OrderModel saved = orderPersistencePort.saveOrder(orderModel);

        RestaurantModel restaurant = restaurantPersistencePort.findById(saved.getRestaurantId());
        saved.setRestaurantName(restaurant != null ? restaurant.getName() : null);
        if (saved.getDishes() != null) {
            for (OrderDishModel dishModel : saved.getDishes()) {
                dishModel.setDishName(dishNames.get(dishModel.getDishId()));
            }
        }

        traceabilityClientPort.registerLog(
                saved.getId(), clientId, null, null, STATUS_PENDIENTE, LocalDateTime.now(), token
        );

        return saved;
    }

    @Override
    public Page<OrderModel> listOrders(Long employeeId, String status, int page, int size, String token) {
        Long restaurantId = orderPersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (restaurantId == null) {
            throw new RestaurantNotFoundException("El empleado no pertenece a ningún restaurante");
        }

        Page<OrderModel> orders;
        if (status != null && !status.isBlank()) {
            orders = orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
        } else {
            orders = orderPersistencePort.findByRestaurantId(restaurantId, page, size);
        }

        orders.getContent().forEach(order -> {
            String clientName = userClientPort.getUserNameById(order.getClientId(), token);
            order.setClientName(clientName);
        });

        return orders;
    }

    @Override
    public OrderModel assignOrder(Long orderId, Long employeeId, String token) {
        Long restaurantId = orderPersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (restaurantId == null) {
            throw new RestaurantNotFoundException("El empleado no pertenece a ningún restaurante");
        }

        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new NotRestaurantOwnerException("El pedido no pertenece al restaurante del empleado");
        }

        if (!STATUS_PENDIENTE.equals(order.getStatus())) {
            throw new InvalidOrderStateException("El pedido no está en estado PENDIENTE");
        }

        String previousStatus = order.getStatus();
        order.setStatus(STATUS_EN_PREPARACION);
        order.setEmployeeId(employeeId);
        OrderModel saved = orderPersistencePort.saveOrder(order);

        traceabilityClientPort.registerLog(
                orderId, order.getClientId(), employeeId, previousStatus, STATUS_EN_PREPARACION, LocalDateTime.now(), token
        );

        String clientName = userClientPort.getUserNameById(saved.getClientId(), token);
        saved.setClientName(clientName);

        return saved;
    }

    @Override
    public OrderModel markOrderReady(Long orderId, Long employeeId, String token) {
        Long restaurantId = orderPersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (restaurantId == null) {
            throw new RestaurantNotFoundException("El empleado no pertenece a ningún restaurante");
        }

        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new NotRestaurantOwnerException("El pedido no pertenece al restaurante del empleado");
        }

        if (!STATUS_EN_PREPARACION.equals(order.getStatus())) {
            throw new InvalidOrderStateException("El pedido no está en estado EN_PREPARACION");
        }

        String pin = String.format("%04d", new Random().nextInt(10000));
        String previousStatus = order.getStatus();
        order.setStatus(STATUS_LISTO);
        order.setSecurityPin(pin);
        OrderModel saved = orderPersistencePort.saveOrder(order);

        traceabilityClientPort.registerLog(
                orderId, order.getClientId(), employeeId, previousStatus, STATUS_LISTO, LocalDateTime.now(), token
        );

        String clientPhone = userClientPort.getUserPhoneById(order.getClientId(), token);
        messagingClientPort.sendSms(
                clientPhone,
                "Tu pedido está listo. PIN de recogida: " + pin,
                token
        );

        String clientName = userClientPort.getUserNameById(saved.getClientId(), token);
        saved.setClientName(clientName);

        return saved;
    }

    @Override
    public OrderModel deliverOrder(Long orderId, Long employeeId, String pin, String token) {
        Long restaurantId = orderPersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (restaurantId == null) {
            throw new RestaurantNotFoundException("El empleado no pertenece a ningún restaurante");
        }

        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new NotRestaurantOwnerException("El pedido no pertenece al restaurante del empleado");
        }

        if (!STATUS_LISTO.equals(order.getStatus())) {
            throw new InvalidOrderStateException("El pedido no está en estado LISTO");
        }

        if (!pin.equals(order.getSecurityPin())) {
            throw new InvalidPinException();
        }

        String previousStatus = order.getStatus();
        order.setStatus(STATUS_ENTREGADO);
        OrderModel saved = orderPersistencePort.saveOrder(order);

        traceabilityClientPort.registerLog(
                orderId, order.getClientId(), employeeId, previousStatus, STATUS_ENTREGADO, LocalDateTime.now(), token
        );

        String clientName = userClientPort.getUserNameById(saved.getClientId(), token);
        saved.setClientName(clientName);

        RestaurantModel restaurant = restaurantPersistencePort.findById(saved.getRestaurantId());
        saved.setRestaurantName(restaurant != null ? restaurant.getName() : null);

        return saved;
    }

    @Override
    public OrderModel cancelOrder(Long orderId, Long clientId, String token) {
        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        if (!STATUS_PENDIENTE.equals(order.getStatus())) {
            throw new InvalidOrderStateException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        String previousStatus = order.getStatus();
        order.setStatus(STATUS_CANCELADO);
        OrderModel saved = orderPersistencePort.saveOrder(order);

        traceabilityClientPort.registerLog(
                orderId, clientId, null, previousStatus, STATUS_CANCELADO, LocalDateTime.now(), token
        );

        String clientName = userClientPort.getUserNameById(clientId, token);
        saved.setClientName(clientName);
        saved.setSecurityPin(null);

        return saved;
    }
}
