package com.ecommerce.site.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecommerce.site.admin.model.entity.Brand;
import com.ecommerce.site.admin.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {
	
	@MockBean
	private BrandRepository repository;
	
	@InjectMocks
	private BrandService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Acer";
		Brand brand = new Brand(name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "AMD";
		
		Mockito.when(repository.findByName(name)).thenReturn(null);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name = "Canon";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(2, "Canon");
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Acer";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, "Acer Ltd");
		assertThat(result).isEqualTo("OK");
	}	
}
