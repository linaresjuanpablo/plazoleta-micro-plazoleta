package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.infrastructure.out.feign.TraceabilityFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.CreateOrderLogFeignDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityFeignAdapterTest {

    @Mock
    private TraceabilityFeignClient traceabilityFeignClient;

    private TraceabilityFeignAdapter traceabilityFeignAdapter;

    @BeforeEach
    void setUp() {
        traceabilityFeignAdapter = new TraceabilityFeignAdapter(traceabilityFeignClient);
    }

    @Test
    void registerLog_buildsDtoAndCallsFeignClient() {
        Long orderId = 1L;
        Long clientId = 5L;
        Long employeeId = 7L;
        String previousStatus = "PENDIENTE";
        String newStatus = "EN_PREPARACION";
        LocalDateTime timestamp = LocalDateTime.now();
        String token = "Bearer mytoken";

        doNothing().when(traceabilityFeignClient).registerLog(any(CreateOrderLogFeignDto.class), eq(token));

        traceabilityFeignAdapter.registerLog(orderId, clientId, employeeId, previousStatus, newStatus, timestamp, token);

        ArgumentCaptor<CreateOrderLogFeignDto> captor = ArgumentCaptor.forClass(CreateOrderLogFeignDto.class);
        verify(traceabilityFeignClient).registerLog(captor.capture(), eq(token));

        CreateOrderLogFeignDto capturedDto = captor.getValue();
        assertEquals(orderId, capturedDto.getOrderId());
        assertEquals(clientId, capturedDto.getClientId());
        assertEquals(employeeId, capturedDto.getEmployeeId());
        assertEquals(previousStatus, capturedDto.getPreviousStatus());
        assertEquals(newStatus, capturedDto.getNewStatus());
        assertEquals(timestamp, capturedDto.getTimestamp());
    }

    @Test
    void registerLog_withNullEmployeeId_passesNullCorrectly() {
        LocalDateTime timestamp = LocalDateTime.now();
        String token = "Bearer mytoken";

        doNothing().when(traceabilityFeignClient).registerLog(any(CreateOrderLogFeignDto.class), eq(token));

        traceabilityFeignAdapter.registerLog(1L, 5L, null, null, "PENDIENTE", timestamp, token);

        ArgumentCaptor<CreateOrderLogFeignDto> captor = ArgumentCaptor.forClass(CreateOrderLogFeignDto.class);
        verify(traceabilityFeignClient).registerLog(captor.capture(), eq(token));

        assertNull(captor.getValue().getEmployeeId());
        assertNull(captor.getValue().getPreviousStatus());
    }
}
