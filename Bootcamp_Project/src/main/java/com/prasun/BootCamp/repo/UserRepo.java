package com.prasun.BootCamp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prasun.BootCamp.Model.ApplicationUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<ApplicationUser, Integer> {
     ApplicationUser findByEmail(String email);
    Optional<ApplicationUser> findById(Long id);
    @Modifying
    @Query(value = "update user u set u.is_active=TRUE where u.id=?1",nativeQuery = true)
    @Transactional
    void setActive(@Param("id") Long id);



    @Modifying
    @Query(value = "update user u set u.is_active=FALSE where u.id=?1",nativeQuery = true)
    @Transactional
    void setUnActive(@Param("id") Long id);
}
