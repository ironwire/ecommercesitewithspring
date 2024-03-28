package org.yiqixue.common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;

@Component
public class EmailSetting {

	public EmailSetting() {}
	
	@Value("${smtp.server.host.name}")
	private String smtpHost;
	
	@Value("${port.number}")
	private String portNumber;
	
	@Value("${smtp.server.username}")
	private String userName;


	@Value("${smtp.server.password}")
	private String password;

	@Value("${from.email.address}")
	private String fromEmail;//FROM_EMAIL;
	
	@Value("${sender.name}")
	public String senderName;//SENDER_NAME;
	
	@Value("${email.subject}")
	public String emailSubject;//EMAIL_SUBJECT;

	@Value("${email.content}")
	public String emailContent;//EMAIL_CONTENT;

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	
	
}
