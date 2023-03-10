package com.ecommerce.site.shop.model.dto;

import com.ecommerce.site.shop.model.entity.State;

import java.io.Serializable;

/**
 * A DTO for the {@link State} entity
 */
public record StateDto(Integer id, String name) implements Serializable {
}