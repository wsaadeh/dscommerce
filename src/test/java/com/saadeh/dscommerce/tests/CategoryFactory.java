package com.saadeh.dscommerce.tests;

import com.saadeh.dscommerce.dto.CategoryDTO;
import com.saadeh.dscommerce.entities.Category;

public class CategoryFactory {
    public static Category createCategory(){
        return new Category(1L, "Electronics");
    }

    public static Category createCategory(Long id,String name){
        return new Category(id,name);
    }

    public static CategoryDTO createCategoryDTO(){
        return new CategoryDTO(createCategory());
    }
}
