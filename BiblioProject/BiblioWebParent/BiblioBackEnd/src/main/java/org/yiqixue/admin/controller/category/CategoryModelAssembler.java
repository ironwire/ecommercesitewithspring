package org.yiqixue.admin.controller.category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.yiqixue.common.book.Category;

@Component
public class CategoryModelAssembler 
		implements RepresentationModelAssembler<Category, EntityModel<Category>> {

	@Override
	public EntityModel<Category> toModel(Category entity) {
        EntityModel<Category> accountModel = EntityModel.of(entity);
        
        accountModel.add(linkTo(methodOn(CategoryRestController.class).getOne(entity.getId())).withSelfRel());
        accountModel.add(linkTo(methodOn(CategoryRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
         
        return accountModel;	}

	
}
