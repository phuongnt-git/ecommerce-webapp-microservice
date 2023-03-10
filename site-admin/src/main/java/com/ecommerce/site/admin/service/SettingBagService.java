package com.ecommerce.site.admin.service;

import com.ecommerce.site.admin.model.entity.Setting;
import com.ecommerce.site.admin.model.SettingBag;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
public class SettingBagService extends SettingBag {

    public SettingBagService(List<Setting> listSettings) {
        super(listSettings);
    }

    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String value) {
        super.update("SITE_LOGO", value);
    }

}
