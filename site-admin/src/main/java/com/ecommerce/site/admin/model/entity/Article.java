package com.ecommerce.site.admin.model.entity;

import com.ecommerce.site.admin.model.enums.ArticleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Nguyen Thanh Phuong
 */
@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
public class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "articles_gen")
    @TableGenerator(name = "articles_gen",
            table = "sequencers",
            pkColumnName = "seq_name",
            valueColumnName = "seq_count",
            pkColumnValue = "articles_seq_next_val",
            allocationSize = 1)
    private Integer id;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false, length = 512)
    private String alias;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private ArticleType type;

    @Column(name = "updated_time", nullable = false)
    private Date updatedTime;

    @Column(nullable = false)
    private boolean published;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_articles_users"))
    private User user;

    public Article(Integer id) {
        this.id = id;
    }

    public Article(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Article(Integer id, String title, ArticleType type, Date updatedTime, boolean published, User user) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.updatedTime = updatedTime;
        this.published = published;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", type=" + type +
                '}';
    }

}
