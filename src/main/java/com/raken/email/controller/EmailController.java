package com.raken.email.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raken.email.common.EmailUtility;
import com.raken.email.model.ElectronicMail;
import com.raken.email.model.EmailResponse;
import com.raken.email.service.EmailService;
import com.raken.email.service.QuoteService;
import com.raken.email.service.WeatherService;
import com.sendgrid.Response;

/**
 * @author spandana k
 * Controller class which handles Raken Email service requests
 */
@RestController
public class EmailController {

	private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

	@Value("${raken.email.filter.enable}")
	private String emailFilterEnabled;

	@Autowired
	EmailService emailService;

	@Autowired
	WeatherService weatherService;

	@Autowired
	QuoteService quoteService;

	private static String quoteOfTheDay = null;
	private static int yesterday = 0;

	/**
	 * This method is used to send emails to the valid recipients in the given input
	 * 
	 * @param ElectronicMail (JSON)
	 * @return Response Body (JSON)
	 */
	@RequestMapping(value = "/sendemail", method = RequestMethod.POST, consumes = "application/json")
	public EmailResponse send(@RequestBody @Valid ElectronicMail email,
			@RequestParam(required = false, name = "enrich") boolean enrich) {

		LOG.debug("Enrich = " + enrich);
		LOG.debug("Email Filter = " + emailFilterEnabled);

		int statusCode = 0;
		String message = null;
		Response response = null;

		EmailResponse emailResponse = emailService.validateInputAndGenerateResponseObject(email);
		
		if (emailResponse.getStatusCode() == 400) {
			return emailResponse;
		}

		// Add current Weather in Carlsbad (Raken HQ) and Quote of the day if enrich is true
		String weatherInfo = null;
		if (enrich == true) {
			weatherInfo = weatherService.getWeatherForecast(); // not caching as this keeps changing
			loadQuoteOfTheDay(); // caching this as the quote of day will not change in a day
			String emailContent = email.getEmailContent();
			email.setEmailContent(EmailUtility.updateEmailContent(emailContent, weatherInfo, quoteOfTheDay));
		}
		response = emailService.sendEmail(email);
		statusCode = response.getStatusCode();
		message = response.getBody();
		
		// If status code is 202, check if the request is partial success and send the appropriate response.
		if (statusCode == 202) {
			if (emailResponse.getStatusCode() == 207) {
					return emailResponse;
			} else {
				message = "Success";
			}
		}
		emailResponse.setStatusCode(statusCode);
		emailResponse.setMessage(message);
		return emailResponse;
	}

	/**
	 * Calling the QuoteOfTheDay API only once per day as it will return the same
	 * quote for all the requests in a day.
	 */
	private synchronized void loadQuoteOfTheDay() { // Synchronized to make sure we get only 1 call to quote service API in a day.
		int currentDay = LocalDateTime.now().getDayOfYear();
		if (currentDay - yesterday != 0 || quoteOfTheDay == null) {
			yesterday = currentDay;
			quoteOfTheDay = quoteService.getQuoteOfTheDay();
		}
	}
}
