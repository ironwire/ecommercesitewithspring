package org.yiqixue.hateoas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yiqixue.hateoas.entity.Account;
import org.yiqixue.hateoas.entity.Amount;
import org.yiqixue.hateoas.service.AccountService;
import org.yiqixue.hateoas.utils.AccountModelAssembler;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {
    
	private AccountService service;
    private AccountModelAssembler modelAssembler;
    
    public AccountApi(AccountService service, AccountModelAssembler modelAssembler) {
        this.service = service;
        this.modelAssembler = modelAssembler;
    }  
     
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Account>> getOne(@PathVariable("id") Integer id) {
        try {
        	Account account = service.get(id);
        	
        	EntityModel<Account> model = modelAssembler.toModel(account);
        	
        	return new ResponseEntity<>(model, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();

		}
    }
    
    @GetMapping
    public CollectionModel<EntityModel<Account>> listAll() {
        
    	List<EntityModel<Account>> listEntityModel = service.listAll()
        		.stream().map(modelAssembler::toModel).collect(Collectors.toList());
//        listAccounts = service.listAll();
         
 
        CollectionModel<EntityModel<Account>> collectionModel = CollectionModel.of(listEntityModel);
         
        collectionModel.add(linkTo(methodOn(AccountApi.class).listAll()).withSelfRel());
         
        return collectionModel;
    }
    
    @PostMapping
    public HttpEntity<EntityModel<Account>> add(@RequestBody Account account){
    	Account savedAccount = service.save(account);
    	
    	EntityModel<Account> model = modelAssembler.toModel(savedAccount);
    	
    	return ResponseEntity.created(linkTo(methodOn(AccountApi.class).getOne(savedAccount.getId())).toUri()).body(model);
 
    	}    	
    	
	@PutMapping
	public HttpEntity<EntityModel<Account>> replace(@RequestBody Account account) {
	    Account updatedAccount = service.save(account);
	     
	    return new ResponseEntity<>(modelAssembler.toModel(updatedAccount), HttpStatus.OK);
 
	}
	
	@PatchMapping("/{id}/deposits")
	public HttpEntity<EntityModel<Account>> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
	     
	    Account updatedAccount = service.deposit(amount.getAmount(), id);
	     
	     
	    return new ResponseEntity<>(modelAssembler.toModel(updatedAccount), HttpStatus.OK);      
	}
	
	@PatchMapping("/{id}/withdrawal")
	public HttpEntity<EntityModel<Account>> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
	    Account updatedAccount = service.withdraw(amount.getAmount(), id);
	     
	    return new ResponseEntity<>(modelAssembler.toModel(updatedAccount), HttpStatus.OK);      
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
	    service.delete(id);
	    return ResponseEntity.noContent().build();
	}
	
}