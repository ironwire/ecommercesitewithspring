package org.yiqixue.admin.controller.store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.yiqixue.common.store.Store;

@Component
public class StoreModelAssembler implements RepresentationModelAssembler<Store, EntityModel<Store>> {
	@Override
	public EntityModel<Store> toModel(Store entity) {
        EntityModel<Store> accountModel = EntityModel.of(entity);
        
        accountModel.add(linkTo(methodOn(StoreRestController.class).getOne(entity.getId())).withSelfRel());
        accountModel.add(linkTo(methodOn(StoreRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
         
        return accountModel;	}


}
