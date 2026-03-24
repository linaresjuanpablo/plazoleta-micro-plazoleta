package com.gastropolis.plazoleta.domain.spi;

public interface IMessagingClientPort {
    void sendSms(String phoneNumber, String message, String token);
}
