package org.yiqixue.admin.controller.book;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

import org.yiqixue.common.book.Book;
import org.yiqixue.common.book.BookImage;
import org.yiqixue.common.book.Category;
import org.yiqixue.common.book.Book.BindingType;
import org.yiqixue.common.book.Book.BookCondition;
import org.yiqixue.common.store.Store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

public class BookDeserializer extends StdDeserializer<Book> {
	
	private static final long serialVersionUID = 1L;

	public BookDeserializer() {
		this(null);
	}
	
	public BookDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Book deserialize(JsonParser jp, DeserializationContext ctxt) 
			throws IOException, JsonProcessingException {
			
			JsonNode node = jp.getCodec().readTree(jp);

			String title = node.get("title").asText();
	        String author = node.get("author").asText();
	        
	        String publisher = node.get("publisher").asText();
	        
	        String publicationDate = node.get("publicationDate").asText();
	        
	        
	        BigDecimal price = new BigDecimal(node.get("price").asDouble());
	        int quantity = node.get("quantity").asInt();//(Integer) ((IntNode) node.get("quantity")).numberValue();
	        
	        String isbn10 = node.get("isbn10").asText();
	        String isbn13 = node.get("isbn13").asText();
	        int categoryId = node.get("categoryId").asInt();
	        int storeId = node.get("storeId").asInt();
	        
	        BindingType bindingType = BindingType.fromCode(node.get("bindingType").asInt());
	        BookCondition bookCondition = BookCondition.fromCode(node.get("bookCondition").asInt());
	        
	        int goodReadsRating = node.get("goodReadsRating").asInt();
	        
	        String aboutThisItem = node.get("aboutThisItem").asText();
	        
	        String signed = node.get("signed").asText();
	        
	        boolean enabled = node.get("enabled").asBoolean();
	        int edition = (Integer) ((IntNode) node.get("edition")).numberValue();
	        
	        return new Book(title, author, publisher, publicationDate, isbn10, isbn13,
	    			bindingType, bookCondition, goodReadsRating, quantity, signed,
	    			aboutThisItem, price, enabled, edition, new Category(categoryId), new Store(storeId)
	    			); 
	        
	}
}
