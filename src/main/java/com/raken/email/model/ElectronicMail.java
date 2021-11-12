package com.raken.email.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author spandana k
 *
 */
public class ElectronicMail {

	@NotEmpty
	@Size(min = 1)
	@JsonProperty("to")
	private List<String> toRecipients;
	
	@JsonProperty("cc")
	private List<String> ccRecipients;
	
	@JsonProperty("bcc")
	private List<String> bccRecipients;
	
	@NotEmpty
	@Size(min = 1)
	private String subject;
	
	@NotEmpty
	@Size(min = 1)
	private String from;
	
	@JsonProperty("content")
	private String emailContent;
	
	public List<String> getToRecipients() {
		return toRecipients;
	}

	public void setToRecipients(List<String> toRecipients) {
		this.toRecipients = toRecipients;
	}

	public List<String> getCcRecipients() {
		return ccRecipients;
	}

	public void setCcRecipients(List<String> ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	public List<String> getBccRecipients() {
		return bccRecipients;
	}

	public void setBccRecipients(List<String> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailContent() {
		return emailContent;
	}
	
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
