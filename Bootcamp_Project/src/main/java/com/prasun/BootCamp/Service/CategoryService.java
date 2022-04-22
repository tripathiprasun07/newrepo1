package com.prasun.BootCamp.Service;


import com.prasun.BootCamp.DTOs.Category.CategoryDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryFieldValueResDTO;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Category.CategoryMetadataField;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    CategoryMetadataField addField(String name);
    Category addCategory(String name);
    Category addCategory(String name, Long id);


    Category viewCategory(Long id);

    Category updateCategory(Long id ,String name);

    Map<String, List<CategoryDTO>> viewCategoryById(Long id);

    CategoryFieldValueResDTO addMetadataValue(Long id , Long mid , Set<String> value);

    CategoryFieldValueResDTO updateMetadataValue(Long id , Long mid , Set<String> value);
}