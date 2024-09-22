package com.danley.dscommerce.services;

import com.danley.dscommerce.dto.ProductDto;
import com.danley.dscommerce.models.Product;
import com.danley.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Optional<Product> result = productRepository.findById(id);

        Product product = result.get();
        return new ProductDto(product);
    }
}
