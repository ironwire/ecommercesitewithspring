package org.yiqixue.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yiqixue.admin.utils.BindingTypeStringToEnumConverter;


@Configuration
public class MvcConfigure implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BindingTypeStringToEnumConverter());
    }
}
