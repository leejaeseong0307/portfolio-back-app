package com.recommand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommand.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String email);
    
    Optional<User> findByUserId(String id);
    
}