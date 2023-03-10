package com.ecommerce.site.shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"}, name = "uq_products_name"),
                @UniqueConstraint(columnNames = {"alias"}, name = "uq_products_alias")},
        indexes = {
                @Index(columnList = "id", name = "product_idx", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "products_gen")
    @TableGenerator(name = "products_gen",
            table = "sequencers",
            pkColumnName = "seq_name",
            valueColumnName = "seq_count",
            pkColumnValue = "products_seq_next_val",
            allocationSize = 1)
    private Integer id;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String alias;

    @Column(name = "short_description", nullable = false, length = 512)
    private String shortDescription;

    @Column(name = "full_description", nullable = false, length = 4096)
    private String fullDescription;

    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    @Column
    private float cost;

    @Column
    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    @Column
    private float length;

    @Column
    private float width;

    @Column
    private float height;

    @Column
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Category.class)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_products_categories"))
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Brand.class)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "fk_products_brands"))
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductImage.class)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductDetail.class)
    private List<ProductDetail> details = new ArrayList<>();

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "average_rating")
    private float averageRating;

    @Transient
    private boolean customerCanReview;

    @Transient
    private boolean reviewedByCustomer;

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    @Transient
    public String getMainImagePath() {
        if (id == null || mainImage == null) {
            return "/images/image-thumbnail.png";
        }
        return "/images/product-images/" + this.id + "/" + this.mainImage;
    }

    public boolean containsImageName(String imageName) {
        for (ProductImage image : images) {
            if (image.getName().equals(imageName)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    @Transient
    public float getDiscountPrice() {
        if (discountPercent > 0) {
            return price * ((100 - discountPercent) / 100);
        }
        return this.price;
    }

    @Transient
    public String getUri() {
        return "/p/" + this.alias + "/";
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }

    public void addDetail(Integer id, String name, String value) {
        this.details.add(new ProductDetail(id, name, value, this));
    }

    public Product(String name) {
        this.name = name;
    }

    public Product(Integer id) {
        this.id = id;
    }

}
