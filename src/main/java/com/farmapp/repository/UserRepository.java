package com.farmapp.repository;

import com.farmapp.enums.UserRole;
import com.farmapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByName(String name) ;
    List<User> findAllByRoleAndActiveTrue(UserRole role);
    Optional<User> findById(int id);
    Optional<User> findByPhone(String phone);
}
