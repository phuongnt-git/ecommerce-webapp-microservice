package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.exception.BrandNotFoundRestException;
import com.ecommerce.site.admin.service.BrandService;
import com.ecommerce.site.admin.model.dto.CategoryDto;
import com.ecommerce.site.admin.model.entity.Brand;
import com.ecommerce.site.admin.model.entity.Category;
import com.ecommerce.site.admin.exception.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class BrandRestController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUnique(Integer id, String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDto> listCategoriesByBrand(@PathVariable("id") Integer id) throws BrandNotFoundRestException {
        List<CategoryDto> listCategories = new ArrayList<>();

        try {
            Brand brand = brandService.get(id);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories) {
                CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
                listCategories.add(categoryDto);

            }

            return listCategories;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }

}
