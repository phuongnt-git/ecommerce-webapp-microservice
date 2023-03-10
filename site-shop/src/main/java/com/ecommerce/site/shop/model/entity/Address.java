package com.ecommerce.site.shop.model.entity;

import com.ecommerce.site.shop.model.AbstractAddressWithCountry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address extends AbstractAddressWithCountry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "addresses_gen")
    @TableGenerator(name = "addresses_gen",
            table = "sequencers",
            pkColumnName = "seq_name",
            valueColumnName = "seq_count",
            pkColumnValue = "addresses_seq_next_val",
            allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_addresses_customers"))
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultForShipping;

}
