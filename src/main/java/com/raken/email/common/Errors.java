package com.raken.email.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author spandana k
 *
 */
public class Errors {
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String field;
	
	@JsonProperty
	private String help;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}
}
