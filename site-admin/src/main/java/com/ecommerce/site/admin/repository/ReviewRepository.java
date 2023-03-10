package com.ecommerce.site.admin.repository;

import com.ecommerce.site.admin.model.entity.Review;
import com.ecommerce.site.admin.repository.CustomPagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface ReviewRepository extends CustomPagingAndSortingRepository<Review, Integer> {
	
	@Query("SELECT r FROM Review r WHERE r.headline LIKE %?1% "
			+ "OR r.comment LIKE %?1% OR r.product.name LIKE %?1% "
			+ "OR CONCAT(r.customer.firstName, ' ', r.customer.lastName) LIKE %?1%")
	Page<Review> findAll(String keyword, Pageable pageable);
}
