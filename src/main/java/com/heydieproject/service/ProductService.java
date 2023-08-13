package com.heydieproject.service;

import com.heydieproject.entity.Product;
import com.heydieproject.dto.ProductDto;
import com.heydieproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Product createNew(Product product) {
        product.setCreatedAt(LocalDateTime.now().toString());
        product.setUpdatedAt(LocalDateTime.now().toString());
        ProductDto productDto = new ProductDto("create", repository.save(product));
        kafkaTemplate.send("product-cqrs",productDto);
        return productDto.getProduct();
    }

    public Product updateProduct(Long id, Product product) {
        try {
            Product existingProduct = repository.findById(id).get();

            existingProduct.setProductName(product.getProductName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setProductImg(product.getProductImg());
            existingProduct.setCreatedAt(product.getCreatedAt());
            existingProduct.setUpdatedAt(LocalDateTime.now().toString());

            ProductDto productDto = new ProductDto("update", existingProduct);

            kafkaTemplate.send("product-cqrs", productDto);

            return repository.save(existingProduct);
        } catch (Exception e) {
            throw new RuntimeException("Error ==> "+ e.getMessage());
        }

    }
}
