package org.yiqixue.admin.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.intThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.yiqixue.admin.persistence.category.CategoryRepository;
import org.yiqixue.common.book.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repo;

	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(14);
		Category subCategory = new Category("Memory", parent);
		Category subCategory1 = new Category("Storage", parent);
		repo.saveAll(List.of(subCategory, subCategory1));

	}

	@Test
	public void testGetCategory() {
		Category category = repo.findById(12).get();

		System.out.println(category.getName());

		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(children.size()).isGreaterThan(0);

	}

	@Test
	public void testPrintHierarchicalCategories() {

		Iterable<Category> categories = repo.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSublevel = subLevel + 1;
		
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory: children) {
			
			for(int i = 0; i < newSublevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			
			printChildren(subCategory, newSublevel);
		}
	}

}
