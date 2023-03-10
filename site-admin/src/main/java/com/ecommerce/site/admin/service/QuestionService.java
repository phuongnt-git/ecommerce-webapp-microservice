package com.ecommerce.site.admin.service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;


import com.ecommerce.site.admin.exception.QuestionNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Question;
import com.ecommerce.site.admin.model.entity.User;
import com.ecommerce.site.admin.repository.QuestionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.QUESTIONS_PER_PAGE;

/**
 * @author nguyenthanhphuong
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionService {
	
	@Autowired
	private QuestionRepository repository;
	
	public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper) {
		helper.listEntities(pageNumber, QUESTIONS_PER_PAGE, repository);
	}	
	
	public Question get(Integer id) throws QuestionNotFoundException {
		Optional<Question> question = repository.findById(id);
		if (question.isPresent()) {
		    return question.get();
		} else {
			throw new QuestionNotFoundException(String.format("Could not find question with ID %s", id));
		}
	}
	
	public void save(@NotNull Question questionInForm, User user) throws QuestionNotFoundException {
		try {
			Question questionInDb = repository.findById(questionInForm.getId()).orElseThrow();
			questionInDb.setQuestionContent(questionInForm.getQuestionContent());
			questionInDb.setAnswer(questionInForm.getAnswer());
			questionInDb.setApproved(questionInForm.isApproved());

			if (questionInDb.isAnswered()) {
				questionInDb.setAnswerTime(new Date());
				questionInDb.setAnswerer(user);
			}

			repository.save(questionInDb);
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException(String.format("Could not find any question with ID %s", questionInForm.getId()));
		}		
	}
	
	public void approve(Integer id) {
		repository.updateApprovalStatus(id, true);
	}
	
	public void disapprove(Integer id) {
		repository.updateApprovalStatus(id, false);
	}
	
	public void delete(Integer id) throws QuestionNotFoundException {
		if (!repository.existsById(id)) {
			throw new QuestionNotFoundException(String.format("Could not find question with ID %s", id));
		}
		repository.deleteById(id);
	}	
}
