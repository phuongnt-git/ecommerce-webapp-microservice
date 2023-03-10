package com.ecommerce.site.shop.repository;

import com.ecommerce.site.shop.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}