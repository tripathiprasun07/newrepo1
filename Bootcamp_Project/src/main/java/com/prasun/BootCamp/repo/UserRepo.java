package com.prasun.BootCamp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prasun.BootCamp.Model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Optional<User> findById(Long id);

    @Modifying
    @Query(value = "update user u set u.is_active=TRUE where u.id=?1", nativeQuery = true)
    @Transactional
    void setActive(@Param("id") Long id);


    @Modifying
    @Query(value = "update user u set u.is_active=FALSE where u.id=?1", nativeQuery = true)
    @Transactional
    void setUnActive(@Param("id") Long id);
}
