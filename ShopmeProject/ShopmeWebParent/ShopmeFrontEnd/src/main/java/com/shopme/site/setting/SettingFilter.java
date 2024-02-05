package com.shopme.site.setting;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.setting.Setting;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SettingFilter implements Filter {

	@Autowired
	private SettingService sservice;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		String url = servletRequest.getRequestURI().toString();
		
		if(url.endsWith(".css")||url.endsWith(".js")||
				url.endsWith(".jpg")||url.endsWith(".png")||url.endsWith(".jpeg")) {
			chain.doFilter(request, response);
			return;
		}
		List<Setting> generalSettings = sservice.getGeneralSettings();
		
		generalSettings.forEach(setting -> {
			request.setAttribute(setting.getKey(), setting.getValue());
			System.out.println(setting.getKey() + " == > " + setting.getValue());
		});
		
		//request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
		
	
		
		chain.doFilter(request, response);
		
		
	}

}
