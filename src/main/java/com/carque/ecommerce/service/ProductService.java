package com.carque.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carque.ecommerce.exception.ResourceNotFoundException;
import com.carque.ecommerce.model.Product;
import com.carque.ecommerce.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {
        Product ExistingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ExistingProduct.setName(product.getName());
        ExistingProduct.setPrice(product.getPrice());

        return productRepository.save(ExistingProduct);
    }

    public void deleteProduct(Long id) {
        Product existiProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(existiProduct);
    }
}
