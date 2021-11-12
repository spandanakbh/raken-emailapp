package com.raken.email.model;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author spandana k
 *
 */
@JsonComponent
public class EmailResponse {
	
	@JsonProperty
	private int statusCode;
	
	@JsonProperty
	private String message;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}