package com.shopme.admin.product.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.product.service.ProductService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService pservice;
	@Autowired
	private BrandService bservice;
	
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null); 
		
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		List<Brand> listBrands = bservice.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfImages", 0);
		
		return "products/product_form";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Product> page = pservice.listByPage(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("products", listProducts);
		
		return "products/products";
	}
	
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes re,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames
			
			) throws IOException {
		
		setMainImageName(mainImageMultipart, product);
		
		setNewExtraImageNames(extraImageMultiparts, product);
		
		setExistingExtraImageNames(imageIDs, imageNames, product);
		
		setProductDetails(detailIDs, detailNames, detailValues, product);


		
		Product savedProduct = pservice.save(product);
		
		saveUploadedImages(mainImageMultipart,extraImageMultiparts, savedProduct);
		
		deleteExtraImagesWereRemoveOnForm(product);

	
		re.addFlashAttribute("message","Product has been saved successfully.");
		return "redirect:/products";
	}
	
	private void deleteExtraImagesWereRemoveOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();
				
				if(!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted existing extra image file: " +filename);
					}catch(IOException e) {
						LOGGER.error("Could not delete extra image: "+ filename);
					}
				}
			});
		}catch(IOException ex){
			LOGGER.error("Could not list directory: "+ dirPath);
		}
		
		
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if(imageIDs == null || imageIDs.length == 0) return;
		
		Set<ProductImage> images = new HashSet<>();
		
		for(int count = 0; count<imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			
			images.add(new ProductImage(id, name, product));
		}
		
		product.setImages(images);
		
	}

	private void setProductDetails(String[] detailIDs, String[] detailNames, 
			String[] detailValues, Product product) {
		if(detailNames == null || detailNames.length == 0) return;
		
		for(int count =0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			System.out.println("++++++++++++++++++"+detailIDs[count]);
			Integer id = Integer.parseInt(detailIDs[count]);
			
			if(id != 0) {
				product.addDetail(id, name, value);
			}else if(!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
	}


	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		if(extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId()+"/extras";
			
			for(MultipartFile multipart : extraImageMultiparts) {
				if(multipart.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipart);
			}
		}
		
	}

	private void setMainImageName(MultipartFile mainImageMultipart,Product product) {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
			
		}
		
	}
	
	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if(extraImageMultiparts.length > 0) {
			for(MultipartFile multipart : extraImageMultiparts) {
				if(!multipart.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
					if(!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String udpateProductEnabledStatu(
			@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes red) {
		pservice.updateProductEnabledStatus(id, enabled);
		String status = enabled? "enabled":"disabled";
		String message ="The product with ID "+id+" has been " + status;
		red.addFlashAttribute("message", message);
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			pservice.deleteProductById(id);
			String productExtraImagesDir = "../product-images/" + id +"/extras";
			String productMainImageDir = "../product-images/" + id ;
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productMainImageDir);
			
			redirectAttributes.addFlashAttribute("message", "The product ID "+id+" hase been deleted successfully.");
		}catch(ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = pservice.get(id);
			
			List<Brand> listBrands = bservice.listAll();
			
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID:"+ id +")");
			
			Integer numberOfExistingImages = product.getImages().size();
			model.addAttribute("numberOfImages", numberOfExistingImages);
			return "products/product_form";
		}catch(ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
			
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = pservice.get(id);
			
			
			model.addAttribute("product", product);
		
			return "products/product_detail_modal";
		}catch(ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
			
		}
	}
}









