package org.yiqixue.admin.controller.category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yiqixue.admin.service.category.CategoryService;
import org.yiqixue.common.book.Category;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins="http://localhost:4200")
public class CategoryRestController {

	@Autowired
	private CategoryService service;
	private CategoryModelAssembler assembler;
	
	@Autowired
	private PagedResourcesAssembler<Category> pagedResourcesAssembler;

	public CategoryRestController(CategoryService service, CategoryModelAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<Category>> listAll() {
		List<EntityModel<Category>> listEntityModel = service.listAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Category>> collectionModel = CollectionModel.of(listEntityModel);

		collectionModel.add(linkTo(methodOn(CategoryRestController.class).listAll()).withSelfRel());

		return collectionModel;
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Category>> getOne(@PathVariable("id") Integer id) {
		try {
			Category category = service.get(id);
			EntityModel<Category> model = assembler.toModel(category);

			return new ResponseEntity<>(model, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping//(consumes={ MediaType.MULTIPART_FORM_DATA_VALUE })
	@CrossOrigin
	public HttpEntity<EntityModel<Category>> add(@RequestBody @Valid Category category) {
		Category savedCategory = service.save(category);
		EntityModel<Category> model = assembler.toModel(savedCategory);

		return ResponseEntity
				.created(linkTo(methodOn(CategoryRestController.class).getOne(savedCategory.getId())).toUri())
				.body(model);
	}
	//@PostMapping(consumes={ MediaType.MULTIPART_FORM_DATA_VALUE })
//	@PostMapping(consumes= {MediaType.MULTIPART_FORM_DATA_VALUE})
//	public ResponseEntity<EntityModel<Category>> add(@RequestPart("category") @Valid Category category,
//			@RequestParam(name="file",required=false) MultipartFile multipartFile) throws IOException {
//		Category savedCategory = new Category();
//		
//		if(multipartFile !=null && !multipartFile.isEmpty()) {
//			
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			
//			System.out.println("Image fileName is ==========: "+ fileName);
//			
//			
//			savedCategory = service.save(category);
//			String uploadDir = "../category-images/" + savedCategory.getId();
//
//			FileUpLoadUtil.cleanDir(uploadDir);
//			System.out.println("uploadDir is ==========: "+ uploadDir);
//			FileUpLoadUtil.saveFile(uploadDir, fileName, multipartFile);
//		}else {
//			savedCategory = service.save(category);
//		}
//		
//		EntityModel<Category> model = assembler.toModel(savedCategory);
//
//		return ResponseEntity
//				.created(linkTo(methodOn(CategoryRestController.class).getOne(savedCategory.getId())).toUri())
//				.body(model);
//	}
	@CrossOrigin
	@GetMapping("/page")
	public CollectionModel<EntityModel<Category>> listByPage(@RequestParam(name = "page") int pageNum,
			@RequestParam(name = "size") int pageSize,
			@RequestParam(name="keyword", required = false) String keyword
			) {

		//Page<Category> listCategories = service.listByPage(pageNum+1, pageSize);
		
		Page<Category> listCategories = service.listByPage(pageNum, pageSize, null, null, keyword);
		
		PagedModel<EntityModel<Category>> pagedModel = pagedResourcesAssembler
				.toModel(listCategories, assembler);
		

		pagedModel.add(linkTo(methodOn(CategoryRestController.class).listAll()).withSelfRel());

		return pagedModel;
		
		
	}
	
	@CrossOrigin
	@GetMapping("/pages")
	public CollectionModel<EntityModel<Category>> listByPage(@RequestParam(name = "page") int pageNum,
			@RequestParam(name = "size") int pageSize,
			@RequestParam(name="sortField", required = false) String sortField,
			@RequestParam(name="sortDir", required = false) String sortDir, 
			@RequestParam(name="keyword", required = false) String keyword,
			Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		
		Page<Category> listCategories = service.listByPage(pageNum, pageSize, sortField, sortDir, keyword);
		
		PagedModel<EntityModel<Category>> pagedModel = pagedResourcesAssembler
				.toModel(listCategories, assembler);
		

		pagedModel.add(linkTo(methodOn(CategoryRestController.class).listAll()).withSelfRel());

		return pagedModel;
		
//		String reverseSortDir = sortDir.equals("asc")? "desc" : "asc";
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
	    service.delete(id);
	    return ResponseEntity.noContent().build();
	}
	
}