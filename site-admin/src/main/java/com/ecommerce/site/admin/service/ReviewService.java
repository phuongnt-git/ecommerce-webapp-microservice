package com.ecommerce.site.admin.service;

import java.util.NoSuchElementException;

import com.ecommerce.site.admin.exception.ReviewNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Review;
import com.ecommerce.site.admin.repository.ProductRepository;
import com.ecommerce.site.admin.repository.ReviewRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.REVIEWS_PER_PAGE;


/**
 * @author Nguyen Thanh Phuong
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReviewService {

	
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ProductRepository productRepository;
	
	public void listByPage(int pageNum, @NotNull PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepository);
	}
	
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return reviewRepository.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new ReviewNotFoundException(String.format("Could not find any reviews with ID %s", id));
		}
	}
	
	public void save(@NotNull Review reviewInForm) {
		Review reviewInDb = reviewRepository.findById(reviewInForm.getId()).get();
		reviewInDb.setHeadline(reviewInForm.getHeadline());
		reviewInDb.setComment(reviewInForm.getComment());
		
		reviewRepository.save(reviewInDb);
		//productRepository.updateReviewCountAndAverageRating(reviewInDb.getProduct().getId());
	}
	
	public void delete(Integer id) throws ReviewNotFoundException {
		if (!reviewRepository.existsById(id)) {
			throw new ReviewNotFoundException(String.format("Could not find any reviews with ID %s", id));
		}
		reviewRepository.deleteById(id);
	}
}
