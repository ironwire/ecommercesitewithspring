package org.yiqixue.common.book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.yiqixue.common.IdBasedEntity;
import org.yiqixue.common.store.Store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.StreamReadCapability;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.yiqixue.admin.controller.book.BookDeserializer;

@Entity
@Table(name="books")
@JsonDeserialize(using = BookDeserializer.class)
public class Book extends IdBasedEntity {
	
	public enum BindingType {
		
		SOFTCOVER(1), 
		
		HARDCOVER(2), 
		
		STAPLED(3), 
		
		SPIRAL(4);
		
		private int code;
		
	    private BindingType(int code) {
	    	this.code =code;
	    }
	    
	    @JsonValue
	    public int getCode() {
	    	return this.code;
	    }
		
	    @JsonCreator
	    public static BindingType fromCode(int code) {
	        return Stream.of(BindingType.values()).
	        		filter(targetEnum -> targetEnum.code==code).findFirst().orElse(null);
	    }
		
//		SOFTCOVER(0), 
//		
//		HARDCOVER(1), 
//		
//		STAPLED(2), 
//		
//		SPIRAL(3);
//		
//		private int typeCode;
//		
//		private BindingType(int typeCode) {
//			this.typeCode = typeCode;
//		}
//		
//		public int getTypeCode() {
//			return typeCode;
//		}
//		
//		
//	    @JsonCreator
//	    public static BindingType decode(final int code) {
//	    	return Stream.of(BindingType.values()).
//	    			filter(targetEnum -> targetEnum.typeCode==code).findFirst().orElse(null);
//	    	
//	    }
//	    
//	    @JsonValue
//	    public int getCode() {
//	    	return typeCode;
//	    }
	}
	
	
	public enum BookCondition {
		ASNEW(0),FINE(1),VERYGOOD(2),GOOD(3),FAIR(4), POOR(5);
		
		private int conditionId;
		
		private BookCondition(int conditionId) {
			this.conditionId = conditionId;
		}
		@JsonValue
		public int getConditionId() {
			return conditionId;
		}
		
	    @JsonCreator
	    public static BookCondition fromCode(int code) {
	        return Stream.of(BookCondition.values()).
	        		filter(targetEnum -> targetEnum.conditionId==code).findFirst().orElse(null);
	    }
		
	}

	
	@Column(name="book_title", unique = false, length = 255, nullable = false)
	private String title;
	
	
	@Column(unique = false, length = 255, nullable = false)
	private String author;
	
	@Column(unique = false, length = 255, nullable = false)
	private String publisher;
	
	@Column(unique = false, length = 10, nullable = false)
	private String publicationDate;
	
	@Column(unique = false, length = 10, nullable = false)
	private String isbn10;

	@Column(unique = false, length = 13, nullable = true)
	private String isbn13;
	
	
	@Column(name="binding_type",columnDefinition = "integer default 1")
	@Enumerated(EnumType.ORDINAL)
	
	private BindingType bindingType;

	@Column(name="book_condition",columnDefinition = "integer default 1")
	@Enumerated(EnumType.ORDINAL)
	private BookCondition bookCondition;
	
	@Column(name="goodreads_rating",unique = false, nullable = false, columnDefinition = "integer default 1" )
	private int goodReadsRating; //o means no rating; 1~5 means 1 star to 5 stars respectively
	
	@Column(unique = false, nullable = false, columnDefinition = "integer default 1" )
	private int quantity; //o means no rating; 1~5 means 1 star to 5 stars respectively
	
	@Column(unique = false, length = 50, nullable = true)
	private String signed;
	
	@Column(name="about_thisitem",unique = false, length = 1024, nullable = false)
	private String aboutThisItem;
	
	@Column(unique = false, nullable = false)
	private BigDecimal price;
	
//	@Column(name = "main_image", nullable = false)
//	private String mainImage;
	
	private boolean enabled;

	@Column(unique = false, nullable = true)
	private int edition;
	
	@Column(name = "created_time", nullable = false, updatable = false,columnDefinition = "DATE DEFAULT CURRENT_DATE")
	private Date createdTime;

	@Column(name = "updated_time", columnDefinition = "DATE DEFAULT CURRENT_DATE")
	private Date updatedTime;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@JsonManagedReference
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

	public BindingType getBindingType() {
		return bindingType;
	}

	public void setBindingType(BindingType bindingType) {
		this.bindingType = bindingType;
	}

	//从原来的返回BookCondition, 改为返回int，直接就把Json的返回值给制成对应的数值行而非字符串
	public BookCondition getBookCondition() {
		return bookCondition;//.getConditionId();
	}

	public void setCondition(BookCondition bookCondition) {
		this.bookCondition = bookCondition;
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

//	public String getMainImage() {
//		return mainImage;
//	}
//
//	public void setMainImage(String mainImage) {
//		this.mainImage = mainImage;
//	}

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
	
	public void addImage(String imageName, String imageType) {
		this.images.add(new BookImage(imageName, imageType, this));
	}

	public boolean containsImageName(String imageName) {
		Iterator<BookImage> iterator = images.iterator();

		while (iterator.hasNext()) {
			BookImage image = iterator.next();
			if (image.getName().equals(imageName)) {
				return true;
			}
		}

		return false;
	}

	
	public Book() {}
	public Book(Integer id) {
		this.id = id;
	}
	
	public Book(String title, String aboutThisItem, Category category, Store store) {
		super();
		this.aboutThisItem = aboutThisItem;
		this.title = title;
		this.category = category;
		this.store = store;
	}

	public Book(String title, String author, String publisher, String publicationDate, String isbn10, String isbn13,
			BindingType bindingType, BookCondition bookCondition, int goodReadsRating, int quantity, String signed,
			String aboutThisItem, BigDecimal price, boolean enabled, int edition, Category category, Store store
			) {
		super();
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publicationDate = publicationDate;
		this.isbn10 = isbn10;
		this.isbn13 = isbn13;
		this.bindingType = bindingType;
		this.bookCondition = bookCondition;
		this.goodReadsRating = goodReadsRating;
		this.quantity = quantity;
		this.signed = signed;
		this.aboutThisItem = aboutThisItem;
		this.price = price;
		this.enabled = enabled;
		this.edition = edition;
		this.category = category;
		this.store = store;
	
	}
	
	
}
