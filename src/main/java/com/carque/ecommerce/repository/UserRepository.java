package com.carque.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carque.ecommerce.model.User;

/**
 * UserRepository
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}