package org.yiqixue.admin.controller.store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yiqixue.admin.service.store.StoreService;
import org.yiqixue.admin.utils.EmailUtility;
import org.yiqixue.common.store.Store;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.yiqixue.common.EmailSetting;
import org.yiqixue.common.book.Book;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins="http://localhost:4200")
public class StoreRestController {
	
	@Autowired
	private StoreService storeService;
	@Autowired
	private StoreModelAssembler assembler;

	
	public StoreRestController(StoreService service, StoreModelAssembler assembler) {
		this.storeService = service;
		this.assembler = assembler;
	}


	@GetMapping
	public CollectionModel<EntityModel<Store>> listAll() {
		List<EntityModel<Store>> listEntityModel = storeService.listAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Store>> collectionModel = CollectionModel.of(listEntityModel);

		collectionModel.add(linkTo(methodOn(StoreRestController.class).listAll()).withSelfRel());

		return collectionModel;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Store>> getOne(@PathVariable("id") Integer id) {
		try {
			Store store = storeService.get(id);
			EntityModel<Store> model = assembler.toModel(store);

			return new ResponseEntity<>(model, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/checkEmailUniqueness")
	public String checkDuplicateEmail(@RequestBody String email) {
		return storeService.isEmailUnique(email) ? "true" : "false";
	}	
	
	@PostMapping("/register")
	public ResponseEntity<StoreResponseEntity> registerSeller(@RequestBody Store seller) {
		Store store=storeService.registerSeller(seller);
		StoreResponseEntity response = new StoreResponseEntity();
		response.setStatus("registered");
		response.setUrl("http://locahost:4200/verify?code="+store.getVerificationCode());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/verify")
	public ResponseEntity<StoreResponseEntity> verifyAccount(@RequestParam(name = "code") String code) {
		boolean verified = storeService.verify(code);
		StoreResponseEntity response = new StoreResponseEntity();
		if(verified == true) {
			response.setStatus("verified");
			response.setUrl("http://locahost:4200/login");
			return ResponseEntity.ok().body(response);
		}else {
			response.setStatus("store not found");
			response.setUrl("http://localhost:4200/seller/seller_register");
			return  ResponseEntity.status(404).body(response);
		}
		 
	}


}
