package com.gastropolis.plazoleta.infrastructure.out.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeFeignRequestDto {

    private String name;
    private String lastName;
    private String dni;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;
}
