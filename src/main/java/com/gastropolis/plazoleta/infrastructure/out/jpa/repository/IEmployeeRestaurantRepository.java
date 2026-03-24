package com.gastropolis.plazoleta.infrastructure.out.jpa.repository;

import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurantEntity, EmployeeRestaurantId> {
    Optional<EmployeeRestaurantEntity> findByIdEmployeeId(Long employeeId);
}
