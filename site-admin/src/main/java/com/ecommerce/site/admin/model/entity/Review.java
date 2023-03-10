package com.ecommerce.site.admin.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "reviews_gen")
	@TableGenerator(name = "reviews_gen",
			table = "sequencers",
			pkColumnName = "seq_name",
			valueColumnName = "seq_count",
			pkColumnValue = "reviews_seq_next_val",
			allocationSize = 1)
	private Integer id;
	
	@Column(length = 128, nullable = false)
	private String headline;
	
	@Column(name = "comments", length = 512, nullable = false)
	private String comment;

	@Column(nullable = false)
	private int rating;	

	@Column(nullable = false)
	private int votes;
	
	@Column(name = "review_time", nullable = false)
	private Date reviewTime;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reviews_products"))
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reviews_customers"))
	private Customer customer;
	
	@Transient
	private boolean upVotedByCurrentCustomer;
	
	@Transient
	private boolean downVotedByCurrentCustomer;

	public Review(Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Review review = (Review) o;

		return Objects.equals(id, review.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Review{" +
				"headline='" + headline + '\'' +
				", rating=" + rating +
				", reviewTime=" + reviewTime +
				", product=" + product.getShortName() +
				", customer=" + customer.getFullName() +
				'}';
	}

}
