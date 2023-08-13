package com.heydieproject.dto;

import com.heydieproject.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String eventKafka;
    private Product product;
}
