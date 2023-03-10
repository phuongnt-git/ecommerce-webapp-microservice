package com.ecommerce.site.shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cart_items_gen")
	@TableGenerator(name = "cart_items_gen",
			table = "sequencers",
			pkColumnName = "seq_name",
			valueColumnName = "seq_count",
			pkColumnValue = "cart_items_seq_next_val",
			allocationSize = 1)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_cart_items_customers"))
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_cart_items_products"))
	private Product product;
	
	private int quantity;
	
	@Transient
	private float shippingCost;

	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice() * quantity;
	}

	@Override
	public String toString() {
		return "CartItem{" +
				"id=" + id +
				", customer=" + customer.getFullName() +
				", product=" + product.getShortName() +
				", quantity=" + quantity +
				'}';
	}

}
