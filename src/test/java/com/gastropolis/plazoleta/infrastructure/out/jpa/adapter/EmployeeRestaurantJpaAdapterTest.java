package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantId;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeRestaurantJpaAdapterTest {

    @Mock
    private IEmployeeRestaurantRepository employeeRestaurantRepository;

    @Mock
    private IRestaurantRepository restaurantRepository;

    private EmployeeRestaurantJpaAdapter employeeRestaurantJpaAdapter;

    @BeforeEach
    void setUp() {
        employeeRestaurantJpaAdapter = new EmployeeRestaurantJpaAdapter(
                employeeRestaurantRepository, restaurantRepository);
    }

    @Test
    void saveEmployeeRestaurant_createsCompositeIdAndSavesEntity() {
        Long employeeId = 7L;
        Long restaurantId = 1L;
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(restaurantId);

        when(restaurantRepository.getReferenceById(restaurantId)).thenReturn(restaurantEntity);
        when(employeeRestaurantRepository.save(any(EmployeeRestaurantEntity.class)))
                .thenReturn(new EmployeeRestaurantEntity());

        employeeRestaurantJpaAdapter.saveEmployeeRestaurant(employeeId, restaurantId);

        ArgumentCaptor<EmployeeRestaurantEntity> captor =
                ArgumentCaptor.forClass(EmployeeRestaurantEntity.class);
        verify(employeeRestaurantRepository).save(captor.capture());

        EmployeeRestaurantEntity saved = captor.getValue();
        assertNotNull(saved.getId());
        assertEquals(employeeId, saved.getId().getEmployeeId());
        assertEquals(restaurantId, saved.getId().getRestaurantId());
        assertEquals(restaurantEntity, saved.getRestaurant());
    }

    @Test
    void saveEmployeeRestaurant_callsGetReferenceByIdForRestaurant() {
        Long employeeId = 3L;
        Long restaurantId = 2L;
        RestaurantEntity restaurantEntity = new RestaurantEntity();

        when(restaurantRepository.getReferenceById(restaurantId)).thenReturn(restaurantEntity);
        when(employeeRestaurantRepository.save(any(EmployeeRestaurantEntity.class)))
                .thenReturn(new EmployeeRestaurantEntity());

        employeeRestaurantJpaAdapter.saveEmployeeRestaurant(employeeId, restaurantId);

        verify(restaurantRepository).getReferenceById(restaurantId);
        verify(employeeRestaurantRepository).save(any(EmployeeRestaurantEntity.class));
    }
}
