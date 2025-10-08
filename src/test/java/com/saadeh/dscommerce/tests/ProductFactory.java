package com.saadeh.dscommerce.tests;

import com.saadeh.dscommerce.dto.ProductDTO;
import com.saadeh.dscommerce.entities.Category;
import com.saadeh.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct(){
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L,"Laptop Lenovo", "Core 5,1Tb SSD,DDR5 32Gb,Keyboard backlight",3000.0,"http://lenovo.com/laptop.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name){
        Product product = createProduct();
        product.setName(name);
        return product;
    }

    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct());
    }
}
