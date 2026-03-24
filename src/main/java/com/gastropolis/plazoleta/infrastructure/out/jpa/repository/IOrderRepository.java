package com.gastropolis.plazoleta.infrastructure.out.jpa.repository;

import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByRestaurantId(Long restaurantId, Pageable pageable);
    Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable);
    List<OrderEntity> findByClientIdAndStatusIn(Long clientId, List<String> statuses);
}
