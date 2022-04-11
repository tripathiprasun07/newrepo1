package com.prasun.BootCamp.repo;

import com.prasun.BootCamp.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
