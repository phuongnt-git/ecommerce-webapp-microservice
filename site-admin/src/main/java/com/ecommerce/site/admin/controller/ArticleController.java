package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.exception.ArticleNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Article;
import com.ecommerce.site.admin.security.UserDetailsImpl;
import com.ecommerce.site.admin.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class ArticleController {

    private final String defaultRedirectUrl = "redirect:/articles/page/1?sortField=title&sortDir=asc";

    @Autowired
    private ArticleService service;

    @GetMapping("/articles")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/articles/page/{pageNumber}")
    public String listByPage(@PagingAndSortingParam(moduleUrl = "/articles", listName = "listArticles") PagingAndSortingHelper helper,
                             @PathVariable("pageNumber") int pageNumber) {
        service.listByPage(pageNumber, helper);

        return "articles/articles";
    }

    @GetMapping("/articles/new")
    public String newArticle(@NotNull Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("pageTitle", "Create New Article");

        return "articles/article_form";
    }

    @PostMapping("/articles/save")
    public String saveArticle(Article article, @NotNull RedirectAttributes attributes,
                              @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        service.save(article, userDetails.getUser());
        attributes.addFlashAttribute("message", "The article has been saved successfully");

        return defaultRedirectUrl;
    }

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes attributes) {
        try {
            Article article = service.get(id);
            model.addAttribute("article", article);
            model.addAttribute("pageTitle", "Edit Article (ID: " + id + ")");

            return "articles/article_form";
        } catch (ArticleNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/articles/detail/{id}")
    public String viewArticle(@PathVariable("id") Integer id, RedirectAttributes attributes, @NotNull Model model) {
        try {
            Article article = service.get(id);
            model.addAttribute("article", article);

            return "articles/article_detail_modal";
        } catch (ArticleNotFoundException ex) {
            attributes.addFlashAttribute("message", String.format("Could not find any article with ID %s", id));

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/articles/{id}/enabled/{publishStatus}")
    public String publishArticle(@PathVariable("id") Integer id,
                                 @PathVariable("publishStatus") String publishStatus,
                                 @NotNull RedirectAttributes attributes) {
        try {
            boolean published = Boolean.parseBoolean(publishStatus);
            service.updatePublishStatus(id, published);
            attributes.addFlashAttribute("message", String.format("The article ID %s has been %s", id, published ? "published" : "unpublished"));
        } catch (ArticleNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return defaultRedirectUrl;
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable("id") Integer id,
                                @NotNull RedirectAttributes attributes,
                                HttpServletRequest request) {
        try {
            service.delete(id);
            attributes.addFlashAttribute("message", String.format("The article ID %s has been deleted", id));
        } catch (ArticleNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }
}
