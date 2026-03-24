package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantId;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderJpaAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IEmployeeRestaurantRepository employeeRestaurantRepository;

    private OrderJpaAdapter orderJpaAdapter;

    private OrderEntity orderEntity;
    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        orderJpaAdapter = new OrderJpaAdapter(orderRepository, orderEntityMapper,
                restaurantRepository, dishRepository, employeeRestaurantRepository);

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setStatus("PENDIENTE");
        orderEntity.setClientId(5L);
        orderEntity.setDate(LocalDateTime.now());
        orderEntity.setOrderDishes(new ArrayList<>());

        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setStatus("PENDIENTE");
        orderModel.setClientId(5L);
        orderModel.setRestaurantId(1L);
    }

    // ---- saveOrder (new - id is null) ----

    @Test
    void saveOrder_whenIdIsNull_andDishesIsNull_createsNewEntityWithoutDishes() {
        orderModel.setId(null);
        orderModel.setDishes(null);

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        OrderEntity savedOnce = new OrderEntity();
        savedOnce.setId(10L);
        savedOnce.setOrderDishes(new ArrayList<>());

        when(restaurantRepository.getReferenceById(1L)).thenReturn(restaurantEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOnce);
        when(orderEntityMapper.toModel(savedOnce)).thenReturn(orderModel);

        OrderModel result = orderJpaAdapter.saveOrder(orderModel);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(dishRepository, never()).getReferenceById(any());
    }

    @Test
    void saveOrder_whenIdIsNull_andHasDishes_savesOrderAndDishes() {
        orderModel.setId(null);
        OrderDishModel dishModel = new OrderDishModel(null, 10L, 2);
        orderModel.setDishes(List.of(dishModel));

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        DishEntity dishEntity = new DishEntity();

        OrderEntity firstSaved = new OrderEntity();
        firstSaved.setId(10L);
        firstSaved.setOrderDishes(new ArrayList<>());

        OrderEntity secondSaved = new OrderEntity();
        secondSaved.setId(10L);
        secondSaved.setOrderDishes(new ArrayList<>());

        when(restaurantRepository.getReferenceById(1L)).thenReturn(restaurantEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(firstSaved).thenReturn(secondSaved);
        when(dishRepository.getReferenceById(10L)).thenReturn(dishEntity);
        when(orderEntityMapper.toModel(secondSaved)).thenReturn(orderModel);

        OrderModel result = orderJpaAdapter.saveOrder(orderModel);

        assertNotNull(result);
        verify(orderRepository, times(2)).save(any(OrderEntity.class));
        verify(dishRepository).getReferenceById(10L);
    }

    // ---- saveOrder (update - id is not null) ----

    @Test
    void saveOrder_whenIdIsNotNull_andEntityExists_updatesFieldsAndSaves() {
        orderModel.setStatus("EN_PREPARACION");
        orderModel.setEmployeeId(7L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderEntityMapper.toModel(orderEntity)).thenReturn(orderModel);

        OrderModel result = orderJpaAdapter.saveOrder(orderModel);

        assertNotNull(result);
        assertEquals("EN_PREPARACION", orderEntity.getStatus());
        assertEquals(7L, orderEntity.getEmployeeId());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(orderEntity);
    }

    @Test
    void saveOrder_whenIdIsNotNull_andEntityNotFound_createsNewEntityAndSaves() {
        orderModel.setStatus("EN_PREPARACION");

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        OrderEntity newEntity = new OrderEntity();
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(newEntity);
        when(orderEntityMapper.toModel(newEntity)).thenReturn(orderModel);

        OrderModel result = orderJpaAdapter.saveOrder(orderModel);

        assertNotNull(result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(OrderEntity.class));
    }

    // ---- findById ----

    @Test
    void findById_whenExists_returnsModel() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderEntityMapper.toModel(orderEntity)).thenReturn(orderModel);

        OrderModel result = orderJpaAdapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        OrderModel result = orderJpaAdapter.findById(99L);

        assertNull(result);
        verify(orderEntityMapper, never()).toModel(any());
    }

    // ---- findByRestaurantId ----

    @Test
    void findByRestaurantId_returnsPageOfModels() {
        Page<OrderEntity> entityPage = new PageImpl<>(List.of(orderEntity));
        when(orderRepository.findByRestaurantId(1L, PageRequest.of(0, 10))).thenReturn(entityPage);
        when(orderEntityMapper.toModel(orderEntity)).thenReturn(orderModel);

        Page<OrderModel> result = orderJpaAdapter.findByRestaurantId(1L, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(orderRepository).findByRestaurantId(1L, PageRequest.of(0, 10));
    }

    // ---- findByRestaurantIdAndStatus ----

    @Test
    void findByRestaurantIdAndStatus_returnsPageOfModels() {
        Page<OrderEntity> entityPage = new PageImpl<>(List.of(orderEntity));
        when(orderRepository.findByRestaurantIdAndStatus(1L, "PENDIENTE", PageRequest.of(0, 10))).thenReturn(entityPage);
        when(orderEntityMapper.toModel(orderEntity)).thenReturn(orderModel);

        Page<OrderModel> result = orderJpaAdapter.findByRestaurantIdAndStatus(1L, "PENDIENTE", 0, 10);

        assertEquals(1, result.getContent().size());
        verify(orderRepository).findByRestaurantIdAndStatus(1L, "PENDIENTE", PageRequest.of(0, 10));
    }

    // ---- findActiveOrdersByClientId ----

    @Test
    void findActiveOrdersByClientId_returnsListOfModels() {
        List<String> statuses = List.of("PENDIENTE", "EN_PREPARACION");
        when(orderRepository.findByClientIdAndStatusIn(5L, statuses)).thenReturn(List.of(orderEntity));
        when(orderEntityMapper.toModelList(List.of(orderEntity))).thenReturn(List.of(orderModel));

        List<OrderModel> result = orderJpaAdapter.findActiveOrdersByClientId(5L, statuses);

        assertEquals(1, result.size());
        verify(orderRepository).findByClientIdAndStatusIn(5L, statuses);
        verify(orderEntityMapper).toModelList(List.of(orderEntity));
    }

    // ---- findRestaurantIdByEmployeeId ----

    @Test
    void findRestaurantIdByEmployeeId_whenFound_returnsRestaurantId() {
        EmployeeRestaurantId empRestId = new EmployeeRestaurantId(7L, 1L);
        EmployeeRestaurantEntity empRestEntity = new EmployeeRestaurantEntity(empRestId, new RestaurantEntity());

        when(employeeRestaurantRepository.findByIdEmployeeId(7L)).thenReturn(Optional.of(empRestEntity));

        Long result = orderJpaAdapter.findRestaurantIdByEmployeeId(7L);

        assertEquals(1L, result);
        verify(employeeRestaurantRepository).findByIdEmployeeId(7L);
    }

    @Test
    void findRestaurantIdByEmployeeId_whenNotFound_returnsNull() {
        when(employeeRestaurantRepository.findByIdEmployeeId(99L)).thenReturn(Optional.empty());

        Long result = orderJpaAdapter.findRestaurantIdByEmployeeId(99L);

        assertNull(result);
        verify(employeeRestaurantRepository).findByIdEmployeeId(99L);
    }
}
