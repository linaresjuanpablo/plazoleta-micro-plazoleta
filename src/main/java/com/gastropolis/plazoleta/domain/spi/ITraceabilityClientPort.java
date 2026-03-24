package com.gastropolis.plazoleta.domain.spi;

import java.time.LocalDateTime;

public interface ITraceabilityClientPort {
    void registerLog(Long orderId, Long clientId, Long employeeId, String previousStatus, String newStatus, LocalDateTime timestamp, String token);
}
