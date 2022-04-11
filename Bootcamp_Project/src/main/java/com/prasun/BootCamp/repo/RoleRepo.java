package com.prasun.BootCamp.repo;

import com.prasun.BootCamp.Enums.Role_Enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prasun.BootCamp.Model.Role;

import java.util.Set;


@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
   Set<Role> findByName(String name);

}
