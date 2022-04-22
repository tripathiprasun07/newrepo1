package com.prasun.BootCamp.Model.Category;

import com.prasun.BootCamp.Model.Auditable;
import com.prasun.BootCamp.Model.Category.CategoryMetadataFieldValues;
import com.prasun.BootCamp.Model.Product.Product;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Set;
@Entity
@EnableJpaAuditing
public class Category extends Auditable<String> {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private Long parentCategoryId;


    @OneToMany(mappedBy = "category")
    Set<CategoryMetadataFieldValues> categoryMetadataFieldValues;


    @OneToMany(mappedBy = "categoryId")
    private Set<Product> product;

    public Set<CategoryMetadataFieldValues> getCategoryMetadataFieldValues() {
        return categoryMetadataFieldValues;
    }

    public void setCategoryMetadataFieldValues(Set<CategoryMetadataFieldValues> categoryMetadataFieldValues) {
        this.categoryMetadataFieldValues = categoryMetadataFieldValues;
    }

    public Set<Product> getProduct() {
        return product;
    }

    public void setProduct(Set<Product> product) {
        this.product = product;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}