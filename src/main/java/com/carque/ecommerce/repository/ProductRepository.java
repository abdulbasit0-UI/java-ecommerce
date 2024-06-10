
package com.carque.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carque.ecommerce.model.Product;

/**
 * ProductRepository
 */

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

}