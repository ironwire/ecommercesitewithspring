package org.yiqixue.hateoas.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.yiqixue.hateoas.controller.AccountApi;
import org.yiqixue.hateoas.entity.Account;

@Component
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>>{

	@Override
	public EntityModel<Account> toModel(Account entity) {
        EntityModel<Account> accountModel = EntityModel.of(entity);
        
        accountModel.add(linkTo(methodOn(AccountApi.class).getOne(entity.getId())).withSelfRel());
        accountModel.add(linkTo(methodOn(AccountApi.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
        accountModel.add(linkTo(methodOn(AccountApi.class).deposit(entity.getId(), null)).withRel("deposits"));
        accountModel.add(linkTo(methodOn(AccountApi.class).withdraw(entity.getId(), null)).withRel("withdrawal"));
         
        return accountModel;
	}
	
	
}
