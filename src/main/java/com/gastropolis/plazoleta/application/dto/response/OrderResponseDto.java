package com.gastropolis.plazoleta.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private LocalDateTime date;
    private String status;
    private Long clientId;
    private String clientName;
    private Long restaurantId;
    private String restaurantName;
    private String securityPin;
    private List<OrderDishResponseDto> dishes;
}
