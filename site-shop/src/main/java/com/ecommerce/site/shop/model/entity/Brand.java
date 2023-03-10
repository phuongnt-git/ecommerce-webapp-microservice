package com.ecommerce.site.shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "brands",
        uniqueConstraints = {@UniqueConstraint(name = "uq_brands_name", columnNames = "name")})
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"logo"})
public class Brand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "brands_gen")
    @TableGenerator(name = "brands_gen",
            table = "sequencers",
            pkColumnName = "seq_name",
            valueColumnName = "seq_count",
            pkColumnValue = "brands_seq_next_val",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 128)
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false, updatable = false),
            foreignKey = @ForeignKey(name = "fk_brands_categories_brands"),
            inverseForeignKey = @ForeignKey(name = "fk_brands_categories_categories"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"brand_id", "category_id"})
    )
    private Set<Category> categories = new HashSet<>();

    public Brand(Integer id) {
        this.id = id;
    }

    public Brand(String name) {
        this.name = name;
        this.logo = "image-thumbnail.png";
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public String getLogoPath() {
        if (this.id == null) {
            return "/images/image-thumbnail.png";
        }
        return "/images/brand-logos/" + this.id + "/" + this.logo;
    }

}
