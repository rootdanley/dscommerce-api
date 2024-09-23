package com.danley.dscommerce.services;

import com.danley.dscommerce.dto.ProductDto;
import com.danley.dscommerce.models.Product;
import com.danley.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return products.map(ProductDto::new);
    }

    @Transactional
    public ProductDto insert(ProductDto productDto) {

        Product entity = new Product();
        copyDtoToEntity(productDto, entity);

        entity = productRepository.save(entity);
        return new ProductDto(entity);
    }


    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        Product entity = productRepository.getReferenceById(id);

        copyDtoToEntity(productDto, entity);

        entity = productRepository.save(entity);
        return new ProductDto(entity);
    }


    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }


    private void copyDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImageUrl());
    }


}
