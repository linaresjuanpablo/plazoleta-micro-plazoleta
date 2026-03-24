package com.gastropolis.plazoleta.infrastructure.out.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderLogFeignDto {
    private Long orderId;
    private Long clientId;
    private Long employeeId;
    private String previousStatus;
    private String newStatus;
    private LocalDateTime timestamp;
}
