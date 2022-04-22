package com.prasun.BootCamp.repo;

import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface SellerRepo extends JpaRepository<Seller,Long> {
    Seller findByGst(String gst);
    Seller findByCompanyName(String CompanyName);
    Seller findByUserId(User user);
    Seller findByUserId(long id);

}
