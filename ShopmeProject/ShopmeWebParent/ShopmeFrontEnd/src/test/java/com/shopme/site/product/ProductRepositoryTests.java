package com.shopme.site.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.product.Product;
import com.shopme.site.product.persistence.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTests {

	@Autowired private ProductRepository prepo;
	
	@Test
	public void testFindByAlias() {
		
		String alias = "canon-eos-m50";
		Product product = prepo.findByAlias(alias);
		
		assertThat(product).isNotNull();
	}
}
