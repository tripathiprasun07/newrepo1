package com.prasun.BootCamp.repo;

import com.prasun.BootCamp.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {
}
