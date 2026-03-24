package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.infrastructure.out.feign.MessagingFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.SendSmsFeignDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagingFeignAdapterTest {

    @Mock
    private MessagingFeignClient messagingFeignClient;

    private MessagingFeignAdapter messagingFeignAdapter;

    @BeforeEach
    void setUp() {
        messagingFeignAdapter = new MessagingFeignAdapter(messagingFeignClient);
    }

    @Test
    void sendSms_buildsDtoAndCallsFeignClient() {
        String phone = "+573001234567";
        String message = "Su pedido está listo. PIN: 1234";
        String token = "Bearer mytoken";

        doNothing().when(messagingFeignClient).sendSms(any(SendSmsFeignDto.class), eq(token));

        messagingFeignAdapter.sendSms(phone, message, token);

        ArgumentCaptor<SendSmsFeignDto> captor = ArgumentCaptor.forClass(SendSmsFeignDto.class);
        verify(messagingFeignClient).sendSms(captor.capture(), eq(token));

        SendSmsFeignDto capturedDto = captor.getValue();
        assertEquals(phone, capturedDto.getPhoneNumber());
        assertEquals(message, capturedDto.getMessage());
    }

    @Test
    void sendSms_callsFeignClientExactlyOnce() {
        doNothing().when(messagingFeignClient).sendSms(any(), any());

        messagingFeignAdapter.sendSms("+573001111111", "Test message", "token");

        verify(messagingFeignClient, times(1)).sendSms(any(SendSmsFeignDto.class), eq("token"));
    }
}
