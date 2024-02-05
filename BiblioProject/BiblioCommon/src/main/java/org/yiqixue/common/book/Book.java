package org.yiqixue.common.book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yiqixue.common.IdBasedEntity;
import org.yiqixue.common.store.Store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="books")
public class Book extends IdBasedEntity {
	
	public enum BindingType {
		SOFTCOVER(0), HADERCOVER(1), STABPLED(2), SPIRAL(3);
		
		int typeCode;
		
		private BindingType(int typeCode) {
			this.typeCode = typeCode;
		}
		
		public int getTypeCode() {
			return typeCode;
		}
	}
	
	public enum BookCondition {
		ASNEW(0),FINE(1),VERYGOOD(2),GOOD(3),FAIR(4), POOR(5);
		
		int conditionId;
		
		private BookCondition(int conditionId) {
			this.conditionId = conditionId;
		}
		
		public int getConditionId() {
			return conditionId;
		}
		
	}

	
	@Column(name="book_title", unique = false, length = 255, nullable = false)
	private String title;
	
	
	@Column(unique = false, length = 255, nullable = false)
	private String author;
	
	@Column(unique = false, length = 255, nullable = false)
	private String publisher;
	
	@Column(unique = false, length = 4, nullable = false)
	private String publicationDate;
	
	@Column(unique = false, length = 10, nullable = false)
	private String isbn10;

	@Column(unique = false, length = 13, nullable = true)
	private String isbn13;
	
	
	@Column(name="binding_type",columnDefinition = "integer default 1")
	@Enumerated(EnumType.ORDINAL)
	private BindingType binding;
	
	@Column(name="book_condition",columnDefinition = "integer default 1")
	@Enumerated(EnumType.ORDINAL)
	private BookCondition condition;
	
	@Column(name="goodreads_rating",unique = false, nullable = false, columnDefinition = "integer default 1" )
	private int goodReadsRating; //o means no rating; 1~5 means 1 star to 5 stars respectively
	
	@Column(unique = false, nullable = false, columnDefinition = "integer default 1" )
	private int quantity; //o means no rating; 1~5 means 1 star to 5 stars respectively
	
	@Column(unique = false, length = 50, nullable = true)
	private String signed;
	
	@Column(name="about_thisitem",unique = false, length = 255, nullable = false)
	private String aboutThisItem;
	
	@Column(unique = false, nullable = false)
	private BigDecimal price;
	
	@Column(name = "main_image", nullable = false)
	private String mainImage;
	
	private boolean enabled;

	@Column(unique = false, nullable = true)
	private int edition;
	
	@Column(name = "created_time", nullable = false, updatable = false)
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BookImage> images = new HashSet<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookDetails> details = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public BindingType getBinding() {
		return binding;
	}

	public void setBinding(BindingType binding) {
		this.binding = binding;
	}

	public BookCondition getCondition() {
		return condition;
	}

	public void setCondition(BookCondition condition) {
		this.condition = condition;
	}

	public int getGoodReadsRating() {
		return goodReadsRating;
	}

	public void setGoodReadsRating(int goodReadsRating) {
		this.goodReadsRating = goodReadsRating;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getAboutThisItem() {
		return aboutThisItem;
	}

	public void setAboutThisItem(String aboutThisItem) {
		this.aboutThisItem = aboutThisItem;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<BookImage> getImages() {
		return images;
	}

	public void setImages(Set<BookImage> images) {
		this.images = images;
	}

	public List<BookDetails> getDetails() {
		return details;
	}

	public void setDetails(List<BookDetails> details) {
		this.details = details;
	}
	
	

}
