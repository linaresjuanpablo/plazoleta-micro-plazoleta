package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.spi.IUserClientPort;
import com.gastropolis.plazoleta.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.CreateEmployeeFeignRequestDto;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.UserFeignResponseDto;

public class UserFeignAdapter implements IUserClientPort {

    private final UserFeignClient userFeignClient;

    public UserFeignAdapter(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public String getUserRoleById(Long userId, String token) {
        UserFeignResponseDto user = userFeignClient.getUserById(userId, token);
        return user.getRoleName();
    }

    @Override
    public String getUserPhoneById(Long userId, String token) {
        UserFeignResponseDto user = userFeignClient.getUserById(userId, token);
        return user.getPhone();
    }

    @Override
    public String getUserNameById(Long userId, String token) {
        UserFeignResponseDto user = userFeignClient.getUserById(userId, token);
        return user.getName() + " " + user.getLastName();
    }

    @Override
    public Long getOwnerIdByDni(String dni, String token) {
        UserFeignResponseDto user = userFeignClient.getUserByDni(dni, token);
        return user.getId();
    }

    @Override
    public Long createEmployee(CreateEmployeeModel model, String token) {
        CreateEmployeeFeignRequestDto dto = new CreateEmployeeFeignRequestDto(
                model.getName(),
                model.getLastName(),
                model.getDni(),
                model.getPhone(),
                model.getBirthDate(),
                model.getEmail(),
                model.getPassword()
        );
        UserFeignResponseDto response = userFeignClient.createEmployee(dto, token);
        return response.getId();
    }
}
