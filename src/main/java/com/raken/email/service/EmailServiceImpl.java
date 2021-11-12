package com.raken.email.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raken.email.common.EmailUtility;
import com.raken.email.common.SendGridClient;
import com.raken.email.common.SendGridResponse;
import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

/**
 * @author spandana k Service class for Raken Email app
 */
@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${raken.email.filter.enable}")
	private String emailFilterEnabled;

	/**
	 * Sends email(s) to the one or more recipients via sendgrid.com
	 */
	@Override
	public Response sendEmail(ElectronicMail electronicMail) {

		Email fromEmail = new Email(electronicMail.getFrom());
		String subject = electronicMail.getSubject();
		Content content = new Content("text/HTML", electronicMail.getEmailContent());

		Personalization personalization = new Personalization();

		LOG.debug("TO : " + electronicMail.getToRecipients() + ", CC : " + electronicMail.getCcRecipients() + "BCC : "
				+ electronicMail.getBccRecipients());

		for (String toRecipient : electronicMail.getToRecipients()) {
			Email email = new Email(toRecipient);
			personalization.addTo(email);
		}

		List<String> ccRecipients = electronicMail.getCcRecipients();
		if (ccRecipients != null) {
			for (String ccRecipient : ccRecipients) {
				Email email = new Email(ccRecipient);
				personalization.addCc(email);
			}
		}
		List<String> bccRecipients = electronicMail.getBccRecipients();
		if (bccRecipients != null) {
			for (String bccRecipient : bccRecipients) {
				Email email = new Email(bccRecipient);
				personalization.addBcc(email);
			}
		}

		Mail mail = new Mail();
		mail.setFrom(fromEmail);
		mail.setSubject(subject);

		mail.addContent(content);
		mail.addPersonalization(personalization);

		SendGrid sg = SendGridClient.getInstance();
		Request request = new Request();
		Response response = null;
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			LOG.debug("REQUEST :::\n" + request.getBody());

			response = sg.api(request);

			LOG.debug("RESPONSE :::\n" + response.getBody());

			if (response.getBody() != null && response.getBody().length() > 0) {
				ObjectMapper mapper = new ObjectMapper();

				SendGridResponse result = mapper.readValue(response.getBody(), SendGridResponse.class);
				// Setting the error message from SendGrid to the response body
				response.setBody(result.getErrors()[0].getMessage());
			}

		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
		return response;
	}

	public EmailResponse validateInputAndGenerateResponseObject(ElectronicMail email) {

		EmailUtility.cleanEmailContent(email);
		List<String> toRecipients = email.getToRecipients();
		List<String> ccRecipients = email.getCcRecipients();
		List<String> bccRecipients = email.getBccRecipients();

		int inputRecipientCount = EmailUtility.getEmailCount(email);

		EmailResponse emailResponse = new EmailResponse();

		LOG.debug("Email Filter = " + emailFilterEnabled);

		// Set only valid email addresses for each type (To, CC, BCC) of Recipient in
		// the model object
		email.setToRecipients(EmailUtility.getValidEmailList(toRecipients));

		email.setCcRecipients(EmailUtility.getValidEmailList(ccRecipients));

		email.setBccRecipients(EmailUtility.getValidEmailList(bccRecipients));

		int validRecipientCount = EmailUtility.getEmailCount(email);

		if (email.getToRecipients().isEmpty()) {
			emailResponse = EmailUtility.getEmailResponse(400,
					"Bad Request, to does not contain any valid email addresses");
			return emailResponse;
		} else if ("true".equalsIgnoreCase(emailFilterEnabled)) {

			// Set only raken email addresses for each type (To, CC, BCC) of Recipient in
			// the model object
			email.setToRecipients(EmailUtility.filterEmails(email.getToRecipients()));

			email.setCcRecipients(EmailUtility.filterEmails(email.getCcRecipients()));

			email.setBccRecipients(EmailUtility.filterEmails(email.getBccRecipients()));

			if (email.getToRecipients().isEmpty()) {
				emailResponse = EmailUtility.getEmailResponse(400,
						"Bad Request, To does not contain any rakenapp.com domain email addresses");
				return emailResponse;
			}

			int filteredRecipientCount = EmailUtility.getEmailCount(email);
			// If email filter is enabled, log the emails.
			if (validRecipientCount > filteredRecipientCount) {
				logFilteredEmails(email, toRecipients, ccRecipients, bccRecipients);
			}
		}

		// If there exists at least one valid email address and one or more invalid
		// email addresses,
		// then update status code to multi-status (207) and update user that request is
		// only partially successful.
		if (validRecipientCount < inputRecipientCount) {
			emailResponse = EmailUtility.getEmailResponse(207,
					"Partially success, email was sent to valid email addresses");
		}
		return emailResponse;
	}

	private void logFilteredEmails(ElectronicMail email, List<String> toRecipients, List<String> ccRecipients,
			List<String> bccRecipients) {
		// If there exists non rakenapp.com email domains in the valid email recipients
		// (TO, CC & BCC) list, then log the email details
		LOG.info("FROM = " + email.getFrom());
		LOG.info("TO : " + toRecipients + ", CC : " + ccRecipients + ", BCC : " + bccRecipients);
		LOG.info("SUBJECT = " + email.getSubject());
		LOG.info("BODY = " + email.getEmailContent());
	}
}
