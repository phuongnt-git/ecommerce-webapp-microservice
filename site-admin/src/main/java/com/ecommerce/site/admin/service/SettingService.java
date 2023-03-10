package com.ecommerce.site.admin.service;

import com.ecommerce.site.admin.repository.SettingRepository;
import com.ecommerce.site.admin.model.entity.Setting;
import com.ecommerce.site.admin.model.enums.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class SettingService {

    @Autowired
    private SettingRepository repository;

    public List<Setting> listAllSettings() {
        return repository.findAll();
    }

    public SettingBagService getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new SettingBagService(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        repository.saveAll(settings);
    }

    public List<Setting> getMailServerSettings() {
        return repository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings() {
        return repository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySettings() {
        return repository.findByCategory(SettingCategory.CURRENCY);
    }

    public List<Setting> getPaymentSettings() {
        return repository.findByCategory(SettingCategory.PAYMENT);
    }

}
