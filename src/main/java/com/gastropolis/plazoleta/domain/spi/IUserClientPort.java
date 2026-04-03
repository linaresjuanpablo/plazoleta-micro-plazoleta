package com.gastropolis.plazoleta.domain.spi;

import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;

public interface IUserClientPort {
    String getUserRoleById(Long userId, String token);
    String getUserPhoneById(Long userId, String token);
    String getUserNameById(Long userId, String token);
    Long createEmployee(CreateEmployeeModel model, String token);
    Long getOwnerIdByDni(String dni, String token);
}
