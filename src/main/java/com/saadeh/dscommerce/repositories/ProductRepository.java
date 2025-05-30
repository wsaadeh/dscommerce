package com.saadeh.dscommerce.repositories;

import com.saadeh.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
