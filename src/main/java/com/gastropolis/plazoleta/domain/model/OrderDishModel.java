package com.gastropolis.plazoleta.domain.model;

public class OrderDishModel {

    private Long orderId;
    private Long dishId;
    private String dishName;
    private Integer quantity;

    public OrderDishModel() {}

    public OrderDishModel(Long orderId, Long dishId, Integer quantity) {
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
