package com.prasun.BootCamp.repo.CategoryRepo;
import com.prasun.BootCamp.Model.Category.CategoryMetadataField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface CategoryMetadataFieldRepo extends JpaRepository<CategoryMetadataField,Long> {
    CategoryMetadataField findByName(String name);
    CategoryMetadataField findById(long id);
    List<CategoryMetadataField> findAll(Sort sort);
}