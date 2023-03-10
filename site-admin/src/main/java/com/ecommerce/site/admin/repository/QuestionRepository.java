package com.ecommerce.site.admin.repository;

import com.ecommerce.site.admin.model.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface QuestionRepository extends CustomPagingAndSortingRepository<Question, Integer> {

	@Query("SELECT q FROM Question q WHERE q.questionContent LIKE %?1% "
			+ "OR q.answer LIKE %?1% OR q.product.name LIKE %?1% "
			+ "OR CONCAT(q.asker.firstName, ' ', q.asker.lastName) LIKE %?1%")
	Page<Question> findAll(String keyword, Pageable pageable);

	@Modifying
	@Query("UPDATE Question p SET p.approved = ?2 WHERE p.id = ?1")
	void updateApprovalStatus(Integer id, boolean enabled);
	
}
