package com.ecommerce.site.shop.repository;


import com.ecommerce.site.shop.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();

    Country findByCode(String code);

}
