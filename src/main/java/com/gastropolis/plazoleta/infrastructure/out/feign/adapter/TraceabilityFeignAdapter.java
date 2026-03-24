package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.domain.spi.ITraceabilityClientPort;
import com.gastropolis.plazoleta.infrastructure.out.feign.TraceabilityFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.CreateOrderLogFeignDto;

import java.time.LocalDateTime;

public class TraceabilityFeignAdapter implements ITraceabilityClientPort {

    private final TraceabilityFeignClient traceabilityFeignClient;

    public TraceabilityFeignAdapter(TraceabilityFeignClient traceabilityFeignClient) {
        this.traceabilityFeignClient = traceabilityFeignClient;
    }

    @Override
    public void registerLog(Long orderId, Long clientId, Long employeeId, String previousStatus,
                            String newStatus, LocalDateTime timestamp, String token) {
        CreateOrderLogFeignDto log = new CreateOrderLogFeignDto(
                orderId, clientId, employeeId, previousStatus, newStatus, timestamp
        );
        traceabilityFeignClient.registerLog(log, token);
    }
}
