package com.ecommerce.site.admin.service;

import com.ecommerce.site.admin.exception.ShippingRateAlreadyExistsException;
import com.ecommerce.site.admin.exception.ShippingRateNotFoundException;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.model.entity.Country;
import com.ecommerce.site.admin.model.entity.Product;
import com.ecommerce.site.admin.model.entity.ShippingRate;
import com.ecommerce.site.admin.repository.CountryRepository;
import com.ecommerce.site.admin.repository.ProductRepository;
import com.ecommerce.site.admin.repository.ShippingRateRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.SHIPPING_RATES_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ShippingRateService {

    @Autowired
    private ShippingRateRepository rateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final int DIM_DIVISOR = 139;

    public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber, SHIPPING_RATES_PER_PAGE, rateRepository);
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(@NotNull ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDb = rateRepository.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDb != null;
        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDb != null & !rateInDb.equals(rateInForm);

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException(String.format(
                    "There is already a rate for the destination %s, %s", rateInForm.getCountry().getName(), rateInForm.getState()));
        }

        rateRepository.save(rateInForm);
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        Optional<ShippingRate> rate = rateRepository.findById(id);
        if (rate.isPresent()) {
            return rate.get();
        } else {
            throw new ShippingRateNotFoundException(String.format("Could not find shipping rate with ID %s", id));
        }
    }

    public void updateCodSupported(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
        Long count = rateRepository.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException(String.format("Could not find shipping rate with ID %s", id));
        }
        rateRepository.updateCodSupported(id, codSupported);
    }

    public void delete(Integer id) throws ShippingRateNotFoundException {
        Long count = rateRepository.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException(String.format("Could not find shipping rate with ID %s", id));
        }
        rateRepository.deleteById(id);
    }

    public float calculateShippingCost(Integer productId, Integer countryId, String state)
            throws ShippingRateNotFoundException {
        ShippingRate rate = rateRepository.findByCountryAndState(countryId, state);
        if (rate == null) {
            throw new ShippingRateNotFoundException("No shipping rate found for the given destination. You have to enter shipping cost manually");
        }

        Product product = productRepository.findById(productId).get();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight * rate.getRate();
    }

}
