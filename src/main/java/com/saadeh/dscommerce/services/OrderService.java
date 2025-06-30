package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.OrderDTO;
import com.saadeh.dscommerce.entities.Order;
import com.saadeh.dscommerce.repositories.OrderRepository;
import com.saadeh.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order order = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Recurso não encontrado"));
        return  new OrderDTO(order);
    }
}
