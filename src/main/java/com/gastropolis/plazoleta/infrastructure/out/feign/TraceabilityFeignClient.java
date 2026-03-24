package com.gastropolis.plazoleta.infrastructure.out.feign;

import com.gastropolis.plazoleta.infrastructure.out.feign.dto.CreateOrderLogFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-trazabilidad", url = "${feign.trazabilidad.url}")
public interface TraceabilityFeignClient {

    @PostMapping("/api/v1/traceability/")
    void registerLog(@RequestBody CreateOrderLogFeignDto log, @RequestHeader("Authorization") String token);
}
