package com.raken.email.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;

/**
 * @author spandana k
 * Utility class for Raken Email App
 */
@Component
public class EmailUtility {
	//	Java email validation permitted by RFC 5322
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    private static final String RAKEN_DOMAIN = "rakenapp.com";
    
	/**
	 * Validates the format of given email addresses
	 * @param emailAddresses
	 * @return List of valid email addresses
	 */
	public static List<String> getValidEmailList(List<String> emailAddresses) {
		List<String> validEmailList = new ArrayList<String>();
		if (emailAddresses != null && !emailAddresses.isEmpty()) {
			validEmailList = emailAddresses.stream().filter(e->isValidEmail(e)).collect(Collectors.toList());
		}
		return validEmailList;
	}
	
	/**
	 * Validates a single email address
	 * @param emailAddress
	 * @return True if valid, False if not valid
	 */
	public static boolean isValidEmail(String emailAddress) {
		Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
	}
	
	/**
	 * Filters out the non rakenapp.com email domains
	 * @param emailAddresses
	 * @return List of rakenapp.com email addresses
	 */
	public static List<String> filterEmails(List<String> emailAddresses) {
		
		List<String> filteredEmails = emailAddresses.stream().filter(e->getDomainName(e).equals(RAKEN_DOMAIN)).collect(Collectors.toList());
		return filteredEmails;
	}
	
	/**
	 * Extracts the domain name from the given email address
	 * @param emailAddress
	 * @return Domain name
	 */
	private static String getDomainName(String emailAddress) {
		return emailAddress.substring(emailAddress.lastIndexOf("@")+1);
	}
	
	
	/**
	 * Returns EmailResponse object with the given statusCode and message
	 * @param statusCode
	 * @param message
	 * @return response
	 */
	public static EmailResponse getEmailResponse(int statusCode, String message) {
		
		EmailResponse response = new EmailResponse();
		response.setStatusCode(statusCode);
		response.setMessage(message);
		
		return response;
	}

	/**
	 * Updates Email Content with weather and quote of the day info
	 * @param emailContent
	 * @param weatherInfo
	 * @param quoteOfTheDay
	 * @return content
	 */
	public static String updateEmailContent(String emailContent, String weatherInfo, String quoteOfTheDay) {
		
		 StringBuilder content = new StringBuilder(emailContent);
		 content = content.append("<br><br>").append(weatherInfo).append("<br>").append(quoteOfTheDay);
		 
		return content.toString();
	}

	/**
	 * Counts the total no. of To, Cc and Bcc recipient emails
	 * @param email
	 * @return totalSize
	 */
	public static int getEmailCount(ElectronicMail email) {
		
		int totalSize = email.getToRecipients().size()
				+(email.getCcRecipients() == null ? 0 : email.getCcRecipients().size())
				+(email.getBccRecipients() == null ? 0 : email.getBccRecipients().size());
		
		return totalSize;
	}
	
	private static List<String> trimSpaces(List<String> emailIds) {
		return emailIds.stream().map(e -> e.trim()).collect(Collectors.toList());
	}
	
public static void cleanEmailContent(ElectronicMail email) {
	email.setToRecipients(trimSpaces(email.getToRecipients()));
	email.setBccRecipients(trimSpaces(email.getBccRecipients()));
	email.setCcRecipients(trimSpaces(email.getCcRecipients()));
	}

}
