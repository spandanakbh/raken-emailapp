package com.raken.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.raken.email.model.QuoteInfo;

/**
 * @author spandana k
 * Service class to call Quote service API
 */
@Service
public class QuoteServiceImpl implements QuoteService {
	
	private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);

	@Override
	public String getQuoteOfTheDay() {
		StringBuilder quoteOfTheDay = new StringBuilder("Quote Of The Day : ");
		String url = "http://quotes.rest/qod.json";
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<QuoteInfo> response = restTemplate
              .getForEntity(url, QuoteInfo.class);
		
		LOG.debug(response.getBody().toString());
		
		quoteOfTheDay.append(response.getBody().getContents().getQuotes().get(0).getQuote());
		
		return quoteOfTheDay.toString();
	}
}
