package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.CategoryDTO;
import com.saadeh.dscommerce.entities.Category;
import com.saadeh.dscommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> categoryList = repository.findAll();
        return categoryList.stream().map(x-> new CategoryDTO(x)).toList();
    }

}
