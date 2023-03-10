package com.ecommerce.site.admin.repository;

import com.ecommerce.site.admin.model.entity.Article;
import com.ecommerce.site.admin.model.enums.ArticleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface ArticleRepository extends CustomPagingAndSortingRepository<Article, Integer> {

    @Query("SELECT NEW Article(a.id, a.title, a.type, a.updatedTime, a.published, a.user) FROM Article a")
    Page<Article> findAll(Pageable pageable);

    @Query("SELECT NEW Article(a.id, a.title, a.type, a.updatedTime, a.published, a.user) FROM Article a WHERE a.title LIKE %?1% OR a.content LIKE %?1%")
    Page<Article> findAll(String keyword, Pageable pageable);

    @Query("UPDATE Article a SET a.published = ?2 WHERE a.id = ?1")
    @Modifying
    void updatePublishStatus(Integer id, boolean published);

    List<Article> findByTypeOrderByTitle(ArticleType type);

    @Query("SELECT NEW Article(a.id, a.title) From Article a WHERE a.published = true ORDER BY a.title")
    List<Article> findPublishedArticlesWithIdAndTitleOnly();

}