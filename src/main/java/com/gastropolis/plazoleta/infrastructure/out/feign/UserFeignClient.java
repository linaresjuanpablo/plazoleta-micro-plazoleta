package com.gastropolis.plazoleta.infrastructure.out.feign;

import com.gastropolis.plazoleta.infrastructure.out.feign.dto.CreateEmployeeFeignRequestDto;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.UserFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-usuarios", url = "${feign.usuarios.url}")
public interface UserFeignClient {

    @GetMapping("/api/v1/user/{id}")
    UserFeignResponseDto getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @PostMapping("/api/v1/user/owner/employee")
    UserFeignResponseDto createEmployee(@RequestBody CreateEmployeeFeignRequestDto dto,
                                        @RequestHeader("Authorization") String token);
}
