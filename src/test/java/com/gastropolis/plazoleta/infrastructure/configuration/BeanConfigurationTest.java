package com.gastropolis.plazoleta.infrastructure.configuration;

import com.gastropolis.plazoleta.domain.api.IDishServicePort;
import com.gastropolis.plazoleta.domain.api.IOrderServicePort;
import com.gastropolis.plazoleta.domain.api.IRestaurantServicePort;
import com.gastropolis.plazoleta.domain.spi.*;
import com.gastropolis.plazoleta.infrastructure.out.feign.MessagingFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.TraceabilityFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.jpa.adapter.*;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeanConfigurationTest {

    @Mock private IRestaurantRepository restaurantRepository;
    @Mock private IRestaurantEntityMapper restaurantEntityMapper;
    @Mock private IDishRepository dishRepository;
    @Mock private IDishEntityMapper dishEntityMapper;
    @Mock private ICategoryRepository categoryRepository;
    @Mock private IOrderRepository orderRepository;
    @Mock private IOrderEntityMapper orderEntityMapper;
    @Mock private IDishRepository dishRepository2;
    @Mock private IEmployeeRestaurantRepository employeeRestaurantRepository;
    @Mock private UserFeignClient userFeignClient;
    @Mock private TraceabilityFeignClient traceabilityFeignClient;
    @Mock private MessagingFeignClient messagingFeignClient;
    @Mock private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock private IDishPersistencePort dishPersistencePort;
    @Mock private IOrderPersistencePort orderPersistencePort;
    @Mock private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    @Mock private IUserClientPort userClientPort;
    @Mock private ITraceabilityClientPort traceabilityClientPort;
    @Mock private IMessagingClientPort messagingClientPort;

    private BeanConfiguration beanConfiguration;

    @BeforeEach
    void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    void restaurantPersistencePort_returnsRestaurantJpaAdapter() {
        IRestaurantPersistencePort port = beanConfiguration.restaurantPersistencePort(
                restaurantRepository, restaurantEntityMapper);

        assertNotNull(port);
        assertInstanceOf(RestaurantJpaAdapter.class, port);
    }

    @Test
    void dishPersistencePort_returnsDishJpaAdapter() {
        IDishPersistencePort port = beanConfiguration.dishPersistencePort(
                dishRepository, dishEntityMapper, categoryRepository, restaurantRepository);

        assertNotNull(port);
        assertInstanceOf(DishJpaAdapter.class, port);
    }

    @Test
    void orderPersistencePort_returnsOrderJpaAdapter() {
        IOrderPersistencePort port = beanConfiguration.orderPersistencePort(
                orderRepository, orderEntityMapper, restaurantRepository, dishRepository, employeeRestaurantRepository);

        assertNotNull(port);
        assertInstanceOf(OrderJpaAdapter.class, port);
    }

    @Test
    void employeeRestaurantPersistencePort_returnsEmployeeRestaurantJpaAdapter() {
        IEmployeeRestaurantPersistencePort port = beanConfiguration.employeeRestaurantPersistencePort(
                employeeRestaurantRepository, restaurantRepository);

        assertNotNull(port);
        assertInstanceOf(EmployeeRestaurantJpaAdapter.class, port);
    }

    @Test
    void userClientPort_returnsUserFeignAdapter() {
        IUserClientPort port = beanConfiguration.userClientPort(userFeignClient);

        assertNotNull(port);
    }

    @Test
    void traceabilityClientPort_returnsTraceabilityFeignAdapter() {
        ITraceabilityClientPort port = beanConfiguration.traceabilityClientPort(traceabilityFeignClient);

        assertNotNull(port);
    }

    @Test
    void messagingClientPort_returnsMessagingFeignAdapter() {
        IMessagingClientPort port = beanConfiguration.messagingClientPort(messagingFeignClient);

        assertNotNull(port);
    }

    @Test
    void restaurantServicePort_returnsRestaurantUseCase() {
        IRestaurantServicePort port = beanConfiguration.restaurantServicePort(
                restaurantPersistencePort, userClientPort, employeeRestaurantPersistencePort);

        assertNotNull(port);
    }

    @Test
    void dishServicePort_returnsDishUseCase() {
        IDishServicePort port = beanConfiguration.dishServicePort(
                dishPersistencePort, restaurantPersistencePort);

        assertNotNull(port);
    }

    @Test
    void orderServicePort_returnsOrderUseCase() {
        IOrderServicePort port = beanConfiguration.orderServicePort(
                orderPersistencePort, dishPersistencePort, restaurantPersistencePort,
                traceabilityClientPort, messagingClientPort, userClientPort);

        assertNotNull(port);
    }
}
