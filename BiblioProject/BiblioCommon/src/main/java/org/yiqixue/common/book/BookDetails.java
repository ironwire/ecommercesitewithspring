package org.yiqixue.common.book;

import org.yiqixue.common.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="book_details")
public class BookDetails extends IdBasedEntity {
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(nullable = false, length = 255)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	public BookDetails() {
		// TODO Auto-generated constructor stub
	}
	
	public BookDetails(Integer id, String name, String value, Book book) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.book = book;
	}

	public BookDetails(String name, String value, Book book) {
		this.name = name;
		this.value = value;
		this.book = book;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Book getProduct() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
