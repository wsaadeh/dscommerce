package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.OrderDTO;
import com.saadeh.dscommerce.dto.OrderItemDTO;
import com.saadeh.dscommerce.dto.ProductDTO;
import com.saadeh.dscommerce.entities.Order;
import com.saadeh.dscommerce.entities.OrderItem;
import com.saadeh.dscommerce.entities.OrderStatus;
import com.saadeh.dscommerce.entities.Product;
import com.saadeh.dscommerce.repositories.OrderItemRepository;
import com.saadeh.dscommerce.repositories.OrderRepository;
import com.saadeh.dscommerce.repositories.ProductRepository;
import com.saadeh.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order order = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Recurso não encontrado"));
        return  new OrderDTO(order);
    }

    @Transactional
    public @Valid OrderDTO insert(@Valid OrderDTO dto) {
        Order order = new Order();
        copyDtoToEntity(dto,order);
        order = repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }

    private void copyDtoToEntity(@Valid OrderDTO dto, Order order) {
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        order.setClient(userService.authenticated());

        for (OrderItemDTO item: dto.getItems()){
            Product product = productRepository.getReferenceById(item.getProductId());
            OrderItem orderItem = new OrderItem(order, product,item.getQuantity(),product.getPrice());
            order.getItems().add(orderItem);
        }

    }
}
