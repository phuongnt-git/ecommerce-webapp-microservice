package com.ecommerce.site.shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "review_votes")
@Getter
@Setter
public class ReviewVote implements Serializable {

	private static final int VOTE_UP_POINT = 1;

	private static final int VOTE_DOWN_POINT = -1;

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "review_votes_gen")
	@TableGenerator(name = "review_votes_gen",
			table = "sequencers",
			pkColumnName = "seq_name",
			valueColumnName = "seq_count",
			pkColumnValue = "review_votes_seq_next_val",
			allocationSize = 1)
	private Integer id;

	@Column(nullable = false)
	private int votes;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_votes_customers"))
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "review_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_votes_reviews"))
	private Review review;
	
	public void voteUp() {
		this.votes = VOTE_UP_POINT;
	}
	
	public void voteDown() {
		this.votes = VOTE_DOWN_POINT;
	}
	
	@Transient
	public boolean isUpVoted() {
		return this.votes == VOTE_UP_POINT;
	}
	
	@Transient
	public boolean isDownVoted() {
		return this.votes == VOTE_DOWN_POINT;
	}

	@Override
	public String toString() {
		return "ReviewVote{" +
				"votes=" + votes +
				", customer=" + customer.getFullName() +
				", review=" + review.getId() +
				'}';
	}

}
