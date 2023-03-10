package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.exception.ShippingRateAlreadyExistsException;
import com.ecommerce.site.admin.exception.ShippingRateNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Country;
import com.ecommerce.site.admin.model.entity.ShippingRate;
import com.ecommerce.site.admin.service.ShippingRateService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
@RequestMapping("/shipping_rates")
public class ShippingRateController {

    private final String defaultRedirectUrl = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";

    @Autowired
    private ShippingRateService service;

    @GetMapping("")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(@PagingAndSortingParam(listName = "shippingRates", moduleUrl = "/shipping_rates")PagingAndSortingHelper helper,
                      @PathVariable("pageNumber") int pageNumber) {
        service.listByPage(pageNumber, helper);

        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/new")
    public String newShippingRate(@NotNull Model model) {
        List<Country> listCountries = service.listAllCountries();

        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "New Shipping Rate");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/save")
    public String saveShippingRate(ShippingRate rate, @NotNull RedirectAttributes attributes) {
        try {
            service.save(rate);
            attributes.addFlashAttribute("message", "The shipping rate has been saved successfully");
        } catch (ShippingRateAlreadyExistsException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return defaultRedirectUrl;
    }

    @GetMapping("/edit/{id}")
    public String editShippingRate(@PathVariable("id") Integer id, @NotNull Model model, RedirectAttributes attributes) {
        try {
            ShippingRate rate = service.get(id);
            List<Country> listCountries = service.listAllCountries();

            model.addAttribute("listCountries", listCountries);
            model.addAttribute("rate", rate);
            model.addAttribute("pageTitle", String.format("Edit Shipping Rate (ID: %s)", id));

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/cod/{id}/enabled/{supported}")
    public String updateCodSupported(@PathVariable("id") Integer id,
                                     @PathVariable("supported") Boolean supported,
                                     @NotNull RedirectAttributes attributes,
                                     HttpServletRequest request) {
        try {
            service.updateCodSupported(id, supported);
            attributes.addFlashAttribute("message", String.format("COD support for shipping rate ID %s has been updated", id));
        } catch (ShippingRateNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("/delete/{id}")
    public String deleteShippingRate(@PathVariable("id") Integer id,
                                     @NotNull RedirectAttributes attributes,
                                     HttpServletRequest request) {
        try {
            service.delete(id);
            attributes.addFlashAttribute("message", String.format("The shipping rate with ID %s has been deleted", id));
        } catch (ShippingRateNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }
}
