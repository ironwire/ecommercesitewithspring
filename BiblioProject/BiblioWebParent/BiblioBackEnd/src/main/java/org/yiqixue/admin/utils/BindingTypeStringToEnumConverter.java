package org.yiqixue.admin.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.yiqixue.common.book.Book.BindingType;

@Component
public class BindingTypeStringToEnumConverter implements Converter<String, BindingType> {
    @Override
    public BindingType convert(String source) {
        try {
            return BindingType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
