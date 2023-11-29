package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		System.out.println(userPhotosPath);
		
		registry.addResourceHandler("/"+dirName+"/**")
			.addResourceLocations("file:"+userPhotosPath +"/"); //for mac/linux, no / after file:
		
		String cateDirName = "../category-images";
		Path cateImagesDir = Paths.get(cateDirName);
		
		String cateImagesPath = cateImagesDir.toFile().getAbsolutePath();
		System.out.println(cateImagesPath);
		
		registry.addResourceHandler("/category-images/**")
			.addResourceLocations("file:"+cateImagesPath +"/"); //for mac/linux, no / after file:
		
	}

}
