package com.prasun.BootCamp.Service;

import com.prasun.BootCamp.DTOs.Category.CategoryFieldValueResDTO;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Category.CategoryMetadataField;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    CategoryMetadataField addField(String name);
    Category addCategory(String name);
    Category addCategory(String name, long id);
    Category showCategory(long id);

    Category updateCategory(long id ,String name);

    Map<String, List<Category>> viewCategoryById(long id);

    CategoryFieldValueResDTO addMetadataValue(long id , long mid , Set<String> value);

    CategoryFieldValueResDTO updateMetadataValue(long id , long mid , Set<String> value);
}