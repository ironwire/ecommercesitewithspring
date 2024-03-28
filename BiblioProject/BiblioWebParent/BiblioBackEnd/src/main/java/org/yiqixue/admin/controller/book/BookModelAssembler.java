package org.yiqixue.admin.controller.book;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.yiqixue.common.book.Book;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {

	@Override
	public EntityModel<Book> toModel(Book entity) {
		EntityModel<Book> accountModel = EntityModel.of(entity);

		accountModel.add(linkTo(methodOn(BookRestController.class).getOne(entity.getId())).withSelfRel());
		accountModel.add(linkTo(methodOn(BookRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));

		return accountModel;
	}
}
