//package com.prasun.BootCamp.repo.ProductRepo;
//
//
//import com.prasun.BootCamp.Model.Product.Product;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {
//
//    List<ProductVariation> findAllByProduct(Product product, Pageable pageable);
//
//    List<ProductVariation> findAllByProduct(Product product);
//
//}
