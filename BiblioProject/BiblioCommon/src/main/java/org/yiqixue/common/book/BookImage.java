package org.yiqixue.common.book;

import org.yiqixue.common.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="book_images")
public class BookImage extends IdBasedEntity {
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	
	public BookImage() {
	}

	public BookImage(Integer id, String name, Book book) {
		this.id = id;
		this.name = name;
		this.book = book;
	}


	public BookImage(String name, Book book) {
		this.name = name;
		this.book = book;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	@Transient
	public String getImagePath() {
		return  "/book-images/" + book.getId() + "/extras/" + this.name; //Constants.S3_BASE_URI +
	}
}
