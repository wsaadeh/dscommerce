package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.ProductDTO;
import com.saadeh.dscommerce.dto.ProductMinDTO;
import com.saadeh.dscommerce.entities.Product;
import com.saadeh.dscommerce.repositories.ProductRepository;
import com.saadeh.dscommerce.services.exceptions.ResourceNotFoundException;
import com.saadeh.dscommerce.tests.ProductFactory;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;


    private Long existingProductId;
    private Long nonExistingProductId;
    private Product product;
    private ProductDTO dto;
    private String productName;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingProductId = 1L;
        nonExistingProductId = 2L;

        productName = "Laptop Lenovo";

        product = ProductFactory.createProduct(productName);
        dto = ProductFactory.createProductDTO();

        Pageable pageable = PageRequest.of(0,12);
        page = new PageImpl<>(List.of(product),pageable,1);

        Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        Mockito.when(repository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(repository.searchByName(any(),(Pageable)any())).thenReturn(page);
//        Mockito.when(repository.searchByName(productName,pageable)).thenReturn(page);

        Mockito.when(repository.save(any())).thenReturn(product);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingProductId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());

    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.findById(nonExistingProductId);
        });

    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO(){
        Pageable pageable = PageRequest.of(0,12);
        String name = "Laptop";

        Page<ProductMinDTO> result = service.findAll(name,pageable);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnProductDTO(){
        ProductDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(),dto.getId());
        Assertions.assertEquals(result.getName(),dto.getName());

    }

    @Test
    public void updateShouldReturnProductDTOWhenDataIsValid(){
        ProductDTO result = service.update(existingProductId,dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(),dto.getId());
        Assertions.assertEquals(result.getName(),dto.getName());
    }

    @Test
    public void updateShouldRaiseResourceNotFoundExceptionWhenDataDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            ProductDTO result = service.update(nonExistingProductId,dto);
        });

    }


}
