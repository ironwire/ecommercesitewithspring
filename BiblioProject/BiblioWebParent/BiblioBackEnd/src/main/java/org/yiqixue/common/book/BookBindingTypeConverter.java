package org.yiqixue.common.book;

import java.util.Arrays;

import org.yiqixue.common.book.Book.BindingType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//@Converter(autoApply = true)
//public class BookBindingTypeConverter implements AttributeConverter<Book.BindingType, Integer> {

//	@Override
//	public Integer convertToDatabaseColumn(BindingType type) {
//		
//		return type.getTypeCode();
//	}
//
//	@Override
//	public BindingType convertToEntityAttribute(Integer typeCode) {
//		
//		return Arrays.stream(Book.BindingType.values())
//				.filter(b -> b.getTypeCode() == typeCode)
//				.findFirst()
//				.orElseThrow(IllegalArgumentException::new);
//	}

	
//}
