package com.saadeh.dscommerce.tests;

import com.saadeh.dscommerce.dto.OrderDTO;
import com.saadeh.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {
    public static Order createOrder(User user) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user, new Payment());

        Product product = ProductFactory.createProduct();

        OrderItem orderItem = new OrderItem(order,product,2,10.0);
        order.getItems().add(orderItem);

        return order;
    }

    public static OrderDTO createOrderDTO(User user) {
        return new OrderDTO(createOrder(user));
    }
}
