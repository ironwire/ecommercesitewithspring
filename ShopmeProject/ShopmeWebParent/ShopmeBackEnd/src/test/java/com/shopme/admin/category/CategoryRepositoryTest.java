package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)


public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository crepo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electrinics");
		Category savedCate = crepo.save(category);
		
		assertThat(savedCate.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(10);
		Category sub1 = new Category("Memory", parent);
		Category sub2 = new Category("CPU", parent);
		
		crepo.saveAll(List.of(sub1, sub2));
		//assertThat(savedCate.getId()).isGreaterThan(0);
		
		
		//assertThat(savedCate.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateOneSub() {
		Category parent = new Category(1);
		Category sub1 = new Category("Components", parent);
//		Category sub2 = new Category("Crystal Sillicon", parent);
		
		Category savedSub1=crepo.save(sub1);
		assertThat(savedSub1.getId()).isGreaterThan(0);
		
		
		//assertThat(savedCate.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCountByParentId() {
		
		Long count = crepo.countByParentId(10);
		System.out.println(count);
		assertThat(count).isGreaterThan(0);
		
	}
	

	
	
}



















