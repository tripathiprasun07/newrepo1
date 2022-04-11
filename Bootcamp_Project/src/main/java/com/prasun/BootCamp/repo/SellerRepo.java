package com.prasun.BootCamp.repo;

import com.prasun.BootCamp.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller,Long> {
}
