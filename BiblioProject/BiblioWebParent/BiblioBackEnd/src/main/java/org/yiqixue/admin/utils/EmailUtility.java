package org.yiqixue.admin.utils;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.yiqixue.common.EmailSetting;


@Component
public class EmailUtility {
	

	
	public static JavaMailSenderImpl prepareMailSender(EmailSetting setting) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(setting.getSmtpHost());
		mailSender.setPort(Integer.parseInt(setting.getPortNumber()));
		mailSender.setUsername(setting.getUserName());
		mailSender.setPassword(setting.getPassword());
		
		Properties mailProperties = new Properties();
		//mailProperties.setProperty("mail.smtp.auth", "TLS");
		mailProperties.setProperty("mail.smtp.auth", "SSL");
		mailProperties.setProperty("mail.smtp.starttls.enable", "true");
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}

}
