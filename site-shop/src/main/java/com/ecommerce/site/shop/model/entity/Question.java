package com.ecommerce.site.shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "questions_gen")
	@TableGenerator(name = "questions_gen",
			table = "sequencers",
			pkColumnName = "seq_name",
			valueColumnName = "seq_count",
			pkColumnValue = "questions_seq_next_val",
			allocationSize = 1)
	private Integer id;

	@Column(name = "question", nullable = false)
	private String questionContent;

	@Column
	private String answer;

	@Column(nullable = false, columnDefinition = "number(10, 0) default 0")
	private int votes;

	@Column(nullable = false, columnDefinition = "number(1, 0) default 0")
	private boolean approved;
	
	@Column(name = "ask_time", nullable = false)
	private Date askTime;
	
	@Column(name = "answer_time")
	private Date answerTime;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_questions_products"))
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "answerer_id", foreignKey = @ForeignKey(name = "fk_questions_users"))
	private User answerer;
	
	@ManyToOne
	@JoinColumn(name = "asker_id", nullable = false, foreignKey = @ForeignKey(name = "fk_questions_customers"))
	private Customer asker;
	
	@Transient
	public boolean isAnswered() {
		return this.answer != null && !answer.isEmpty();
	}

	@Transient
	public String getProductName() {
		return this.product.getName();
	}
	
	@Transient
	public String getAskerFullName() {
		return asker.getFirstName() + " " + asker.getLastName();
	}
	
	@Transient
	public String getAnswererFullName() {
		if (answerer != null) {
			return answerer.getFirstName() + " " + answerer.getLastName();
		}
		return "";
	}

	@Transient
	private boolean upVotedByCurrentCustomer;
	
	@Transient	
	private boolean downVotedByCurrentCustomer;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Question question = (Question) o;

		return Objects.equals(id, question.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
