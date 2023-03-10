package com.ecommerce.site.admin.model.dto;

import com.ecommerce.site.admin.model.entity.Product;

import java.io.Serializable;

/**
 * A DTO for the {@link Product} entity
 *
 * @author Nguyen Thanh Phuong
 */
public record ProductDto(String name, String imagePath, float price, float cost) implements Serializable {
}