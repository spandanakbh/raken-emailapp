package com.raken.email.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author spandana k
 *
 */
public class SendGridResponse {
	
	@JsonProperty
	private Errors[] errors;

	public Errors[] getErrors() {
		return errors;
	}

	public void setErrors(Errors[] errors) {
		this.errors = errors;
	}
}
