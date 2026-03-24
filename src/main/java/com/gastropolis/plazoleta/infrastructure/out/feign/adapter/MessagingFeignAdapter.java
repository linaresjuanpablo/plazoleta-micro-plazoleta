package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.domain.spi.IMessagingClientPort;
import com.gastropolis.plazoleta.infrastructure.out.feign.MessagingFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.SendSmsFeignDto;

public class MessagingFeignAdapter implements IMessagingClientPort {

    private final MessagingFeignClient messagingFeignClient;

    public MessagingFeignAdapter(MessagingFeignClient messagingFeignClient) {
        this.messagingFeignClient = messagingFeignClient;
    }

    @Override
    public void sendSms(String phoneNumber, String message, String token) {
        SendSmsFeignDto sms = new SendSmsFeignDto(phoneNumber, message);
        messagingFeignClient.sendSms(sms, token);
    }
}
