package com.ecommerce.site.admin.model.dto;

import com.ecommerce.site.admin.model.entity.State;

import java.io.Serializable;

/**
 * A DTO for the {@link State} entity
 */
public record StateDto(Integer id, String name) implements Serializable {
}