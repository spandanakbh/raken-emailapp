package com.raken.email.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;
import com.raken.email.service.EmailServiceImpl;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

/**
 * @author spandana k
 *
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

	private ElectronicMail electronicMail;

	@InjectMocks
	private EmailServiceImpl emailService;
	
	@Mock
	private SendGrid sendGrid;
	
	@Mock
	private EmailServiceImpl emailServiceMock;
	
	@BeforeEach
	public void initialize() {
		electronicMail = new ElectronicMail();

		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("mailOne@test.com");

		List<String> ccRecipients = new ArrayList<String>();
		ccRecipients.add("mailTwo@test.com");

		List<String> bccRecipients = new ArrayList<String>();
		bccRecipients.add("mailThree@test.com");

		electronicMail.setSubject("Test Send Email API");

		electronicMail.setToRecipients(toRecipients);
		electronicMail.setCcRecipients(ccRecipients);
		electronicMail.setBccRecipients(bccRecipients);
	}
	
	@Test
	public void validateInput_Success() {
		EmailResponse emailresponse = emailService.validateInputAndGenerateResponseObject(electronicMail);
		assertEquals(0, emailresponse.getStatusCode());
		assertEquals(null, emailresponse.getMessage());
	}
	
	@Test
	public void validateInput_Failure_WhenAllToRecipientsInvalid() {
		// Setting invalid email addresses in To Recipients
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("invalidEmailAddress1");
		toRecipients.add("invalidEmailAddress2");

		electronicMail.setToRecipients(toRecipients);
		
		EmailResponse emailresponse = emailService.validateInputAndGenerateResponseObject(electronicMail);
		assertEquals(400, emailresponse.getStatusCode());
		assertEquals("Bad Request, to does not contain any valid email addresses", emailresponse.getMessage());
	}
	
	@Test
	public void validateInput_Failure_WhenAnyEmailAddressesInCcOrBccRecipientsInvalid() {
		// Setting valid and invalid email addresses to Cc and Bcc respectively
		List<String> validCcRecipients = new ArrayList<String>();
		validCcRecipients.add("abc@test.com");

		List<String> invalidBccRecipients = new ArrayList<String>();
		invalidBccRecipients.add("xyz.com");

		electronicMail.setCcRecipients(validCcRecipients);
		electronicMail.setBccRecipients(invalidBccRecipients);
		
		EmailResponse emailresponse = emailService.validateInputAndGenerateResponseObject(electronicMail);
		assertEquals(207, emailresponse.getStatusCode());
		assertEquals("Partially success, email was sent to valid email addresses", emailresponse.getMessage());
	}
	
	@Test
	public void validateInput_ReturnEmptyEmailResponse_WhenEmailFilterEnabled_And_RakenDomainEmailsInToRecipientsEmpty() {

		// Setting non raken domain email addresses in To Recipients
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("one@test.com");
		toRecipients.add("two@email.com");

		electronicMail.setToRecipients(toRecipients);
		
		EmailResponse emptyEmailresponse = new EmailResponse();
		
		EmailResponse emailresponse = emailService.validateInputAndGenerateResponseObject(electronicMail);
		assertEquals(emptyEmailresponse.getStatusCode(), emailresponse.getStatusCode());
		assertEquals(emptyEmailresponse.getMessage(), emailresponse.getMessage());
	}
}
