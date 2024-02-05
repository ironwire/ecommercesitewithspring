package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.product.persistence.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		
		
		Brand brand = entityManager.find(Brand.class, 3);
		Category category = entityManager.find(Category.class, 15);
		
		Product product = new Product();
		product.setName("Samsung Kabab A31");
		product.setAlias("Kabab");
		product.setShortDescription("blahblahblahblahblahblahblahblahblahblah");
		product.setFullDescription("blahblahblahblahblahblahblahblahblahblahblahblahblahblah");
		
		product.setBrand(brand);
		product.setCategory(category);
		product.setEnabled(true);
		product.setInStock(false);
		
		product.setPrice(345);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
		
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testSavedProducWithImages() {
		
		Integer productId = 4;
		Product product = repo.findById(productId).get();
		
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra_image_1.png");
		
		product.addExtraImage("extra_image_2.png");
		product.addExtraImage("extra_image_3.png");
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
		
		
		
	}
}













