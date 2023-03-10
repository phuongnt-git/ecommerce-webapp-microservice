package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.exception.ShippingRateNotFoundException;
import com.ecommerce.site.admin.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class ShippingRateRestController {

    @Autowired
    private ShippingRateService service;

    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
        float shippingCost = service.calculateShippingCost(productId, countryId, state);
        return String.valueOf(shippingCost);
    }

}
