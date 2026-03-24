package com.gastropolis.plazoleta.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderModel {

    private Long id;
    private LocalDateTime date;
    private String status;
    private Long clientId;
    private String clientName;
    private Long employeeId;
    private Long restaurantId;
    private String restaurantName;
    private String securityPin;
    private List<OrderDishModel> dishes;

    public OrderModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public String getSecurityPin() { return securityPin; }
    public void setSecurityPin(String securityPin) { this.securityPin = securityPin; }
    public List<OrderDishModel> getDishes() { return dishes; }
    public void setDishes(List<OrderDishModel> dishes) { this.dishes = dishes; }
}
