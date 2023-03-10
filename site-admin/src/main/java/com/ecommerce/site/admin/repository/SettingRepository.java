package com.ecommerce.site.admin.repository;

import com.ecommerce.site.admin.model.entity.Setting;
import com.ecommerce.site.admin.model.enums.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findByCategory(SettingCategory category);

}