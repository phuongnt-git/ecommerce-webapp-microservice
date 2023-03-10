package com.ecommerce.site.admin.service;

import com.ecommerce.site.admin.exception.ArticleNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Article;
import com.ecommerce.site.admin.model.entity.User;
import com.ecommerce.site.admin.model.enums.ArticleType;
import com.ecommerce.site.admin.repository.ArticleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.ARTICLES_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber, ARTICLES_PER_PAGE, repository);
    }

    public List<Article> listAll() {
        return repository.findPublishedArticlesWithIdAndTitleOnly();
    }

    public List<Article> listArticlesForMenu() {
        return repository.findByTypeOrderByTitle(ArticleType.MENU_BOUND);
    }

    public void save(Article article, User user) {
        setDefaultAlias(article);

        article.setUpdatedTime(new Date());
        article.setUser(user);

        repository.save(article);
    }

    private void setDefaultAlias(@NotNull Article article) {
        if (article.getAlias() == null || article.getAlias().isEmpty()) {
            article.setAlias(article.getTitle().replaceAll(" ", "-"));
        }
    }

    public Article get(Integer id) throws ArticleNotFoundException {
        Optional<Article> article = repository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new ArticleNotFoundException(String.format("Could not find any article with ID %s", id));
        }
    }

    public void updatePublishStatus(Integer id, boolean published) throws ArticleNotFoundException {
        if (!repository.existsById(id)) {
            throw new ArticleNotFoundException(String.format("Could not find any article with ID %s", id));
        }
        repository.updatePublishStatus(id, published);
    }

    public void delete(Integer id) throws ArticleNotFoundException {
        if (!repository.existsById(id)) {
            throw new ArticleNotFoundException(String.format("Could not find any article with ID %s", id));
        }
        repository.deleteById(id);
    }

}
