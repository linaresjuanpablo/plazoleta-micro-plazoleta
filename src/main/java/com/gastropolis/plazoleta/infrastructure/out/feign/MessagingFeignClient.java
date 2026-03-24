package com.gastropolis.plazoleta.infrastructure.out.feign;

import com.gastropolis.plazoleta.infrastructure.out.feign.dto.SendSmsFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-mensajeria", url = "${feign.mensajeria.url}")
public interface MessagingFeignClient {

    @PostMapping("/api/v1/sms/send")
    void sendSms(@RequestBody SendSmsFeignDto sms, @RequestHeader("Authorization") String token);
}
