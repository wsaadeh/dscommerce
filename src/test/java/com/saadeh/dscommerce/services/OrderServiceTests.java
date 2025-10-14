package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.OrderDTO;
import com.saadeh.dscommerce.dto.ProductDTO;
import com.saadeh.dscommerce.dto.UserDto;
import com.saadeh.dscommerce.entities.Order;
import com.saadeh.dscommerce.entities.OrderItem;
import com.saadeh.dscommerce.entities.Product;
import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.repositories.OrderItemRepository;
import com.saadeh.dscommerce.repositories.OrderRepository;
import com.saadeh.dscommerce.repositories.ProductRepository;
import com.saadeh.dscommerce.services.exceptions.ForbiddenException;
import com.saadeh.dscommerce.services.exceptions.ResourceNotFoundException;
import com.saadeh.dscommerce.tests.OrderFactory;
import com.saadeh.dscommerce.tests.ProductFactory;
import com.saadeh.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Order order;
    private OrderDTO dto;
    private User admin, client;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        existingProductId = 1L;
        nonExistingProductId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Jef", "jef@gmail.com");
        client = UserFactory.createCustomClientUser(2L, "Bob", "bob@gmail.com");

        order = OrderFactory.createOrder(client);
        dto = OrderFactory.createOrderDTO(client);

        product = ProductFactory.createProduct();

        Mockito.doReturn(Optional.of(order)).when(repository).findById(existingOrderId);
        Mockito.doReturn(Optional.empty()).when(repository).findById(nonExistingOrderId);

        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(authService).validateSelfOrAdmin(order.getClient().getId());

        Mockito.when(repository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> dto = service.findById(nonExistingOrderId));
    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> service.findById(existingOrderId));
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
    }

    @Test
    public void inserShouldReturnOrderDTOWhenAdminLogged(){
        Mockito.when(userService.authenticated()).thenReturn(admin);

        OrderDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void inserShouldReturnOrderDTOWhenClientLogged(){
        Mockito.when(userService.authenticated()).thenReturn(client);

        OrderDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(),existingOrderId);
    }

    @Test
    public void insertShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        dto = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class,()->{
            service.insert(dto);
        });
    }

    @Test
    public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExist(){
        Mockito.when(userService.authenticated()).thenReturn(client);

        product.setId(nonExistingProductId);
        OrderItem orderItem = new OrderItem(order,product,2,10.0);
        order.getItems().add(orderItem);

        dto = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class,()->{
            service.insert(dto);
        });
    }

}
