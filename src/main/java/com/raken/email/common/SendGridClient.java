package com.raken.email.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sendgrid.SendGrid;

/**
 * @author spandana k
 * Client class to call SendGrid API
 */
@Component
public class SendGridClient {
	
	private static SendGrid instance;
	
	private SendGridClient(@Value("${sendgrid.api.key}") String key) {
		if(instance == null){
			instance = new SendGrid(key);
		}
	}
	
	// To make the SendGrid object singleton
	public static SendGrid getInstance() {
		return instance;
	}
}
