package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.exception.ReviewNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Review;
import com.ecommerce.site.admin.service.ReviewService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * @author Nguyen Thanh Phuong
 */
@Controller
@RequestMapping("/reviews")
public class ReviewController {

	private final String defaultRedirectUrl = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";
	
	@Autowired
	private ReviewService service;
	
	@GetMapping("")
	public String listFirstPage() {
		return defaultRedirectUrl;
	}

	@GetMapping("/page/{pageNumber}")
	public String listByPage(@PagingAndSortingParam(listName = "listReviews", moduleUrl = "/reviews") PagingAndSortingHelper helper,
			@PathVariable("pageNumber") int pageNumber) {
		service.listByPage(pageNumber, helper);
		return "reviews/reviews";
	}
	
	@GetMapping("/detail/{id}")
	public String viewReview(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes attributes) {
		try {
			Review review = service.get(id);
			model.addAttribute("review", review);
			return "reviews/review_detail_modal";
		} catch (ReviewNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
			return defaultRedirectUrl;
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editReview(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes ra) {
		try {
			Review review = service.get(id);
			model.addAttribute("review", review);
			model.addAttribute("pageTitle", String.format("Edit Review (ID %d)", id));
			
			return "reviews/review_form";
		} catch (ReviewNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectUrl;
		}
	}	
	
	@PostMapping("/save")
	public String saveReview(Review reviewInForm, @NotNull RedirectAttributes attributes) {
		service.save(reviewInForm);		
		attributes.addFlashAttribute("message", String.format("The review ID %s has been updated successfully", reviewInForm.getId()));
		return defaultRedirectUrl;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteReview(@PathVariable("id") Integer id, @NotNull RedirectAttributes attributes) {
		try {
			service.delete(id);
			attributes.addFlashAttribute("message", String.format("The review ID %s has been deleted", id));
		} catch (ReviewNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectUrl;
	}

}
