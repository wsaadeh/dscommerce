package com.saadeh.dscommerce.repositories;

import com.saadeh.dscommerce.entities.Order;
import com.saadeh.dscommerce.entities.OrderItem;
import com.saadeh.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
