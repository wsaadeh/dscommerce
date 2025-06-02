package com.saadeh.dscommerce.controllers;

import com.saadeh.dscommerce.dto.ProductDTO;
import com.saadeh.dscommerce.entities.Product;
import com.saadeh.dscommerce.repositories.ProductRepository;
import com.saadeh.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping(value = "/{id}")
    public ProductDTO findById(@PathVariable Long id){
       return  service.findById(id);
    }

    @GetMapping
    public Page<ProductDTO> findALL(Pageable pageable){
        return  service.findAll(pageable);
    }

    @PostMapping
    public ProductDTO insert(@RequestBody ProductDTO productDTO){
        return service.insert(productDTO);
    }


}
