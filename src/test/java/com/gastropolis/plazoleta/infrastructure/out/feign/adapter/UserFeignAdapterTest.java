package com.gastropolis.plazoleta.infrastructure.out.feign.adapter;

import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.dto.UserFeignResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFeignAdapterTest {

    @Mock
    private UserFeignClient userFeignClient;

    private UserFeignAdapter userFeignAdapter;

    private UserFeignResponseDto userResponse;
    private final String token = "Bearer mytoken";

    @BeforeEach
    void setUp() {
        userFeignAdapter = new UserFeignAdapter(userFeignClient);

        userResponse = new UserFeignResponseDto(
                1L, "Juan", "Perez", "123456", "+573001234567", "juan@email.com", "CLIENTE"
        );
    }

    @Test
    void getUserRoleById_returnsRoleName() {
        when(userFeignClient.getUserById(1L, token)).thenReturn(userResponse);

        String role = userFeignAdapter.getUserRoleById(1L, token);

        assertEquals("CLIENTE", role);
        verify(userFeignClient).getUserById(1L, token);
    }

    @Test
    void getUserPhoneById_returnsPhone() {
        when(userFeignClient.getUserById(1L, token)).thenReturn(userResponse);

        String phone = userFeignAdapter.getUserPhoneById(1L, token);

        assertEquals("+573001234567", phone);
        verify(userFeignClient).getUserById(1L, token);
    }

    @Test
    void getUserNameById_returnsConcatenatedNameAndLastName() {
        when(userFeignClient.getUserById(1L, token)).thenReturn(userResponse);

        String name = userFeignAdapter.getUserNameById(1L, token);

        assertEquals("Juan Perez", name);
        verify(userFeignClient).getUserById(1L, token);
    }

    @Test
    void createEmployee_buildsRequestDtoAndReturnsId() {
        CreateEmployeeModel model = new CreateEmployeeModel(
                "Ana", "Lopez", "654321", "+573002222222",
                LocalDate.of(1995, 5, 15), "ana@email.com", "pass456"
        );
        UserFeignResponseDto createdUser = new UserFeignResponseDto(
                5L, "Ana", "Lopez", "654321", "+573002222222", "ana@email.com", "EMPLEADO"
        );

        when(userFeignClient.createEmployee(any(), eq(token))).thenReturn(createdUser);

        Long result = userFeignAdapter.createEmployee(model, token);

        assertEquals(5L, result);
        verify(userFeignClient).createEmployee(any(), eq(token));
    }
}
