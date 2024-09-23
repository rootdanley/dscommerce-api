package com.danley.dscommerce.services;

import com.danley.dscommerce.dto.ProductDto;
import com.danley.dscommerce.models.Product;
import com.danley.dscommerce.repositories.ProductRepository;
import com.danley.dscommerce.services.exceptions.DatabaseException;
import com.danley.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso nao encontrado."));

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


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }


    private void copyDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImageUrl());
    }


}
