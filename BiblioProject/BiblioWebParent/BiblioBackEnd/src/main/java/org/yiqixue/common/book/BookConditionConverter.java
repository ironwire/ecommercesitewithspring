package org.yiqixue.common.book;

import java.util.Arrays;

import org.yiqixue.common.book.Book.BookCondition;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookConditionConverter implements AttributeConverter<Book.BookCondition, Integer> {

	@Override
	public Integer convertToDatabaseColumn(BookCondition condition) {
	
		return condition.getConditionId();
	}

	@Override
	public BookCondition convertToEntityAttribute(Integer conditionId) {
		
		return Arrays.stream(Book.BookCondition.values())
				.filter(c -> c.getConditionId() == conditionId)
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
