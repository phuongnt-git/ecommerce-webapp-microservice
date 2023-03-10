package com.ecommerce.site.admin.controller;


import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.exception.QuestionNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Question;
import com.ecommerce.site.admin.security.UserDetailsImpl;
import com.ecommerce.site.admin.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author nguyenthanhphuong
 */
@Controller
@RequestMapping("/questions")
public class QuestionController {

	private final String defaultRedirectUrl = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
	
	@Autowired
	private QuestionService service;
	
	@GetMapping("")
	public String listFirstPage() {
		return defaultRedirectUrl;
	}
	
	@GetMapping("/page/{pageNumber}")
	public String listByPage(@PagingAndSortingParam(listName = "listQuestions", moduleUrl = "/questions") PagingAndSortingHelper helper,
							 @PathVariable("pageNumber") int pageNumber) {
		service.listByPage(pageNumber, helper);
		
		return "questions/questions";		
	}
	
	@GetMapping("/detail/{id}")
	public String viewQuestion(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes attributes) {
		try {
			Question question = service.get(id);
			model.addAttribute("question", question);
			
			return "questions/question_detail_modal";
		} catch (QuestionNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());

			return defaultRedirectUrl;
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editQuestion(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes attributes) {
		try {
			Question question = service.get(id);
			model.addAttribute("question", question);
			model.addAttribute("pageTitle", String.format("Edit Question (ID %s)", id));

			return "questions/question_form";
		} catch (QuestionNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
			
			return defaultRedirectUrl;
		}
	}
	
	@PostMapping("/save")
	public String saveQuestion(Question question, @NotNull RedirectAttributes attributes,
							   @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
		try {
			service.save(question, userDetails.getUser());
			attributes.addFlashAttribute("message", String.format("The Question ID %s has been updated successfully", question.getId()));
		} catch (QuestionNotFoundException ex) {
			attributes.addFlashAttribute("message", String.format("Could not find any question with ID %s", question.getId()));
		}
		return defaultRedirectUrl;
	}
	
	@GetMapping("/{id}/approve")
	public String approveQuestion(@PathVariable("id") Integer id, @NotNull RedirectAttributes attributes, @NotNull HttpServletRequest request) {
		service.approve(id);
		attributes.addFlashAttribute("message", String.format("The Question ID %s has been approved", id));
		return String.format("redirect:%s", request.getHeader("Referer"));
	}
	
	@GetMapping("/{id}/disapprove")
	public String disapproveQuestion(@PathVariable("id") Integer id, @NotNull RedirectAttributes attributes, @NotNull HttpServletRequest request) {
		service.disapprove(id);
		attributes.addFlashAttribute("message", String.format("The Question ID %s has been disapproved", id));
		return String.format("redirect:%s", request.getHeader("Referer"));
	}
	
	@GetMapping("/delete/{id}")
	public String deleteQuestion(@PathVariable("id") Integer id, @NotNull RedirectAttributes attributes, HttpServletRequest request) {
		try {
			service.delete(id);
			attributes.addFlashAttribute("message", String.format("The question ID %s has been deleted successfully", id));
		} catch (QuestionNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return String.format("redirect:%s", request.getHeader("Referer"));
	}
}
