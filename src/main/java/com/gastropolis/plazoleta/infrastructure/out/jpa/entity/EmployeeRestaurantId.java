package com.gastropolis.plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeRestaurantId implements Serializable {

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}
