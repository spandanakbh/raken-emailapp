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
import org.springframework.beans.factory.annotation.Value;

import com.raken.email.controller.EmailController;
import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;
import com.raken.email.service.EmailService;
import com.raken.email.service.QuoteService;
import com.raken.email.service.WeatherService;
import com.sendgrid.Response;

/**
 * @author spandana k
 *
 */
@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

	@Value("${raken.email.filter.enable}")
	private String emailFilterEnabled;

	private ElectronicMail electronicMail;

	@Mock
	private EmailService emailService;

	@Mock
	private WeatherService weatherService;

	@Mock
	private QuoteService quoteService;

	@InjectMocks
	private EmailController emailController;

	@BeforeEach
	public void initialize() {

		electronicMail = new ElectronicMail();

		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("mailOne@test.com");

		List<String> ccRecipients = new ArrayList<String>();
		ccRecipients.add("mailTwo@test.com");

		List<String> bccRecipients = new ArrayList<String>();
		bccRecipients.add("mailThree@test.com");

		electronicMail.setFrom("from@test.com");

		electronicMail.setSubject("Test Send Email API");

		electronicMail.setToRecipients(toRecipients);
		electronicMail.setCcRecipients(ccRecipients);
		electronicMail.setBccRecipients(bccRecipients);

		electronicMail.setEmailContent("This is a test email from Raken Send Email App");

	}

	@Test
	public void sendEmail_Success() {
		EmailResponse serviceResponse = new EmailResponse();
		
		Mockito.when(emailService.validateInputAndGenerateResponseObject(Mockito.any(ElectronicMail.class))).thenReturn(serviceResponse);
		
		Response response = new Response();
		response.setStatusCode(202);
		Mockito.when(emailService.sendEmail(Mockito.any(ElectronicMail.class))).thenReturn(response);
		
		EmailResponse emailresponse = emailController.send(electronicMail, true);
		assertEquals(202, emailresponse.getStatusCode());
		
		emailresponse = emailController.send(electronicMail, false);
		assertEquals(202, emailresponse.getStatusCode());
		assertEquals("Success", emailresponse.getMessage());
	}

	@Test
	public void sendEmail_Failure_WhenAllToRecipientsInvalid() {
		
		EmailResponse serviceResponse = new EmailResponse();
		serviceResponse.setStatusCode(400);
		serviceResponse.setMessage("Bad Request, to does not contain any valid email addresses");
		Mockito.when(emailService.validateInputAndGenerateResponseObject(Mockito.any(ElectronicMail.class))).thenReturn(serviceResponse);

		// Setting invalid email addresses in To Recipients
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("invalidEmailAddress1");
		toRecipients.add("invalidEmailAddress2");

		electronicMail.setToRecipients(toRecipients);
		EmailResponse emailresponse = emailController.send(electronicMail, true);
		assertEquals(400, emailresponse.getStatusCode());
		assertEquals("Bad Request, to does not contain any valid email addresses", emailresponse.getMessage());

	}

	@Test
	public void sendEmail_PartialSuccess_WhenAnyEmailAddressesInCcOrBccRecipientsInvalid() {

		// Setting valid and invalid email addresses to Cc and Bcc respectively
		List<String> validCcRecipients = new ArrayList<String>();
		validCcRecipients.add("abc@test.com");

		List<String> invalidBccRecipients = new ArrayList<String>();
		invalidBccRecipients.add("xyz.com");

		electronicMail.setCcRecipients(validCcRecipients);
		electronicMail.setBccRecipients(invalidBccRecipients);
		
		EmailResponse serviceResponse = new EmailResponse();
		serviceResponse.setStatusCode(207);
		serviceResponse.setMessage("Partially success, email was sent to valid email addresses");
		Mockito.when(emailService.validateInputAndGenerateResponseObject(Mockito.any(ElectronicMail.class))).thenReturn(serviceResponse);
		
		Response response = new Response();
		response.setStatusCode(202);
		Mockito.when(emailService.sendEmail(Mockito.any(ElectronicMail.class))).thenReturn(response);
		
		EmailResponse emailresponse = emailController.send(electronicMail, true);
		assertEquals(207, emailresponse.getStatusCode());
		assertEquals("Partially success, email was sent to valid email addresses", emailresponse.getMessage());
	}
	
	@Test
	public void sendEmail_Success_WhenEmailFilterEnabled_And_RakenDomainEmailsInToRecipientsExist() {
		
		System.out.println("Email Filter = " + emailFilterEnabled);
		
		// Setting non raken domain email addresses in To Recipients
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("one@rakenapp.com");
		toRecipients.add("two@email.com");

		electronicMail.setToRecipients(toRecipients);
		
		EmailResponse serviceResponse = new EmailResponse();
		serviceResponse.setStatusCode(202);
		serviceResponse.setMessage("Success");
		Mockito.when(emailService.validateInputAndGenerateResponseObject(Mockito.any(ElectronicMail.class))).thenReturn(serviceResponse);
		
		Response response = new Response();
		response.setStatusCode(202);
		Mockito.when(emailService.sendEmail(Mockito.any(ElectronicMail.class))).thenReturn(response);
		
		EmailResponse emailresponse = emailController.send(electronicMail, true);
		assertEquals(202, emailresponse.getStatusCode());
		assertEquals("Success", emailresponse.getMessage());
	}

	@Test
	public void sendEmail_Failure_WhenEmailFilterEnabled_And_RakenDomainEmailsInToRecipientsEmpty() {
		
		System.out.println("Email Filter = " + emailFilterEnabled);
		
		// Setting non raken domain email addresses in To Recipients
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("one@test.com");
		toRecipients.add("two@email.com");

		electronicMail.setToRecipients(toRecipients);
		
		EmailResponse serviceResponse = new EmailResponse();
		serviceResponse.setStatusCode(400);
		serviceResponse.setMessage("Bad Request, To does not contain any rakenapp.com domain email addresses");
		Mockito.when(emailService.validateInputAndGenerateResponseObject(Mockito.any(ElectronicMail.class))).thenReturn(serviceResponse);
		
		EmailResponse emailresponse = emailController.send(electronicMail, true);
		assertEquals(400, emailresponse.getStatusCode());
		assertEquals("Bad Request, To does not contain any rakenapp.com domain email addresses", emailresponse.getMessage());
	}
}
