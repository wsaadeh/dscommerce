package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.OrderDTO;
import com.saadeh.dscommerce.entities.Order;
import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.repositories.OrderRepository;
import com.saadeh.dscommerce.services.exceptions.ForbiddenException;
import com.saadeh.dscommerce.services.exceptions.ResourceNotFoundException;
import com.saadeh.dscommerce.tests.OrderFactory;
import com.saadeh.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    private Long existingOrderId, nonExistingOrderId;
    private Order order;
    private OrderDTO dto;
    private User admin, client;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Jef", "jef@gmail.com");
        client = UserFactory.createCustomClientUser(2L, "Bob", "bob@gmail.com");

        order = OrderFactory.createOrder(client);
        dto = OrderFactory.createOrderDTO(client);

        Mockito.doReturn(Optional.of(order)).when(repository).findById(existingOrderId);
        Mockito.doReturn(Optional.empty()).when(repository).findById(nonExistingOrderId);

        Mockito.doNothing().when(authService).validateSelfOrAdmin(order.getClient().getId());

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> dto = service.findById(nonExistingOrderId));
    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = service.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenOrderExistAndAdminLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        dto = service.findById(existingOrderId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), order.getId());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenOrderExistAndSelfClientLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        dto = service.findById(existingOrderId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getId(), order.getId());
    }


}
