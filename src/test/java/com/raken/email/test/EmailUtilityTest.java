package com.raken.email.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raken.email.common.EmailUtility;
import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;

/**
 * @author spandana k
 *
 */
@ExtendWith(MockitoExtension.class)
public class EmailUtilityTest {
	
	private List<String> emailList = new ArrayList<String>();
	private List<String> emptyEmailList = new ArrayList<String>();
	private String validEmailAddress = "validEmail@test.com";
	private String invalidEmailAddress = "invalidEmail";
	
	private ElectronicMail electronicMail;
	
	@BeforeEach
	public void initialize() {
		
		emailList.add("testEmail@test.com");
		emailList.add("testEmail1@test.com");
		emailList.add("testEmail2@test.com");
		
		electronicMail = new ElectronicMail();
		
		List<String> toRecipients = new ArrayList<String>();
		toRecipients.add("mail@test.com");
		toRecipients.add("mailOne@test.com");

		List<String> ccRecipients = new ArrayList<String>();
		ccRecipients.add("mailTwo@test.com");

		List<String> bccRecipients = new ArrayList<String>();
		bccRecipients.add("mailThree@test.com");

		electronicMail.setToRecipients(toRecipients);
		electronicMail.setCcRecipients(ccRecipients);
		electronicMail.setBccRecipients(bccRecipients);
	}
	
	@Test
	public void getValidEmailListWhenValidEmails() {
		
		List<String> validEmails = EmailUtility.getValidEmailList(emailList);
		assertEquals(3, validEmails.size());
	}
	
	@Test
	public void getValidEmailListWhenOneOrMoreInvalidEmails() {
		
		emailList.add("testInvalidEmail");
		emailList.add("testEmail3@test");
		List<String> validEmails = EmailUtility.getValidEmailList(emailList);
		assertEquals(4, validEmails.size());
	}
	
	@Test
	public void getValidEmailListWhenEmailListEmpty() {
		
		List<String> validEmails = EmailUtility.getValidEmailList(emptyEmailList);
		assertEquals(0, validEmails.size());
		assertEquals(new ArrayList<String>(), validEmails);
	}
	
	@Test
	public void isValidEmailWhenEmailValidAndInvalid() {
		
		 assertEquals(true, EmailUtility.isValidEmail(validEmailAddress));
		 assertEquals(false, EmailUtility.isValidEmail(invalidEmailAddress));
	}
	
	@Test
	public void filterEmailsWhenBothRakenAndNonRakenEmailsExist() {
		
		emailList.add("testEmail3@rakenapp.com");
		emailList.add("testEmail4@nonraken.com");
		List<String> filteredEmails = EmailUtility.filterEmails(emailList);
		assertEquals(1, filteredEmails.size());
		assertEquals("testEmail3@rakenapp.com", filteredEmails.get(0));
	}
	
	@Test
	public void filterEmailsWhenNonRakenEmailExistAndRakenEmailDoesNotExist() {
		
		List<String> filteredEmails = EmailUtility.filterEmails(emailList);
		assertEquals(0, filteredEmails.size());
		assertEquals(new ArrayList<String>(), filteredEmails);
	}
	
	@Test
	public void filterEmailsWhenEmailListEmpty() {
		
		List<String> filteredEmails = EmailUtility.filterEmails(emptyEmailList);
		assertEquals(0, filteredEmails.size());
		assertEquals(new ArrayList<String>(), filteredEmails);
	}
	
	@Test
	public void getEmailResponseWhenStatusCodeAndMessageSetWithValues() {
		
		EmailResponse emailResponse = EmailUtility.getEmailResponse(202, "Success");
		assertEquals(202, emailResponse.getStatusCode());
		assertEquals("Success", emailResponse.getMessage());
	}
	
	@Test
	public void getEmailResponseWhenStatusCodeAndMessageSetWithDefaultValues() {
		
		int statusCode = 0;
		String message = null;
		EmailResponse emailResponse = EmailUtility.getEmailResponse(statusCode, message);
		assertEquals(0, emailResponse.getStatusCode());
		assertEquals(null, emailResponse.getMessage());
	}
	
	@Test
	public void updateEmailContentSuccess() {
		
		String content = EmailUtility.updateEmailContent("This is an Email from Raken App", "Current Weather, Carlsbad", "Never give up");
		assertEquals(true, content.contains("This is an Email from Raken App"));
		assertEquals(true, content.contains("Current Weather, Carlsbad"));
		assertEquals(true, content.contains("Never give up"));
		assertEquals(false, content.contains("Something other than input data"));
	}
	
	@Test
	public void getEmailCountWhenEmailRecipientsNotEmpty() {
		
		int count = EmailUtility.getEmailCount(electronicMail);
		assertEquals(4, count);
	}
	
	@Test
	public void getEmailCountWhenEmailRecipientsEmpty() {
		
		electronicMail = new ElectronicMail();
		electronicMail.setToRecipients(new ArrayList<String>());
		electronicMail.setCcRecipients(new ArrayList<String>());
		electronicMail.setBccRecipients(new ArrayList<String>());
		
		int count = EmailUtility.getEmailCount(electronicMail);
		assertEquals(0, count);
	}
}
